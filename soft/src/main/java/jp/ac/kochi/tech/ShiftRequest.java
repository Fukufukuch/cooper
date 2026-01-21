package jp.ac.kochi.tech;

public class ShiftRequest {

    private String date;
    private String startTime;
    private String endTime;
    private boolean allDay;
    private String userId;
    private String date;
    private String shift;

    public ShiftRequest() {}

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAllDay() {
        return allDay;
    }
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getUserId() { return userId; }
    public String getDate() { return date; }
    public String getShift() { return shift; }
}

