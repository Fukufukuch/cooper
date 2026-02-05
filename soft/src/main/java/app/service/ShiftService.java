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

    public Map<LocalDate, Map<TimeSlot, Map<Position, List<String>>>> generateShift() {
        Option option = optionLoader.load();
        List<TimeSlot> timeSlots = timeSlotLoader.loadAll();
        List<Position> positions = positionLoader.loadAll();
        List<Worker> workers = workerLoader.loadAll(option);
        ShiftRepository shiftRepo = new ShiftRepository();

        LocalDate startDate = option.getFirstDate();
        LocalDate endDate = startDate.plusDays(option.getGenerateDays() - 1);

        shiftRepo.deleteBetween(startDate, endDate);

        ShiftGenerator generator = new ShiftGenerator(timeSlots, positions, workers, option);

        Map<LocalDate, Map<TimeSlot, Map<Position, List<String>>>> shift = generator.generate();

        for (var dateEntry : shift.entrySet()) {
            LocalDate date = dateEntry.getKey();

            for (var slotEntry : dateEntry.getValue().entrySet()) {
                TimeSlot slot = slotEntry.getKey();

                for (var posEntry : slotEntry.getValue().entrySet()) {
                    Position pos = posEntry.getKey();

                    for (String workerId : posEntry.getValue()) {
                        // validate before inserting to avoid DB errors
                        if (workerId == null || workerId.trim().isEmpty()) {
                            System.err.println("Skipping shift insert: empty workerId for date=" + date + " pos=" + pos.getName());
                            continue;
                        }
                        int positionId = pos.getId();
                        if (positionId <= 0) {
                            System.err.println("Skipping shift insert: invalid position id=" + positionId + " for pos=" + pos.getName());
                            continue;
                        }
                        int sm = slot.getStartMinute(), em = slot.getEndMinute();
                        if (sm < 0 || sm >= 24*60 || em < 0 || em >= 24*60) {
                            System.err.println("Skipping shift insert: invalid minutes start=" + sm + " end=" + em + " for slot=" + slot.getName());
                            continue;
                        }

                        try {
                            shiftRepo.insert(
                                date,
                                workerId,
                                positionId,
                                sm,
                                em,
                                slot.getName()
                            );
                        } catch (RuntimeException ex) {
                            // Log and continue to avoid full generation failure
                            System.err.println("shift insert failed for date=" + date + " worker=" + workerId + " pos=" + positionId + ": " + ex.getMessage());
                        }
                    }
                }
            }
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
