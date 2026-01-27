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
                        shiftRepo.insert(
                            date,
                            workerId,
                            pos.getId(),
                            slot.getStartMinute(),
                            slot.getEndMinute(),
                            slot.getName()
                        );
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
