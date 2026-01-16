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
}
