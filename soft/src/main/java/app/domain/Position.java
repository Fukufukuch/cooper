package app.domain;

import java.util.Objects;

import app.entity.PositionEntity;

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

    public static Position fromEntity(PositionEntity e) {
        return new Position(
            e.getId(),
            e.getName(),
            e.getMinWorkers(),
            e.getMaxWorkers(),
            e.getRequireAuthorityWorkers()
        );
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return Objects.equals(name, position.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
