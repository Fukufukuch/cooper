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
        // 人数不足枠表示
        printShortages(generator.getShortageSlots());
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
        System.out.println("＜管理者未割当枠＞");
        if (shortages.stream().noneMatch(s -> !s.isAuthorityAssigned())) {
            System.out.println("該当なし");
        } else {
            for (shortageSlot s : shortages) {
                if (s.isAuthorityAssigned()) continue;
                System.out.println(
                    s.getDate() + " " +
                    s.getTimeSlot().getName() + " " +
                    s.getPosition().getName() +
                    "（必要:" + s.getRequired() +
                    " / 割当:" + s.getAssigned() + "）"
                );
            }
        }
        
        System.out.println("\n＜労働者未割当枠＞");
        if (shortages.stream().noneMatch(s -> s.isAuthorityAssigned())) {
            System.out.println("該当なし");
            return;
        } else {
            for (shortageSlot s : shortages) {
                if (!s.isAuthorityAssigned()) continue;
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

    private static void printWorkerMonthMinutes(List<Worker> workers) {
        System.out.println("\n【従業員月労働時間】");
        for (Worker w : workers) {
            System.out.println("Worker " + w.getId() + ": " + w.getMonthlyWorkMinutes() + "分");
        }
    }
}
