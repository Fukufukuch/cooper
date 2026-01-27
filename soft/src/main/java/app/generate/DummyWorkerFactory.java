package app.generate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.domain.*;

public class DummyWorkerFactory implements ShiftDataSource {
    //private final List<TimeSlot> timeSlots;
    //private final List<Position> positions;
    private final List<Worker> workers;
    private final Option option = new Option(8000, 720, 5000, 1, 7, LocalDate.of(2026, 1, 5));

    public DummyWorkerFactory() {
        //this.timeSlots = createTimeSlots();
        //this.positions = createPositions();
        this.workers = createWorkers();
    }
/*
    @Override
    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    @Override
    public List<Position> getPositions() {
        return positions;
    }
*/
    @Override
    public List<Worker> getWorkers() {
        return workers;
    }

    @Override
    public Option getOption() {
        return option;
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

    /*
    private static List<TimeSlot> createTimeSlots() {
        List<TimeSlot> list = new ArrayList<>();
        list.add(new TimeSlot(1, "早番", 360, 540, -1, -1, 2));    //6-9h
        list.add(new TimeSlot(2, "午前", 540, 840, 0, 0, 1));    //9-14h
        list.add(new TimeSlot(3, "中番", 840, 1080, -1, -1, 1));   //14-18h
        list.add(new TimeSlot(4, "午後", 1080, 1200, 1, 1, 1));  //18-20h
        list.add(new TimeSlot(5, "遅番", 1200, 1410, -1, -1, 2));  //20-23.5h
        return list;
    }
        */

    /*
    private static List<Position> createPositions() {
        List<Position> list = new ArrayList<>();
        
        list.add(new Position(3, "ホール", 1, 2, 1));
        list.add(new Position(2, "キッチン", 2, 3, 0));
        return list;
    }
    */
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
            "1",
            true,
            3000, 
            25000,
            prefs,
            Set.of(3, 2),
            Set.of(),
            option
        );
    }

    private static Worker createWorker2(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());               // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(1, 2, 3, 4));     // 火：6-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(1, 2, 3, 4));     // 水：6-20
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());               // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(1, 2, 3, 4));     // 金：6-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(1, 2, 3, 4));    // 土：6-20
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(1, 2, 3, 4));    // 日：6-20

        return new Worker(
            "2",
            true,
            5000, 
            30000,
            prefs,
            Set.of(3, 2),
            Set.of(),
            option
        );
    }

    private static Worker createWorker3(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(1, 2, 3));        // 月：6-18
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(1, 2, 3));        // 火：6-18
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(1, 2, 3));        // 水：6-18
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(1, 2, 3));        // 木：6-18
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(1, 2, 3));        // 金：6-18
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());              // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());              // 日：休暇

        return new Worker(
            "3",
            true,
            1800, 
            20000,
            prefs,
            Set.of(3),
            Set.of(),
            option
        );
    }

    private static Worker createWorker4(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of());                  // 月：休暇
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(5));             // 火：20-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(5));             // 水：20-23.5
        prefs.put(LocalDate.of(2026, 1, 8), Set.of());                  // 木：休暇
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(5));             // 金：20-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(3, 4, 5));          // 日：14-23.5

        return new Worker(
            "4",
            false,
            300, 
            3000,
            prefs,
            Set.of(2),
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
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(3, 4, 5));          // 土：14-23.5
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(2, 3, 4));          // 日：9-20

        return new Worker(
            "5",
            false,
            500, 
            7000,
            prefs,
            Set.of(3, 2),
            Set.of(0),
            option
        );
    }

    private static Worker createWorker6(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(3, 4));              // 月：14-20
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(3, 4));              // 火：14-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(3, 4));              // 水：14-20
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(3, 4));              // 木：14-20
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(3, 4));              // 金：14-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                 // 日：休暇

        return new Worker(
            "6",
            false,
            800,
            12000,
            prefs,
            Set.of(2), 
            Set.of(),
            option
        );
    }

    private static Worker createWorker7(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(2, 3));              // 月：9-18
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(2, 3));              // 火：9-18
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(2, 3));              // 水：9-18
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(2, 3));              // 木：9-18
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(2, 3));              // 金：9-18
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                 // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                 // 日：休暇

        return new Worker(
            "7",
            false,
            1500,
            18000,
            prefs,
            Set.of(3, 2),       // 両対応
            Set.of(0),     // レジ優先（仮仕様）
            option
        );
    }

    private static Worker createWorker8(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(4, 5));              // 月：18-23.5
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(4, 5));              // 火：18-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                  // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(4, 5));              // 木：18-23.5
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(4, 5));              // 金：18-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(1, 2, 3));          // 土：6-17
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(2, 3, 4, 5));       // 日：9-23.5

        return new Worker(
            "8",
            true,
            2400,
            27000,
            prefs,
            Set.of(3),  
            Set.of(1),
            option
        );
    }

    private static Worker createWorker9(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(5));                  // 月：20-23.5
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(4, 5));                  // 火：18-23.5
        prefs.put(LocalDate.of(2026, 1, 7), Set.of());                      // 水：休暇
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(5));                  // 木：20-23.5
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(4, 5));                  // 金：18-23.5
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                     // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(3, 4, 5));              // 日：14-23.5

        return new Worker(
            "9",
            false,
            120,        // ほぼ未稼働
            5000,    // 上限高
            prefs,
            Set.of(3, 2),
            Set.of(1),
            option
        );
    }

    private static Worker createWorker10(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(1));                  // 月：6-9
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(2));                  // 火：9-14
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(1));                  // 水：6-9
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(2));                  // 木：9-14
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(1));                  // 金：6-9
        prefs.put(LocalDate.of(2026, 1, 10), Set.of());                     // 土：休暇
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                     // 日：休暇

        return new Worker(
            "10",
            true,
            400, 
            24000,
            prefs,
            Set.of(2),
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
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(2, 3, 4));          // 土：9-20
        prefs.put(LocalDate.of(2026, 1, 11), Set.of(2, 3, 4));          // 日：9-20

        return new Worker(
            "11",
            false,
            500, 
            7000,
            prefs,
            Set.of(3, 2),
            Set.of(0, 1),
            option
        );
    }

    private static Worker createWorker12(Option option) {
        Map<LocalDate, Set<Integer>> prefs = new HashMap<>();
        prefs.put(LocalDate.of(2026, 1, 5), Set.of(2, 3, 4));                   // 月：9-20
        prefs.put(LocalDate.of(2026, 1, 6), Set.of(4));                     // 火：18-20
        prefs.put(LocalDate.of(2026, 1, 7), Set.of(2, 3, 4, 5));                // 水：9-23.5
        prefs.put(LocalDate.of(2026, 1, 8), Set.of(1, 2, 3, 4));                // 木：6-20
        prefs.put(LocalDate.of(2026, 1, 9), Set.of(2, 3, 4));                   // 金：9-20
        prefs.put(LocalDate.of(2026, 1, 10), Set.of(4, 5));                     // 土：18-23.5
        prefs.put(LocalDate.of(2026, 1, 11), Set.of());                         // 日：休暇

        return new Worker(
            "12",
            true,
            1500, 
            15000,
            prefs,
            Set.of(2),
            Set.of(),
            option
        );
    }
}
