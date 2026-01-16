public class Option {
    private final int maxWorktimeofMonth; // 月労働時間上限（分）
    private final int maxWorktimeofDay; // 日労働時間上限（分）
    private final int newcomerThresholdMinutes; // 新人判定時間（分）
    private final int requiredSeniorWorkers; // 必要先輩数

    public Option(int maxWorktimeofMonth, int maxWorktimeofDay, int newcomerThresholdMinutes, int requiredSeniorWorkers) {
        this.maxWorktimeofMonth = maxWorktimeofMonth;
        this.newcomerThresholdMinutes = newcomerThresholdMinutes;
        this.maxWorktimeofDay = maxWorktimeofDay;
        this.requiredSeniorWorkers = requiredSeniorWorkers;
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
}