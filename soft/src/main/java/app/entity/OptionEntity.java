package app.entity;

import java.time.LocalDate;

public class OptionEntity {
    
    private final int id;
    private final int maxWorktimeofMonth; // 月労働時間上限（分）
    private final int maxWorktimeofDay; // 日労働時間上限（分）
    private final int newcomerThresholdMinutes; // 新人判定時間（分）
    private final int requiredSeniorWorkers; // 必要先輩数
    private final int generateDays; // 生成日数
    private final LocalDate firstdate; // 生成初日の日付

    public OptionEntity(int id, int maxWorktimeofMonth, int maxWorktimeofDay, int newcomerThresholdMinutes, 
            int requiredSeniorWorkers, int generateDays, LocalDate firstdate) {
        this.id = id;
        this.maxWorktimeofMonth = maxWorktimeofMonth;
        this.newcomerThresholdMinutes = newcomerThresholdMinutes;
        this.maxWorktimeofDay = maxWorktimeofDay;
        this.requiredSeniorWorkers = requiredSeniorWorkers;
        this.generateDays = generateDays;
        this.firstdate = firstdate;
    }

    public int getId() {
        return this.id;
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

    public LocalDate getFirstdate() {
        return firstdate;
    }
}
