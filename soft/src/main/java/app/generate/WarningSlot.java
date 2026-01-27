package app.generate;

import java.time.LocalDate;
import java.util.*;

import app.domain.*;

public class WarningSlot {
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Position position;
    private final int nonconformTag;
    private final List<String> warningWorkers;  // 警告対象の従業員IDリスト
    private final WarningType warningType;       // 警告タイプ

    public WarningSlot(LocalDate date, TimeSlot timeSlot, Position position, int nonconformTag, List<String> warningWorkers, WarningType warningType) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.position = position;
        this.nonconformTag = nonconformTag;
        this.warningWorkers = warningWorkers;
        this.warningType = warningType;
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

    public int getNonconformTag() {
        return nonconformTag;
    }

    public List<String> getWarningWorkers() {
        return warningWorkers;
    }

    public WarningType getWarningType() {
        return warningType;
    }
}