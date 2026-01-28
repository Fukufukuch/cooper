package app.domain;

import java.time.LocalDate;

import app.entity.OptionEntity;

public class Option {
    private final int maxWorktimeofMonth; // 月労働時間上限（分）
    private final int maxWorktimeofDay; // 日労働時間上限（分）
    private final int newcomerThresholdMinutes; // 新人判定時間（分）
    private final int requiredSeniorWorkers; // 必要先輩数
    private final int generateDays; // 生成日数
    private final LocalDate firstDate; // 生成初日の日付

    public Option(int maxWorktimeofMonth, int maxWorktimeofDay, int newcomerThresholdMinutes, int requiredSeniorWorkers, int generateDays, LocalDate firstDate) {
        this.maxWorktimeofMonth = maxWorktimeofMonth;
        this.maxWorktimeofDay = maxWorktimeofDay;
        this.newcomerThresholdMinutes = newcomerThresholdMinutes;
        this.requiredSeniorWorkers = requiredSeniorWorkers;
        this.generateDays = generateDays;
        this.firstDate = firstDate;
    }

    public static Option fromEntity(OptionEntity e) {
        return new Option(
            e.getMaxWorktimeofMonth(),
            e.getMaxWorktimeofDay(),
            e.getNewcomerThresholdMinutes(),
            e.getRequiredSeniorWorkers(),
            e.getGenerateDays(),
            e.getFirstdate()
        );
    }

    public int getMaxWorktimeofMonth() {
        return maxWorktimeofMonth;
    }

    public int getMaxWorktimeofDay() {
        return maxWorktimeofDay;
    }

    public int getNewcomerThresholdMinutes() {
        return newcomerThresholdMinutes;
    }

    public int getRequiredSeniorWorkers() {
        return requiredSeniorWorkers;
    }

    public int getGenerateDays() {
        return generateDays;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }
}