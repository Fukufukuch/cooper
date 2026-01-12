public class Position {
    private final int id;
    private final String name;
    private final int minWorkers;
    private final int maxWorkers;
    private final boolean requiresAuthority;

    public Position(int id, String name, int minWorkers, int maxWorkers, boolean requiresAuthority) {
        this.id = id;                               //ID番号
        this.name = name;                           //スロット名
        this.minWorkers = minWorkers;               //最少人数
        this.maxWorkers = maxWorkers;               //最大人数
        this.requiresAuthority = requiresAuthority; //責任者の有無
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinWorkers() {
        return minWorkers;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public boolean isRequiresAuthority() {
        return requiresAuthority;
    }
}
