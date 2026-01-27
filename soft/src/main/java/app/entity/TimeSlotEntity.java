package app.entity;

public class TimeSlotEntity {

    private final int id;
    private final String name;
    private final int startMinute;
    private final int endMinute;
    private final int minExtraWorkers;
    private final int maxExtraWorkers;
    private final int requireAuthorityWorkers;

    public TimeSlotEntity(
        int id, String name, int startMinute, int endMinute, 
        int minExtraWorkers, int maxExtraWorkers, int requireAuthorityWorkers
    ) {
        this.id = id;
        this.name = name;
        this.startMinute = startMinute;
        this.endMinute = endMinute;
        this.minExtraWorkers = minExtraWorkers;
        this.maxExtraWorkers = maxExtraWorkers;
        this.requireAuthorityWorkers = requireAuthorityWorkers;
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

    public int getMinExtraWorkers() {
        return minExtraWorkers;
    }

    public int getMaxExtraWorkers() {
        return maxExtraWorkers;
    }

    public int getRequireAuthorityWorkers() {
        return requireAuthorityWorkers;
    }
}
