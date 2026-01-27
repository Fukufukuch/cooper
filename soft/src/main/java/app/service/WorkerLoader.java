package app.service;

import app.domain.Worker;
import app.domain.Option;
import app.entity.*;
import app.repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class WorkerLoader {

    private final WorkerRepository workerRepo;
    private final WorkerAvailablePositionRepository positionRepo;
    private final WorkerNonconformTagRepository tagRepo;
    private final WorkerShiftRequestRepository shiftReqRepo;
    
    public WorkerLoader(
            WorkerRepository workerRepo,
            WorkerAvailablePositionRepository positionRepo,
            WorkerNonconformTagRepository tagRepo,
            WorkerShiftRequestRepository shiftReqRepo) {
        this.workerRepo = workerRepo;
        this.positionRepo = positionRepo;
        this.tagRepo = tagRepo;
        this.shiftReqRepo = shiftReqRepo;
    }

    public List<Worker> loadAll(Option option) {
        List<WorkerEntity> workers = workerRepo.findAll();
        List<WorkerAvailablePositionEntity> positions = positionRepo.findAll();
        List<WorkerNonconformTagEntity> tags = tagRepo.findAll();
        List<WorkerShiftRequestEntity> requests = shiftReqRepo.findAll();

        Map<String, Set<Integer>> positionMap = positions.stream()
                .collect(Collectors.groupingBy(
                        WorkerAvailablePositionEntity::getWorkerId,
                        Collectors.mapping(WorkerAvailablePositionEntity::getPositionId, Collectors.toSet())
                ));
        
        Map<String, Set<Integer>> tagMap = tags.stream()
                .collect(Collectors.groupingBy(
                        WorkerNonconformTagEntity::getWorkerId,
                        Collectors.mapping(WorkerNonconformTagEntity::getNonconformTag, Collectors.toSet())
                ));

        Map<String, Map<LocalDate, Set<Integer>>> shiftReqMap = new HashMap<>();
        for (WorkerShiftRequestEntity e : requests) {
            shiftReqMap
                .computeIfAbsent(e.getWorkerId(), k -> new HashMap<>())
                .computeIfAbsent(e.getDate(), k -> new HashSet<>())
                .add(e.getTimeSlotId());
        }

        // ③ Domain Worker に変換
        List<Worker> result = new ArrayList<>();

        for (WorkerEntity e : workers) {
            String id = e.getWorkerId();

            result.add(Worker.fromEntities(
                    e,
                    shiftReqMap.getOrDefault(id, Map.of()),
                    positionMap.getOrDefault(id, Set.of()),
                    tagMap.getOrDefault(id, Set.of()),
                    option
            ));
        }

        return result;
    }
}