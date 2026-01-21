package jp.ac.kochi.tech.soft.model;

import java.time.LocalDate;

public class Shift {
    private LocalDate day;
    private String timetable;

    public Shift(LocalDate day, String timetable) {
        this.day = day;
        this.timetable = timetable;
    }

    public LocalDate getDay() {
        return day;
    }

    public String getTimetable() {
        return timetable;
    }
}

