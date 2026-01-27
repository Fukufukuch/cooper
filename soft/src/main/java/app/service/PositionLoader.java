package app.service;

import app.domain.Position;
import app.entity.PositionEntity;
import app.repository.PositionRepository;

import java.util.*;

public class PositionLoader {

    private final PositionRepository repository;
    
    public PositionLoader(PositionRepository repository) {
        this.repository = repository;
    }

    public List<Position> loadAll() {
        return repository.findAll().stream().map(Position::fromEntity).toList();
    }
}