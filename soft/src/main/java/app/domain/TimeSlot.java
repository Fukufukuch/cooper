package app.domain;

import app.entity.TimeSlotEntity;

import java.util.Objects;

public class TimeSlot {
    private final int id;
    private final String name;
    private final int startMinute;
    private final int endMinute;
    private final int workMinutes;
    private final int minExtraWorkers;
    private final int maxExtraWorkers;
    private final int requireAuthorityWorkers;

    public TimeSlot(int id, String name, int startMinute, int endMinute, int minExtraWorkers, int maxExtraWorkers, int requireAuthorityWorkers) {
        this.id = id;
        this.name = name;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.workMinutes = endMinute - startMinute;
        this.requireAuthorityWorkers = requireAuthorityWorkers;
        this.minExtraWorkers = minExtraWorkers;
        this.maxExtraWorkers = maxExtraWorkers;
    }

    public static TimeSlot fromEntity(TimeSlotEntity e) {
        return new TimeSlot(
            e.getId(),
            e.getName(),
            e.getStartMinute(),
            e.getEndMinute(),
            e.getMinExtraWorkers(),
            e.getMaxExtraWorkers(),
            e.getRequireAuthorityWorkers()
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int getWorkMinutes() {
        return workMinutes;
    }

    public int getMinExtraWorkers() {
        return minExtraWorkers;
    }

    public int getMaxExtraWorkers() {
        return maxExtraWorkers;
    }

    public int getRequireAuthorityWorkers() {
        return requireAuthorityWorkers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(name, timeSlot.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
