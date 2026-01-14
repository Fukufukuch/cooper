import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyWorkerFactory {
    private final List<TimeSlot> timeSlots;
    private final List<Position> positions;
    private final List<Worker> workers;
    private final Option option = new Option(8000, 720, 5000, 1, 1);
    private final int days;

    public DummyWorkerFactory() {
        this.timeSlots = createTimeSlots();
        this.positions = createPositions();
        this.workers = createWorkers();
        this.days = 7; // 生成期間設定
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public Option getOption() {
        return option;
    }

    public int getDays() {
        return days;
    }

    public List<Worker> createWorkers() {
        List<Worker> workers = new ArrayList<>();

        workers.add(createWorker1(this.option));
        workers.add(createWorker2(this.option));
        workers.add(createWorker3(this.option));
        workers.add(createWorker4(this.option));
        workers.add(createWorker5(this.option));
        workers.add(createWorker6(this.option));
        workers.add(createWorker7(this.option));
        workers.add(createWorker8(this.option));
        workers.add(createWorker9(this.option));
        workers.add(createWorker10(this.option));
        workers.add(createWorker11(this.option));
        workers.add(createWorker12(this.option));
        return workers;
    }

    private static List<TimeSlot> createTimeSlots() {
        List<TimeSlot> list = new ArrayList<>();
        list.add(new TimeSlot(0, "早番", 360, 540, -1, -1, true));    //6-9h
        list.add(new TimeSlot(1, "午前", 540, 840, 0, 0, false));    //9-14h
        list.add(new TimeSlot(2, "中番", 840, 1080, -1, -1, false));   //14-18h
        list.add(new TimeSlot(3, "午後", 1080, 1200, 1, 1, false));  //18-20h
        list.add(new TimeSlot(4, "遅番", 1200, 1410, -1, -1, true));  //20-23.5h
        return list;
    }

    private static List<Position> createPositions() {
        List<Position> list = new ArrayList<>();
        
        list.add(new Position(0, "レジ", 1, 2, true));
        list.add(new Position(1, "キッチン", 2, 3, false));
        return list;
    }

    private static Worker createWorker1(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(2, 3, 4));           // 月：14-23.5
        prefs.put(LocalDate.of(2026, 1, 6), Set.of());                  // 火：休暇
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(0, 1, 2, 3));        // 水：6-20
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(2, 3, 4));           // 木：14-23.5
        prefs.put(LocalDate.of(2026, 1, 9), Set.of());                  // 金：休暇
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(0, 1, 2, 3));       // 土：6-20
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(0, 1, 2, 3));       // 日：6-20

        return new Worker(
            1,
            true,
            3000, 
            25000,
            prefs,
            Set.of(0, 1),
            Set.of(),
            option
        );
    }

    private static Worker createWorker2(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());               // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(0, 1, 2, 3));     // 火：6-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(0, 1, 2, 3));     // 水：6-20
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());               // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(0, 1, 2, 3));     // 金：6-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(0, 1, 2, 3));    // 土：6-20
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(0, 1, 2, 3));    // 日：6-20

        return new Worker(
            2,
            true,
            5000, 
            30000,
            prefs,
            Set.of(0, 1),
            Set.of(),
            option
        );
    }

    private static Worker createWorker3(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(0, 1, 2));        // 月：6-18
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(0, 1, 2));        // 火：6-18
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(0, 1, 2));        // 水：6-18
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(0, 1, 2));        // 木：6-18
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(0, 1, 2));        // 金：6-18
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());              // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());              // 日：休暇

        return new Worker(
            3,
            true,
            1800, 
            20000,
            prefs,
            Set.of(0),
            Set.of(),
            option
        );
    }

    private static Worker createWorker4(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());                  // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(4));             // 火：20-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(4));             // 水：20-23.5
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());                  // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(4));             // 金：20-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(2, 3, 4));          // 日：14-23.5

        return new Worker(
            4,
            false,
            300, 
            3000,
            prefs,
            Set.of(1),
            Set.of(),
            option
        );
    }

    private static Worker createWorker5(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());                  // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of());                  // 火：休暇
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                  // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());                  // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of());                  // 金：休暇
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(2, 3, 4));          // 土：14-23.5
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(1, 2, 3));          // 日：9-20

        return new Worker(
            5,
            false,
            500, 
            7000,
            prefs,
            Set.of(0, 1),
            Set.of(0),
            option
        );
    }

    private static Worker createWorker6(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(2, 3));              // 月：14-20
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(2, 3));              // 火：14-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(2, 3));              // 水：14-20
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(2, 3));              // 木：14-20
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(2, 3));              // 金：14-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                 // 日：休暇

        return new Worker(
            6,
            false,
            800,
            12000,
            prefs,
            Set.of(1),     // キッチン専属
            Set.of(),
            option
        );
    }

    private static Worker createWorker7(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(1, 2));              // 月：9-18
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(1, 2));              // 火：9-18
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(1, 2));              // 水：9-18
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(1, 2));              // 木：9-18
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(1, 2));              // 金：9-18
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                 // 日：休暇

        return new Worker(
            7,
            false,
            1500,
            18000,
            prefs,
            Set.of(0, 1),       // 両対応
            Set.of(0),     // レジ優先（仮仕様）
            option
        );
    }

    private static Worker createWorker8(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(3, 4));              // 月：18-23.5
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(3, 4));              // 火：18-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                  // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(3, 4));              // 木：18-23.5
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(3, 4));              // 金：18-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(0, 1, 2));          // 土：6-17
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(1, 2, 3, 4));       // 日：9-23.5

        return new Worker(
            8,
            true,
            2400,
            27000,
            prefs,
            Set.of(0),   // キッチン
            Set.of(1),
            option
        );
    }

    private static Worker createWorker9(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(4));                  // 月：20-23.5
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(3, 4));                  // 火：18-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                      // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(4));                  // 木：20-23.5
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(3, 4));                  // 金：18-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                     // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(2, 3, 4));              // 日：14-23.5

        return new Worker(
            9,
            false,
            120,        // ほぼ未稼働
            5000,    // 上限高
            prefs,
            Set.of(0, 1),
            Set.of(1),
            option
        );
    }

    private static Worker createWorker10(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(0));                  // 月：6-9
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(1));                  // 火：9-14
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(0));                  // 水：6-9
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(1));                  // 木：9-14
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(0));                  // 金：6-9
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                     // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                     // 日：休暇

        return new Worker(
            10,
            true,
            400, 
            24000,
            prefs,
            Set.of(1),
            Set.of(),
            option
        );
    }

    private static Worker createWorker11(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());                  // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of());                  // 火：休暇
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                  // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());                  // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of());                  // 金：休暇
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(1, 2, 3));          // 土：9-20
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(1, 2, 3));          // 日：9-20

        return new Worker(
            11,
            false,
            500, 
            7000,
            prefs,
            Set.of(0, 1),
            Set.of(0, 1),
            option
        );
    }

    private static Worker createWorker12(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(1, 2, 3));                   // 月：9-20
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(3));                     // 火：18-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(1, 2, 3, 4));                // 水：9-23.5
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(0, 1, 2, 3));                // 木：6-20
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(1, 2, 3));                   // 金：9-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(3, 4));                     // 土：18-23.5
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                         // 日：休暇

        return new Worker(
            12,
            true,
            1500, 
            15000,
            prefs,
            Set.of(0, 1),
            Set.of(),
            option
        );
    }
}
