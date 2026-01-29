package app.service;

import app.domain.TimeSlot;
import app.entity.TimeSlotEntity;
import app.repository.TimeSlotRepository;

import java.util.*;

public class TimeSlotLoader {

    private final TimeSlotRepository repository;
    
    public TimeSlotLoader(TimeSlotRepository repository) {
        this.repository = repository;
    }

    public List<TimeSlot> loadAll() {
        return repository.findAll().stream().map(TimeSlot::fromEntity).toList();
    }
}
