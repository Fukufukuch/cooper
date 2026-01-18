import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.*;

public class ShiftGenerator {
    private final int days;
    private final LocalDate firstDate;
    private final List<TimeSlot> timeSlots;
    private final List<Position> positions;
    private final List<Worker> workers;
    private final List<Worker> authorityWorkers;
    private final Map<Integer, Worker> workerMap;
    private final Option option;

    private List<shortageSlot> shortageSlots = new ArrayList<>();
    private List<WarningSlot> warningSlots = new ArrayList<>();

    public ShiftGenerator(int days, LocalDate firstDate, List<TimeSlot> timeSlots, List<Position> positions, List<Worker> workers, Option option) {
        this.days = days;
        this.firstDate = firstDate;
        this.timeSlots = timeSlots;
        this.positions = positions;
        this.workers = workers;
        this.authorityWorkers = extractAuthorityWorkers(workers);
        this.workerMap = workers.stream().collect(Collectors.toMap(Worker::getId, w -> w));
        this.option = option;
    }

    public Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> generate() {
        Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> shift = new LinkedHashMap<>();

        for (int d = 0; d < days; d++) {
            LocalDate currentDate = firstDate.plusDays(d);
            System.out.println(currentDate);
            Map<TimeSlot, Map<Position, List<Integer>>> dayMap = new LinkedHashMap<>();
            shift.put(currentDate, dayMap);

            for (TimeSlot currentSlot : timeSlots) {
                Map<Position, List<Integer>> slotMap = new LinkedHashMap<>();
                dayMap.put(currentSlot, slotMap);

                // phase1:ポジション別責任者割当
                phase1_AuthorityForPos(currentSlot, currentDate, dayMap, slotMap);

                // phase2:時間帯別責任者割当
                phase2_AuthorityForSlot(currentSlot, currentDate, dayMap, slotMap);

                // phase3:最小人数・責任者割当
                phase3_PosMin(currentSlot, currentDate, dayMap, slotMap);

                // phase4:新人制約割当
                phase4_Newcomer(currentSlot, currentDate, dayMap, slotMap);

                // phase5:通常割当
                phase5_Normal(currentSlot, currentDate, dayMap, slotMap);

                // phase6:不足・警告通知チェック
                vailDate(currentSlot, currentDate, dayMap, slotMap);
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

    // phase1:ポジション別責任者割当
    private void phase1_AuthorityForPos(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        List<Position> minSortedPositions = sortListByMinWorkers(positions);

        for (Position currentPosition : minSortedPositions) {
            List<Integer> shiftWorkerList = slotMap.computeIfAbsent(currentPosition, k -> new ArrayList<>());

            int minRequired = minWorkersRequired(currentPosition, currentSlot);
            int authorityForPos = countAuthorityAssignedByPosition(currentPosition, slotMap);

            List<Worker> hasAuthorityCandidates = authorityWorkers.stream()
                    .filter(w -> canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option))
                    .sorted(Comparator.comparingInt((Worker w) -> w.getAvailablePositionIds().size()) // 対応ポジション数昇順
                        .thenComparingInt((Worker w) -> w.getMonthlyWorkMinutes()))
                    .toList();

            for (Worker w : hasAuthorityCandidates) {
                if (shiftWorkerList.size() >= minRequired || authorityForPos >= currentPosition.getRequireAuthorityWorkers()) break; // 最低人数超過時割り当て終了
                assign(w, currentSlot, shiftWorkerList); // 労働者割当
                authorityForPos++;
                System.out.println("ポジション : " + currentPosition.getName() + " Phase1(ポジション別責任者割当)");
            }    
        }
    }

    // phase2:時間帯別責任者割当
    private void phase2_AuthorityForSlot(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        List<Position> minSortedPositions = sortListByMinWorkers(positions);

        int authorityForSlot = countAuthorityAssignedBySlot(slotMap);

        for (Position currentPosition : minSortedPositions) {
            List<Integer> shiftWorkerList = slotMap.computeIfAbsent(currentPosition, k -> new ArrayList<>());

            int minRequired = minWorkersRequired(currentPosition, currentSlot);
            if (shiftWorkerList.size() >= minRequired) continue;

            List<Worker> hasAuthorityCandidates = authorityWorkers.stream()
                .filter(w -> canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option))
                .sorted(Comparator.comparingInt((Worker w) -> w.getAvailablePositionIds().size()) // 対応ポジション数昇順
                    .thenComparingInt((Worker w) -> w.getMonthlyWorkMinutes()))
                .toList();

            for (Worker w : hasAuthorityCandidates) {
                if (shiftWorkerList.size() >= minRequired || authorityForSlot >= currentSlot.getRequireAuthorityWorkers()) break; // 最低人数超過時割り当て終了
                assign(w, currentSlot, shiftWorkerList); // 労働者割当
                authorityForSlot++;
                System.out.println("ポジション : " + currentPosition.getName() + " Phase1(時間帯別責任者割当)");
            }    
        }
    }

    // phase3:最少人数割当メソッド
    private void phase3_PosMin(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        List<Position> minSortedPositions = sortListByMinWorkers(positions); // ポジション最少必要人数昇順ソート
        for (Position currentPosition : minSortedPositions) {
            List<Integer> shiftWorkerList = slotMap.computeIfAbsent(currentPosition, k -> new ArrayList<>());
            
            int required = minWorkersRequired(currentPosition, currentSlot); // 最少人数取得

            List<Worker> candidates = workers.stream()
                    .filter(w -> canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) //割り当て可能確認
                    .sorted(Comparator.comparingInt((Worker w) -> w.getAvailablePositionIds().size()) // 対応ポジション数昇順
                        .thenComparingInt((Worker w) -> w.getMonthlyWorkMinutes())) // 月労働時間昇順
                    .toList();

            for (Worker w : candidates) {
                if (shiftWorkerList.size() >= required) break; // 最低人数超過時割り当て終了
                assign(w, currentSlot, shiftWorkerList); // 労働者割当
                System.out.println("ポジション : " + currentPosition.getName() + " Phase1");
            }
        }
    }

    // phase4:新人制約割当メソッド
    private void phase4_Newcomer(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        if (!hasSeniorRequired(slotMap, option)) return; // 先輩がすでに配置済み

        List<Position> maxSortedPositions = sortListByMaxWorkers(positions); // ポジション最大必要人数昇順ソート

        for (Position currentPosition : maxSortedPositions) { // ポジション最大必要人数が多いものから順に入れる
            
            List<Integer> shiftWorkerList = slotMap.get(currentPosition);
            if (shiftWorkerList == null) continue; // 割り当て済みの労働者がいなければ次のポジションへ（==割り当て可能な労働者が存在しない）

            while (shiftWorkerList.size() < maxWorkersRequired(currentPosition, currentSlot)
                    && hasSeniorRequired(slotMap, option)) { // 最大人数まで埋まっていないかつ先輩が必要

                Optional<Worker> candidate = workers.stream()
                        .filter(w -> !w.isNewcomer()) // 新人ではないか
                        .filter(w -> canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) // 割り当て可能確認
                        .sorted(Comparator.comparingInt((Worker w) -> w.getAvailablePositionIds().size()) // 対応ポジション数昇順ソート
                        .thenComparingInt((Worker w) -> w.getMonthlyWorkMinutes())) // 月労働時間昇順ソート
                        .findFirst();

                if (candidate.isEmpty()) break; //割り当て可能な人がいない場合終了

                assign(candidate.get(), currentSlot, shiftWorkerList); // 労働者割当
                System.out.println("ポジション : " + currentPosition.getName() + " Phase2");
            }
        }
    }

    // phase5:通常割当メソッド
    private void phase5_Normal(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        List<Position> maxSortedPositions = sortListByMaxWorkers(positions);

        for (Position currentPosition : maxSortedPositions) {
            List<Integer> shiftWorkerList = slotMap.get(currentPosition);
            if (shiftWorkerList == null) continue;

            while (shiftWorkerList.size() < maxWorkersRequired(currentPosition, currentSlot)) {
                
                Optional<Worker> candidate = workers.stream()
                        .filter((Worker w) -> canAssign(currentDate, w, currentPosition, currentSlot, dayMap, shiftWorkerList, option)) // 割り当て可能確認
                        .sorted(Comparator.comparingInt((Worker w) -> w.getAvailablePositionIds().size()) // 対応ポジション数昇順ソート
                        .thenComparingInt((Worker w) -> w.getMonthlyWorkMinutes())) // 月労働時間昇順ソート
                        .findFirst();

                if (candidate.isEmpty()) break; //割り当て可能な人がいない場合終了

                assign(candidate.get(), currentSlot, shiftWorkerList); // 労働者割当
                System.out.println("ポジション : " + currentPosition.getName() + " Phase3");
            }
        }
    }

    // phase6:不足・警告チェックメソッド
    private void vailDate(TimeSlot currentSlot, LocalDate currentDate, Map<TimeSlot, Map<Position, List<Integer>>> dayMap, Map<Position, List<Integer>> slotMap) {
        
        int slotAuthorityCount = countAuthorityAssignedBySlot(slotMap);

        // 時間帯別責任者不足チェック
        if (slotAuthorityCount < currentSlot.getRequireAuthorityWorkers()) {
            addShortageSlot(currentDate, currentSlot, null, currentSlot.getRequireAuthorityWorkers(), countAuthorityAssignedBySlot(slotMap), ShortageType.SLOT_AUTHORITY);
        }

        for (Position currentPosition : positions) {
            List<Integer> shiftWorkerList = slotMap.getOrDefault(currentPosition, List.of());
            
            int PosAuthorityCount = countAuthorityAssignedByPosition(currentPosition, slotMap);
            // ポジション別責任者不足チェック
            if (PosAuthorityCount < currentPosition.getRequireAuthorityWorkers()) {
                addShortageSlot(currentDate, null, currentPosition, currentPosition.getRequireAuthorityWorkers(), countAuthorityAssignedByPosition(currentPosition, slotMap), ShortageType.POSITION_AUTHORITY);
            }

            // 人数不足チェック
            if (shiftWorkerList.size() < minWorkersRequired(currentPosition, currentSlot)) {
                addShortageSlot(currentDate, currentSlot, currentPosition, minWorkersRequired(currentPosition, currentSlot), shiftWorkerList.size(), ShortageType.WORKER);
            }

            // 先輩不足チェック
            if (hasSeniorRequired(slotMap, option)) {
                addShortageSlot(currentDate, currentSlot, currentPosition, option.getRequiredSeniorWorkers(), countSeniorAssigned(slotMap), ShortageType.SENIOR);
            }

            // タグ不整合チェック
            for (int set : extractNonconformTags(workers)) {
                if (isNonconformTagAssigned(shiftWorkerList, workers, set)) {
                    addWarningSlot(currentDate, currentSlot, currentPosition, set, hasNonconformTagWorkersList(shiftWorkerList, workers, set), WarningType.NONCONFORM_TAG);
                }
            }
        }
    }

    // 労働者追加処理メソッド
    private void assign(Worker worker, TimeSlot slot, List<Integer> shift) {
        shift.add(worker.getId());
        worker.addTimes(slot.getWorkMinutes());
        System.out.print("割り当て労働者ID : " + worker.getId() + "  時間帯 : " + slot.getName() +
            "\n日労働時間 : " + worker.getDailyWorkMinutes() + " ");
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

    // 最大人数取得メソッド
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
    private boolean hasSeniorRequired(Map<Position, List<Integer>> slotMap, Option option) {
        return countSeniorAssigned(slotMap) < option.getRequiredSeniorWorkers();
    }

    // 先輩割当数カウントメソッド
    private int countSeniorAssigned(Map<Position, List<Integer>> slotMap) {
        int count = 0;
        for (List<Integer> ids : slotMap.values()) {
            for (int id : ids) {
                Worker w = workerMap.get(id);
                if (w != null && !w.isNewcomer()) count++;
            }
        }
        return count;
    }

    // 時間帯責任者割当数カウントメソッド
    private int countAuthorityAssignedBySlot(Map<Position, List<Integer>> slotMap) {
        int count = 0;
        for (List<Integer> ids: slotMap.values()) {
            for (int id : ids) {
                Worker w = workerMap.get(id);
                if (w != null && w.isHasAuthority()) count++;
            }
        }
        return count;
    }

    // ポジション別責任者割り当て数カウントメソッド
    private int countAuthorityAssignedByPosition(Position currentPosition, Map<Position, List<Integer>> slotMap) {
        int count = 0;
        for (int id : slotMap.getOrDefault(currentPosition, List.of())) {
            Worker w = workerMap.get(id);
            if (w != null && w.isHasAuthority()) count++;
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

    // 最小人数昇順ソート
    List<Position> sortListByMinWorkers(List<Position> positions) {
        List<Position> list = new ArrayList<>(positions);
        list.sort(Comparator.comparingInt(Position::getMinWorkers));
        return list;
    }

    // 最大人数昇順ソート
    List<Position> sortListByMaxWorkers(List<Position> positions) {
        List<Position> list = new ArrayList<>(positions);
        list.sort(Comparator.comparingInt(Position::getMaxWorkers).reversed());
        return list;
    }

    // タイムスロット最少追加人数昇順ソートメソッド 
    public static List<TimeSlot> sortListByMinExtraWorkers(List<TimeSlot> timeSlots) { 
        List<TimeSlot> list = new ArrayList<>(timeSlots); 
        list.sort(Comparator.comparingInt(TimeSlot::getMinExtraWorkers)); 
        return list; 
    }

    // 不適合タグ抽出メソッド
    public static Set<Integer> extractNonconformTags(List<Worker> workers) {
        Set<Integer> set = new HashSet<>();
        for (Worker w : workers) {
            set.addAll(w.getNonconformTags());
        }
        return set;
    }

    // 日労働時間リセットメソッド
    public static void resetDailyWorkMinutes(List<Worker> workers) {
        for (Worker w : workers) {
            w.resetDailyWorkMinutes();
        }
    }
}
