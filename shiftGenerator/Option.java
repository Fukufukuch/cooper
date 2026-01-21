public class Option {
    private final int maxWorktimeofMonth; // 月労働時間上限（分）
    private final int maxWorktimeofDay; // 日労働時間上限（分）
    private final int requiredAuthorityWorkers; // 必要権限者数
    private final int requiredSeniorWorkers; // 必要先輩数

    public Option(int maxWorktimeofMonth, int maxWorktimeofDay, int requiredAuthorityWorkers, int requiredSeniorWorkers) {
        this.maxWorktimeofMonth = maxWorktimeofMonth;
        this.maxWorktimeofDay = maxWorktimeofDay;
        this.requiredAuthorityWorkers = requiredAuthorityWorkers;
        this.requiredSeniorWorkers = requiredSeniorWorkers;
    }

    public int getMaxWorktimeofMonth() {
        return maxWorktimeofMonth;
    }

    public int getMaxWorktimeofDay() {
        return maxWorktimeofDay;
    }

    public int getRequiredAuthorityWorkers() {
        return requiredAuthorityWorkers;
    }

    public int getRequiredSeniorWorkers() {
        return requiredSeniorWorkers;
    }
}