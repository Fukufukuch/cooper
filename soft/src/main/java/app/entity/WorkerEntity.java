package app.entity;

public class WorkerEntity {
    private final String workerId;
    private final int hasAuthority;
    private int monthlyWorkMinutes;
    private int totalWorkMinutes;

    public WorkerEntity(
        String workerId, int hasAuthority,
        int monthlyWorkMinutes, int totalWorkMinutes
    ) {
        this.workerId = workerId;
        this.hasAuthority = hasAuthority;
        this.monthlyWorkMinutes = monthlyWorkMinutes;
        this.totalWorkMinutes = totalWorkMinutes;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public int getHasAuthority() {
        return this.hasAuthority;
    }

    public int getMonthlyWorkMinutes() {
        return this.monthlyWorkMinutes;
    }

    public int getTotalWorkMinutes() {
        return this.totalWorkMinutes;
    }
}
