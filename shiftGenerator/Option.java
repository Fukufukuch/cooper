public class Option {
    private final int maxWorktimeofMonth; // 月労働時間上限（分）
    private final int maxWorktimeofDay; // 日労働時間上限（分）
    private final int requiredAuthorityWorkers; // 必要権限者数

    public Option(int maxWorktimeofMonth, int maxWorktimeofDay, int requiredAuthorityWorkers) {
        this.maxWorktimeofMonth = maxWorktimeofMonth;
        this.maxWorktimeofDay = maxWorktimeofDay;
        this.requiredAuthorityWorkers = requiredAuthorityWorkers;
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
}