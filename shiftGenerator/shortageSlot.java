import java.time.LocalDate;

public class shortageSlot {
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Position position;
    private final int required;                 // 最低人数
    private final int assigned;                 // 実際に入った人数
    private final ShortageType shortageType;    // 欠員タイプ

    public shortageSlot(LocalDate date, TimeSlot timeSlot, Position position, int required, int assigned,ShortageType shortageType) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.position = position;
        this.required = required;
        this.assigned = assigned;
        this.shortageType = shortageType;
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

    public ShortageType getShortageType() {
        return shortageType;
    }
}