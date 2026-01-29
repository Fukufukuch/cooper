package app.domain;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

public class ShiftCondition {

    private int maxWorktimeOfMonth;
    private int maxWorktimeOfDay;

    public int getMaxWorktimeOfMonth() {
        return maxWorktimeOfMonth;
    }

    public void setMaxWorktimeOfMonth(int maxWorktimeOfMonth) {
        this.maxWorktimeOfMonth = maxWorktimeOfMonth;
    }

    public int getMaxWorktimeOfDay() {
        return maxWorktimeOfDay;
    }

    public void setMaxWorktimeOfDay(int maxWorktimeOfDay) {
        this.maxWorktimeOfDay = maxWorktimeOfDay;
    }
}