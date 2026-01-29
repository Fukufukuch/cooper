package app.entity;

public class WorkerNonconformTagEntity {
    private String workerId;
    private int nonconformTag;

    public WorkerNonconformTagEntity(
        String workerId, int nonconformTag
    ) {
        this.workerId = workerId;
        this.nonconformTag = nonconformTag;
    }

    public String getWorkerId() {
        return this.workerId;
    }

    public int getNonconformTag() {
        return this.nonconformTag;
    }
}
