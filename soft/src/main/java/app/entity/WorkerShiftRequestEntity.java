package app.entity;

import java.time.LocalDate;

public class WorkerShiftRequestEntity {
    private final String workerId;
    private final LocalDate date;
    private final int timeSlotId;

    public WorkerShiftRequestEntity(
        String workerId, LocalDate date, int timeSlotId
    ) {
        this.workerId = workerId;
        this.date = date;
        this.timeSlotId = timeSlotId;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public int getTimeSlotId() {
        return this.timeSlotId;
    }
}
