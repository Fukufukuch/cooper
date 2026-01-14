import java.time.LocalDate;
import java.util.*;

public class ShiftGenerator {
    private final int days;
    private final LocalDate firstDate;
    private final List<TimeSlot> timeSlots;
    private final List<Position> positions;
    private final List<Worker> workers;
    private final Option option;

    private List<shortageSlot> shortageSlots = new ArrayList<>();
    private List<WarningSlot> warningSlots = new ArrayList<>();

    public ShiftGenerator(int days, LocalDate firstDate, List<TimeSlot> timeSlots, List<Position> positions, List<Worker> workers, Option option) {
        this.days = days;
        this.firstDate = firstDate;
        this.timeSlots = timeSlots;
        this.positions = positions;
        this.workers = workers;
        this.option = option;
    }

    public Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> generate() {
        Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> shift = new LinkedHashMap<>();

        List<Position> maxSortedPositions = sortListByMaxWorkers(positions);
        List<TimeSlot> minSortedTimeSlots = sortListByMinExtraWorkers(timeSlots);

        for (int d = 0; d < days; d++) {
            LocalDate currentDate = firstDate.plusDays(d);
            Map<TimeSlot, Map<Position, List<Integer>>> dayMap = new LinkedHashMap<>();
            shift.put(currentDate, dayMap);

            for (TimeSlot currentSlot : minSortedTimeSlots) {
                Map<Position, List<Integer>> slotMap = new LinkedHashMap<>();
                dayMap.put(currentSlot, slotMap);

                // phase1:最小人数・責任者割当
                assignMinWorkers(positions, workers, currentSlot, currentDate, dayMap, slotMap, option);

                for (Position currentPosition : maxSortedPositions) {
                    List<Integer> shiftWorkerList = slotMap.computeIfAbsent(currentPosition, k -> new ArrayList<>());
                    slotMap.put(currentPosition, shiftWorkerList);

                    // phase2:新人制約割当
                    assignUnnewcomerIfPossible(workers, shiftWorkerList, currentPosition, currentSlot, currentDate, dayMap, option);

                    // phase3:通常割当
                    assignNormal(workers, shiftWorkerList, currentPosition, currentSlot, currentDate, dayMap, option);

                    // 管理者不足チェック
                    int authorityCount = countAuthorityAssigned(slotMap, workers);
                    if (isAuthorityRequired(currentPosition, currentSlot) && authorityCount < option.getRequiredAuthorityWorkers()) {
                        addShortageSlot(currentDate, currentSlot, currentPosition, option.getRequiredAuthorityWorkers(), countAuthorityAssigned(slotMap, workers), ShortageType.AUTHORITY);
                    }

                    // 人数不足チェック
                    if (shiftWorkerList.size() < minWorkersRequired(currentPosition, currentSlot)) {
                        addShortageSlot(currentDate, currentSlot, currentPosition, minWorkersRequired(currentPosition, currentSlot), shiftWorkerList.size(), ShortageType.WORKER);
                    }

                    // 先輩不足チェック
                    if (hasSeniorRequired(slotMap, workers, option)) {
                        addShortageSlot(currentDate, currentSlot, currentPosition, option.getRequiredSeniorWorkers(), countSeniorAssigned(slotMap, workers), ShortageType.SENIOR);
                    }

                    // タグ不整合チェック
                    for (int set : extractNonconformTags(workers)) {
                        if (isNonconformTagAssigned(shiftWorkerList, workers, set)) {
                            addWarningSlot(currentDate, currentSlot, currentPosition, set, hasNonconformTagWorkersList(shiftWorkerList, workers, set), WarningType.NONCONFORM_TAG);
                        }
                    }
                }
            }
            resetDailyWorkMinutes(workers);
        }
        return shift;
    }

    // 不足枠リスト取得メソッド
    public List<shortageSlot> getShortageSlots() {
        return shortageSlots;
    }

    // 警告枠リスト取得メソッド
    public List<WarningSlot> getWarningSlots() {
        return warningSlots;
    }

    // 管理者割当メソッド
    private void assignAuthority(List<Worker> assignWorkers, List<Integer> shiftWorkerList, Position currentPosition, TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Option option) {
        int needed = option.getRequiredAuthorityWorkers();
        int count = 0;

        for (Worker w : extractAuthorityWorkers(assignWorkers)) {
            if (shiftWorkerList.size() >= maxWorkersRequired(currentPosition, currentSlot)) break;
            if (!canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) continue;

            shiftWorkerList.add(w.getId());
            addProcess(w, currentSlot);
            count++;

            if (count >= needed) break;
        }
    }

    // 最少人数割当メソッド
    private void assignMinWorkers(List<Position> positions, List<Worker> workers, TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap, Option option) {
        List<Position> minSortedPositions = sortListByMinWorkers(positions);
        for (Position currentPosition : minSortedPositions) {
            List<Integer> shiftWorkerList = slotMap.computeIfAbsent(currentPosition, k -> new ArrayList<>());

            List<Worker> assignWorkers = assignWorkers(currentDate, workers, currentPosition, currentSlot, dayMap, shiftWorkerList, option);
            assignWorkers = sortListByMonthMinutes(assignWorkers);
            assignWorkers = sortListByAvailablePositionsCount(assignWorkers);

            // 管理者割当
            if (isAuthorityRequired(currentPosition, currentSlot)) {
                assignAuthority(assignWorkers, shiftWorkerList, currentPosition, currentSlot, currentDate, dayMap, option);
                if (shiftWorkerList.size() > currentPosition.getMinWorkers()) continue;
            }

            // 最少人数割当
            for (Worker w : assignWorkers) {
                if (shiftWorkerList.size() >= minWorkersRequired(currentPosition, currentSlot)) break;
                if (!canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) continue;

                shiftWorkerList.add(w.getId());
                addProcess(w, currentSlot);
            }
        }
    }

    // 新人制約割当メソッド
    private void assignUnnewcomerIfPossible(List<Worker> workers, List<Integer> shiftWorkerList, Position currentPosition, TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Option option) {
        List<Worker> assignWorkers = assignWorkers(currentDate, workers, currentPosition, currentSlot, dayMap, shiftWorkerList, option);
        assignWorkers = sortListByMonthMinutes(assignWorkers);
        assignWorkers = sortListByAvailablePositionsCount(assignWorkers);

        if (assignWorkers.isEmpty()) return;
        if (!hasSeniorRequired(dayMap.get(currentSlot), workers, option)) return;

        for (Worker w : assignWorkers) {
            if (!w.isNewcomer()) continue;
            if (!canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) continue;

            shiftWorkerList.add(w.getId());
            addProcess(w, currentSlot);
            break;
        }
    }

    // 通常割当メソッド
    private void assignNormal(List<Worker> workers, List<Integer> shiftWorkerList, Position currentPosition, TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Option option) {
        List<Worker> assignWorkers = assignWorkers(currentDate, workers, currentPosition, currentSlot, dayMap, shiftWorkerList, option);
        assignWorkers = sortListByMonthMinutes(assignWorkers);
        assignWorkers = sortListByAvailablePositionsCount(assignWorkers);

        for (Worker w : assignWorkers) {
            if (shiftWorkerList.size() >= maxWorkersRequired(currentPosition, currentSlot)) break;
            if (!canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) continue;

            shiftWorkerList.add(w.getId());
            addProcess(w, currentSlot);
        }
    }

    // 不足枠追加メソッド
    private void addShortageSlot(LocalDate date, TimeSlot slot, Position position, int required, int assigned, ShortageType shortageType) {
        shortageSlots.add(new shortageSlot(date, slot, position, required, assigned, shortageType));
    }

    // 警告枠追加メソッド
    private void addWarningSlot(LocalDate date, TimeSlot slot, Position position, int nonconformTag, List<Integer> warningWorkers, WarningType warningType) {
        warningSlots.add(new WarningSlot(date, slot, position, nonconformTag, warningWorkers, warningType));
    }

    // 最少必要人数取得メソッド
    public int minWorkersRequired(Position position, TimeSlot timeSlot) {
        return Math.max(1, position.getMinWorkers() + timeSlot.getMinExtraWorkers());
    }

    // 最大必要人数取得メソッド
    public int maxWorkersRequired(Position position, TimeSlot timeSlot) {
        return Math.max(1, position.getMaxWorkers() + timeSlot.getMaxExtraWorkers());
    }

    // 割当可能従業員リスト取得メソッド
    public static List<Worker> assignWorkers(LocalDate date, List<Worker> workers, Position position, TimeSlot slot, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, List<Integer> shiftWorkerList, Option option) {
        List<Worker> list = new ArrayList<>();
        for (Worker w : workers) {
            if (canAssign(date, w, position, slot, dayMap, shiftWorkerList, option)) {
                list.add(w);
            }
        }
        return list;
    }

    // 割当可能判定メソッド
    public static boolean canAssign(LocalDate date, Worker w, Position position, TimeSlot slot, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, List<Integer> shiftWorkerList, Option option) {
        if (!w.getAvailablePositionIds().contains(position.getId())) return false;                              // ポジション対応確認
        if (!w.getAssignShiftTimeslotIds().containsKey(date)) return false;                                     // 日付対応確認
        if (!w.getAssignShiftTimeslotIds().get(date).contains(slot.getId())) return false;                      // 時間帯対応確認
        if (isCovered(dayMap, slot, w)) return false;                                                           // 二重割当確認
        if (shiftWorkerList.contains(w.getId())) return false;                                                  // 同枠内二重割当確認
        if (w.getDailyWorkMinutes() + slot.getWorkMinutes() > option.getMaxWorktimeofDay()) return false;       // 日労働時間上限確認
        if (w.getMonthlyWorkMinutes() + slot.getWorkMinutes() > option.getMaxWorktimeofMonth()) return false;   // 月労働時間上限確認
        return true;
    }

    // 二重割当確認メソッド
    public static boolean isCovered(Map<TimeSlot, Map<Position, List<Integer>>> dayMap, TimeSlot timeSlot, Worker worker) {
        Map<Position, List<Integer>> slotMap = dayMap.get(timeSlot);
        for (List<Integer> ids : slotMap.values()) {
            if (ids.contains(worker.getId())) return true;
        }
        return false;
    }

    // 先輩必要判定メソッド
    public static boolean hasSeniorRequired(Map<Position, List<Integer>> slotMap, List<Worker> workers, Option option) {
        return countSeniorAssigned(slotMap, workers) < option.getRequiredSeniorWorkers();
    }

    // 先輩割当数カウントメソッド
    public static int countSeniorAssigned(Map<Position, List<Integer>> slotMap, List<Worker> workers) {
        int count = 0;
        for (List<Integer> ids : slotMap.values()) {
            for (int id : ids) {
                for (Worker w : workers) {
                    if (w.getId() == id && !w.isNewcomer()) count++;
                }
            }
        }
        return count;
    }

    // 管理者必要判定メソッド
    public static boolean isAuthorityRequired(Position position, TimeSlot timeSlot) {
        return position.isRequiresAuthority() || timeSlot.isRequiresAuthority();
    }

    // 管理者割当確認メソッド
    public static boolean hasAuthorityAssigned(Map<Position, List<Integer>> slotMap, List<Worker> workers) {
        return countAuthorityAssigned(slotMap, workers) > 0;
    }

    // 管理者割当数カウントメソッド
    public static int countAuthorityAssigned(Map<Position, List<Integer>> slotMap, List<Worker> workers) {
        int count = 0;
        for (List<Integer> ids : slotMap.values()) {
            for (int id : ids) {
                for (Worker w : workers) {
                    if (w.getId() == id && w.isHasAuthority()) count++;
                }
            }
        }
        return count;
    }

    // 管理者従業員抽出メソッド
    public static List<Worker> extractAuthorityWorkers(List<Worker> workers) {
        List<Worker> list = new ArrayList<>();
        for (Worker w : workers) {
            if (w.isHasAuthority()) list.add(w);
        }
        return list;
    }

    // 不適合タグ割当確認メソッド
    public static boolean isNonconformTagAssigned(List<Integer> shiftWorkerList, List<Worker> workers, int nonconformTag) {
        if (hasNonconformTagWorkersList(shiftWorkerList, workers, nonconformTag).size() > 1) return true;
        else return false;
    }

    // 不適合タグ該当従業員リスト取得メソッド
    public static List<Integer> hasNonconformTagWorkersList(List<Integer> shiftWorkerList, List<Worker> workers, int nonconformTag) {
        List<Integer> result = new ArrayList<>();

        for (int id : shiftWorkerList) {
            for (Worker w : workers) {
                if (w.getId() == id && w.getNonconformTags().contains(nonconformTag)) {
                    result.add(id);
                    break;
                }
            }
        }
        return result;
    }

    // 不適合タグ抽出メソッド
    public static Set<Integer> extractNonconformTags(List<Worker> workers) {
        Set<Integer> set = new HashSet<>();
        for (Worker w : workers) {
            set.addAll(w.getNonconformTags());
        }
        return set;
    }

    // 月労働時間昇順ソートメソッド
    public static List<Worker> sortListByMonthMinutes(List<Worker> workers) {
        List<Worker> list = new ArrayList<>(workers);
        list.sort(Comparator.comparingInt(Worker::getMonthlyWorkMinutes));
        return list;
    }

    // 対応ポジション数昇順ソートメソッド
    public static List<Worker> sortListByAvailablePositionsCount(List<Worker> workers) {
        List<Worker> list = new ArrayList<>(workers);
        list.sort(Comparator.comparingInt(w -> w.getAvailablePositionIds().size()));
        return list;
    }

    // ポジション最少必要人数昇順ソートメソッド
    public static List<Position> sortListByMinWorkers(List<Position> positions) {
        List<Position> list = new ArrayList<>(positions);
        list.sort(Comparator.comparingInt(Position::getMinWorkers));
        return list;
    }

    // ポジション最大必要人数昇順ソートメソッド
    public static List<Position> sortListByMaxWorkers(List<Position> positions) {
        List<Position> list = new ArrayList<>(positions);
        list.sort(Comparator.comparingInt(Position::getMaxWorkers));
        return list;
    }

    // タイムスロット最少追加人数昇順ソートメソッド
    public static List<TimeSlot> sortListByMinExtraWorkers(List<TimeSlot> timeSlots) {
        List<TimeSlot> list = new ArrayList<>(timeSlots);
        list.sort(Comparator.comparingInt(TimeSlot::getMinExtraWorkers));
        return list;
    }

    // 労働時間加算メソッド
    public static void addProcess(Worker worker, TimeSlot slot) {
        worker.addTimes(slot.getWorkMinutes());
    }

    // 日労働時間リセットメソッド
    public static void resetDailyWorkMinutes(List<Worker> workers) {
        for (Worker w : workers) {
            w.resetDailyWorkMinutes();
        }
    }
}
