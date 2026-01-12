import java.time.LocalDate;

public class shortageSlot {
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Position position;
    private final int required; // 最低人数
    private final int assigned; // 実際に入った人数
    private final boolean isAuthorityAssigned; // 責任者が割り当てられたかどうか
    private final boolean isWorkerAssigned; // 労働者が割り当てられたかどうか
    private final boolean isSeniorAssigned; // 先輩が割り当てられたかどうか

    public shortageSlot(LocalDate date, TimeSlot timeSlot, Position position, int required, int assigned, boolean isAuthorityAssigned, boolean isWorkerAssigned, boolean isSeniorAssigned) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.position = position;
        this.required = required;
        this.assigned = assigned;
        this.isAuthorityAssigned = isAuthorityAssigned;
        this.isWorkerAssigned = isAuthorityAssigned;
        this.isSeniorAssigned = isSeniorAssigned;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public Position getPosition() {
        return position;
    }

    public int getRequired() {
        return required;
    }

    public int getAssigned() {
        return assigned;
    }

    public boolean isAuthorityAssigned() {
        return isAuthorityAssigned;
    }

    public boolean isWorkerAssigned() {
        return isWorkerAssigned;
    }
    
    public boolean isSeniorAssigned() {
        return isSeniorAssigned;
    }
}