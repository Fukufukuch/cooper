package app.generate;

import java.util.List;

import app.domain.*;

public interface ShiftDataSource {

    //List<TimeSlot> getTimeSlots();
    //List<Position> getPositions();
    List<Worker> getWorkers();
    Option getOption();
}