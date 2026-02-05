package app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.repository.*;
import app.generate.ShiftGenerator;
import app.domain.*;

public class ShiftService {
    private final TimeSlotLoader timeSlotLoader;
    private final PositionLoader positionLoader;
    private final WorkerLoader workerLoader;
    private final OptionLoader optionLoader;

    public ShiftService() {
        this.timeSlotLoader = new TimeSlotLoader(new TimeSlotRepository());
        this.positionLoader = new PositionLoader(new PositionRepository());
        this.workerLoader = new WorkerLoader(
            new WorkerRepository(),
            new WorkerAvailablePositionRepository(),
            new WorkerNonconformTagRepository(),
            new WorkerShiftRequestRepository()
        );
        this.optionLoader = new OptionLoader(new OptionRepository());
    }

    private java.util.List<String> warnings = new java.util.ArrayList<>();

    public java.util.List<String> getWarnings() {
        return java.util.Collections.unmodifiableList(warnings);
    }
    private java.util.List<app.generate.shortageSlot> shortageSlots = new java.util.ArrayList<>();
    private java.util.List<app.generate.WarningSlot> warningSlots = new java.util.ArrayList<>();
    private java.util.List<java.util.Map<String, Object>> shortageSummary = new java.util.ArrayList<>();
    private java.util.List<java.util.Map<String, Object>> warningSummary = new java.util.ArrayList<>();

    public java.util.List<app.generate.shortageSlot> getShortageSlots() {
        return java.util.Collections.unmodifiableList(shortageSlots);
    }

    public java.util.List<app.generate.WarningSlot> getWarningSlots() {
        return java.util.Collections.unmodifiableList(warningSlots);
    }

    public java.util.List<java.util.Map<String, Object>> getShortageSummary() {
        return java.util.Collections.unmodifiableList(shortageSummary);
    }

    public java.util.List<java.util.Map<String, Object>> getWarningSummary() {
        return java.util.Collections.unmodifiableList(warningSummary);
    }

    public Map<LocalDate, Map<TimeSlot, Map<Position, List<String>>>> generateShift() {
        Option option = optionLoader.load();
        warnings.clear();

                List<TimeSlot> timeSlots = timeSlotLoader.loadAll();
        List<Position> positions = positionLoader.loadAll();
        List<Worker> workers = workerLoader.loadAll(option);
        ShiftRepository shiftRepo = new ShiftRepository();
        WorkerRepository workerRepo = new WorkerRepository();

        LocalDate startDate = option.getFirstDate();
        LocalDate endDate = startDate.plusDays(option.getGenerateDays() - 1);
        // ここで、月が替わった際の不整合に備えて、生成対象の開始日の月について
        // シフトテーブルからワーカー毎の合計を再集計し、worker.monthly_work_minutes を上書きします。
        try {
            int year = startDate.getYear();
            int month = startDate.getMonthValue();
            java.util.Map<String, Integer> monthMap = shiftRepo.sumMinutesByWorkerForMonth(year, month);

            // 全ワーカーについて存在しないものは0に設定するため、loader のワーカー一覧を使う
            for (Worker w : workers) {
                String wid = w.getId();
                int m = monthMap.getOrDefault(wid, 0);
                try {
                    workerRepo.setMonthlyMinutes(wid, m);
                } catch (Exception we) {
                    String msg = "failed to set monthly minutes for worker=" + wid + ": " + we.getMessage();
                    warnings.add(msg);
                    System.err.println(msg);
                }
            }
        } catch (Exception ex) {
            String msg = "failed to recompute monthly minutes for start month: " + ex.getMessage();
            warnings.add(msg);
            System.err.println(msg);
        }
        
        // 既存シフトを削除する前に、その期間に割り当てられていた分をワーカーの月労働時間から差し引く
        try {
            java.util.Map<String, Integer> oldMinutes = shiftRepo.sumMinutesByWorkerBetween(startDate, endDate);
            for (java.util.Map.Entry<String, Integer> e : oldMinutes.entrySet()) {
                String wid = e.getKey();
                int minutes = e.getValue();
                if (minutes != 0) {
                    try {
                        // 差分として減算（addMonthlyMinutes は負数で減算可能）
                        workerRepo.addMonthlyMinutes(wid, -minutes);
                    } catch (Exception we) {
                        String msg = "failed to subtract old minutes for worker=" + wid + ": " + we.getMessage();
                        warnings.add(msg);
                        System.err.println(msg);
                    }
                }
            }
        } catch (Exception ex) {
            // 集計に失敗しても削除は試みる
            String msg = "failed to sum old shift minutes: " + ex.getMessage();
            warnings.add(msg);
            System.err.println(msg);
        }

        shiftRepo.deleteBetween(startDate, endDate);

        ShiftGenerator generator = new ShiftGenerator(timeSlots, positions, workers, option);

        Map<LocalDate, Map<TimeSlot, Map<Position, List<String>>>> shift = generator.generate();
        // generator が保持する不足/警告リストを保存
        this.shortageSlots = new java.util.ArrayList<>(generator.getShortageSlots());
        this.warningSlots = new java.util.ArrayList<>(generator.getWarningSlots());

        // 簡易サマリ（日付＋時間帯で集約）を作成
        java.util.Map<String, Integer> cnt = new java.util.HashMap<>();
        for (app.generate.shortageSlot s : this.shortageSlots) {
            String timeName = s.getTimeSlot() == null ? "—" : s.getTimeSlot().getName();
            String key = s.getDate().toString() + "|" + timeName;
            cnt.put(key, cnt.getOrDefault(key, 0) + 1);
        }
        this.shortageSummary.clear();
        for (java.util.Map.Entry<String, Integer> e : cnt.entrySet()) {
            String[] parts = e.getKey().split("\\|", 2);
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("date", java.time.LocalDate.parse(parts[0]));
            m.put("timeSlot", parts.length > 1 ? parts[1] : "—");
            m.put("count", e.getValue());
            this.shortageSummary.add(m);
        }
        // ソート: 日付順、かつ timeSlots の定義順でソートする
        java.util.Map<String, Integer> slotOrder = new java.util.HashMap<>();
        int idx = 0;
        for (TimeSlot ts : timeSlots) {
            slotOrder.put(ts.getName(), idx++);
        }
        this.shortageSummary.sort((a, b) -> {
            java.time.LocalDate da = (java.time.LocalDate)a.get("date");
            java.time.LocalDate db = (java.time.LocalDate)b.get("date");
            int c = da.compareTo(db);
            if (c != 0) return c;
            String ta = (String)a.get("timeSlot");
            String tb = (String)b.get("timeSlot");
            Integer ia = slotOrder.getOrDefault(ta, Integer.MAX_VALUE);
            Integer ib = slotOrder.getOrDefault(tb, Integer.MAX_VALUE);
            c = ia.compareTo(ib);
            if (c != 0) return c;
            return ta.compareTo(tb);
        });

        // 警告サマリ（日付＋時間帯で集約）を作成
        java.util.Map<String, Integer> warnCnt = new java.util.HashMap<>();
        for (app.generate.WarningSlot ws : this.warningSlots) {
            String timeName = ws.getTimeSlot() == null ? "—" : ws.getTimeSlot().getName();
            String key = ws.getDate().toString() + "|" + timeName;
            warnCnt.put(key, warnCnt.getOrDefault(key, 0) + 1);
        }
        this.warningSummary.clear();
        for (java.util.Map.Entry<String, Integer> e : warnCnt.entrySet()) {
            String[] parts = e.getKey().split("\\|", 2);
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("date", java.time.LocalDate.parse(parts[0]));
            m.put("timeSlot", parts.length > 1 ? parts[1] : "—");
            m.put("count", e.getValue());
            this.warningSummary.add(m);
        }
        // ソート: 日付順、かつ timeSlots の定義順でソートする
        this.warningSummary.sort((a, b) -> {
            java.time.LocalDate da = (java.time.LocalDate)a.get("date");
            java.time.LocalDate db = (java.time.LocalDate)b.get("date");
            int c = da.compareTo(db);
            if (c != 0) return c;
            String ta = (String)a.get("timeSlot");
            String tb = (String)b.get("timeSlot");
            Integer ia = slotOrder.getOrDefault(ta, Integer.MAX_VALUE);
            Integer ib = slotOrder.getOrDefault(tb, Integer.MAX_VALUE);
            c = ia.compareTo(ib);
            if (c != 0) return c;
            return ta.compareTo(tb);
        });

        for (var dateEntry : shift.entrySet()) {
            LocalDate date = dateEntry.getKey();

            for (var slotEntry : dateEntry.getValue().entrySet()) {
                TimeSlot slot = slotEntry.getKey();

                for (var posEntry : slotEntry.getValue().entrySet()) {
                    Position pos = posEntry.getKey();

                    for (String workerId : posEntry.getValue()) {
                        // validate before inserting to avoid DB errors
                        if (workerId == null || workerId.trim().isEmpty()) {
                            String msg = "Skipping shift insert: empty workerId for date=" + date + " pos=" + pos.getName();
                            warnings.add(msg);
                            System.err.println(msg);
                            continue;
                        }
                        int positionId = pos.getId();
                        if (positionId <= 0) {
                            String msg = "Skipping shift insert: invalid position id=" + positionId + " for pos=" + pos.getName();
                            warnings.add(msg);
                            System.err.println(msg);
                            continue;
                        }
                        int sm = slot.getStartMinute(), em = slot.getEndMinute();
                        if (sm < 0 || sm >= 24*60 || em < 0 || em >= 24*60) {
                            String msg = "Skipping shift insert: invalid minutes start=" + sm + " end=" + em + " for slot=" + slot.getName();
                            warnings.add(msg);
                            System.err.println(msg);
                            continue;
                        }

                        try {
                            int shiftId = shiftRepo.insert(
                                date,
                                workerId,
                                positionId,
                                sm,
                                em,
                                slot.getName(),
                                warnings
                            );
                            if (shiftId > 0) {
                                try {
                                    workerRepo.addMonthlyMinutes(workerId, slot.getWorkMinutes());
                                } catch (Exception we) {
                                    String msg = "failed to update monthly minutes for worker=" + workerId + ": " + we.getMessage();
                                    warnings.add(msg);
                                    System.err.println(msg);
                                }
                            }
                        } catch (RuntimeException ex) {
                            String msg = "shift insert failed for date=" + date + " worker=" + workerId + " pos=" + positionId + ": " + ex.getMessage();
                            warnings.add(msg);
                            System.err.println(msg);
                        }
                    }
                }
            }
        }
        // 生成後に全期間の累計を再計算して worker.total_work_minutes を上書きします。
        try {
            java.util.Map<String, Integer> totalMap = shiftRepo.sumMinutesByWorkerAll();
            for (Worker w : workers) {
                String wid = w.getId();
                int t = totalMap.getOrDefault(wid, 0);
                try {
                    workerRepo.setTotalMinutes(wid, t);
                } catch (Exception we) {
                    String msg = "failed to set total minutes for worker=" + wid + ": " + we.getMessage();
                    warnings.add(msg);
                    System.err.println(msg);
                }
            }
        } catch (Exception ex) {
            String msg = "failed to recompute total minutes: " + ex.getMessage();
            warnings.add(msg);
            System.err.println(msg);
        }

        return shift;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlotLoader.loadAll();
    }

    public List<Position> getPositions() {
        return positionLoader.loadAll();
    }
}
