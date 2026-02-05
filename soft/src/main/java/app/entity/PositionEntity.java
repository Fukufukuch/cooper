package app.entity;

public class PositionEntity {
    
    private final int id;
    private final String name;
    private final int minWorkers;
    private final int maxWorkers;
    private final int requireAuthorityWorkers;
    private final boolean active;

    public PositionEntity (
        int id, String name, int minWorkers, int maxWorkers,
        int requireAuthorityWorkers, boolean active
    ) {
        this.id = id;
        this.name = name;
        this.minWorkers = minWorkers;
        this.maxWorkers = maxWorkers;
        this.requireAuthorityWorkers = requireAuthorityWorkers;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }
}
