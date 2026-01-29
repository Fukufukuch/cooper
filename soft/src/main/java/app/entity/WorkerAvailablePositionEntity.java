package app.entity;

public class WorkerAvailablePositionEntity {
    private final String workerId;
    private final int positionId;

    public WorkerAvailablePositionEntity(
        String workerId, int positionId
    ) {
        this.workerId = workerId;
        this.positionId = positionId;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public int getPositionId() {
        return this.positionId;
    }
}
