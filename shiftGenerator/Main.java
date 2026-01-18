import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        DummyWorkerFactory testData = new DummyWorkerFactory();
        List<TimeSlot> timeSlots = testData.getTimeSlots();
        List<Position> positions = testData.getPositions();
        List<Worker> workers = testData.getWorkers();
        Option option = testData.getOption();
        int days = testData.getDays();
        // シフト生成初日日付取得
        LocalDate firstDate = workers.stream().flatMap(w -> w.getAssignShiftTimeslotIds().keySet().stream()).min(LocalDate::compareTo).orElseThrow();

        // シフト格納場所作成
        ShiftGenerator generator = new ShiftGenerator(days, firstDate, timeSlots, positions, workers, option);

        // シフト生成実行
        Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> shift = generator.generate();

        // シフト表示
        printShift(shift, timeSlots, positions);
        // 不足枠表示
        printShortages(generator.getShortageSlots());
        // 警告枠表示
        printWarnings(generator.getWarningSlots());
        // 従業員月労働時間表示
        printWorkerMonthMinutes(workers);
    }

    private static void printShift(Map<LocalDate, Map<TimeSlot, Map<Position, List<Integer>>>> shift, List<TimeSlot> timeSlots, List<Position> positions) {
        for (LocalDate date : shift.keySet()) {
            System.out.println(date + ":");

            Map<TimeSlot, Map<Position, List<Integer>>> day = shift.get(date);

            for (TimeSlot slot : timeSlots) {
                Map<Position, List<Integer>> slotMap = day.getOrDefault(slot, Map.of());

                for (Position pos : positions) {
                    System.out.print("  " + slot.getName() + " " + pos.getName() + ": ");
                    List<Integer> workers = slotMap.getOrDefault(pos, List.of());

                    if (workers.isEmpty()) {
                        System.out.print("-");
                    } else {
                        workers.forEach(id -> System.out.print(id + " "));
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void printShortages(List<shortageSlot> shortages) {
        System.out.println("\n【管理者対応が必要な枠】");
        if (shortages.isEmpty()) {
            System.out.println("該当なし");
            return;
        }
        System.out.println("＜責任者未割当枠＞");
        System.out.println("-----時間帯-----");
        boolean hasAuthorityShortageForSlot = shortages.stream().anyMatch(s -> s.getShortageType() == ShortageType.SLOT_AUTHORITY);
        if (!hasAuthorityShortageForSlot) {
            System.out.println("該当なし");
        } else {
            for (shortageSlot s : shortages) {
                if (s.getShortageType() != ShortageType.SLOT_AUTHORITY) continue;
                System.out.println(
                    s.getDate() + " " +
                    s.getTimeSlot().getName() + " " +
                    "（必要:" + s.getRequired() +
                    " / 割当:" + s.getAssigned() + "）"
                );
            }
        }

        System.out.println("\n-----ポジション別-----");
        boolean hasAuthorityShortageForPosition = shortages.stream().anyMatch(s -> s.getShortageType() == ShortageType.POSITION_AUTHORITY);
        if (!hasAuthorityShortageForPosition) {
            System.out.println("該当なし");
        } else {
            for (shortageSlot s : shortages) {
                if (s.getShortageType() != ShortageType.POSITION_AUTHORITY) continue;
                System.out.println(
                    s.getDate() + " " +
                    s.getPosition().getName() + " " +
                    "（必要:" + s.getRequired() +
                    " / 割当:" + s.getAssigned() + "）"
                );
            }
        }
        
        System.out.println("\n＜労働者未割当枠＞");
        boolean hasWorkerShortage = shortages.stream().anyMatch(s -> s.getShortageType() == ShortageType.WORKER);
        if (!hasWorkerShortage) {
            System.out.println("該当なし");
        } else {
            for (shortageSlot s : shortages) {
                if (s.getShortageType() != ShortageType.WORKER) continue;
                System.out.println(
                    s.getDate() + " " +
                    s.getTimeSlot().getName() + " " +
                    s.getPosition().getName() +
                    "（必要:" + s.getRequired() +
                    " / 割当:" + s.getAssigned() + "）"
                );
            }
        }

        System.out.println("\n＜先輩未割当枠＞");
        boolean hasSeniorShortage = shortages.stream().anyMatch(s -> s.getShortageType() == ShortageType.SENIOR);
        if (!hasSeniorShortage) {
            System.out.println("該当なし");
        } else {
            for (shortageSlot s : shortages) {
                if (s.getShortageType() != ShortageType.SENIOR) continue;
                System.out.println(
                    s.getDate() + " " +
                    s.getTimeSlot().getName() + " " +
                    s.getPosition().getName() +
                    "（必要:" + s.getRequired() +
                    " / 割当:" + s.getAssigned() + "）"
                );
            }
        }
    }

    private static void printWarnings(List<WarningSlot> warnings) {
        System.out.println("\n【警告枠一覧】");
        System.out.println("＜不適合タグ警告枠＞");
        if (warnings.isEmpty()) {
            System.out.println("該当なし");
            return;
        }
        for (WarningSlot w : warnings) {
            System.out.println(
                w.getDate() + " " +
                w.getTimeSlot().getName() + " " +
                w.getPosition().getName() + " " +
                "\n不適合タグ:" + w.getNonconformTag() + ", 警告対象従業員ID:" + w.getWarningWorkers()
            );
        }
    }

    private static void printWorkerMonthMinutes(List<Worker> workers) {
        System.out.println("\n【従業員月労働時間】");
        for (Worker w : workers) {
            System.out.println("Worker " + w.getId() + ": " + w.getMonthlyWorkMinutes() + "分");
        }
    }
}
