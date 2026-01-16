public class Position {
    private final int id;
    private final String name;
    private final int minWorkers;
    private final int maxWorkers;
    private final int requireAuthorityWorkers;

    public Position(int id, String name, int minWorkers, int maxWorkers, int requireAuthorityWorkers) {
        this.id = id;                               //ID番号
        this.name = name;                           //スロット名
        this.minWorkers = minWorkers;               //最少人数
        this.maxWorkers = maxWorkers;               //最大人数
        this.requireAuthorityWorkers = requireAuthorityWorkers; //責任者の人数
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

    public int getRequireAuthorityWorkers() {
        return requireAuthorityWorkers;
    }
}
