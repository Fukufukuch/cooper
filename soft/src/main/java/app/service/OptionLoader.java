package app.service;

import app.domain.Option;
import app.entity.OptionEntity;
import app.repository.OptionRepository;

import java.util.*;

public class OptionLoader {

    private final OptionRepository repository;
    
    public OptionLoader(OptionRepository repository) {
        this.repository = repository;
    }

    public Option load() {
        try {
            System.out.println("DBからoption取得開始");
            OptionEntity entity = repository.find();
            System.out.println("DB取得成功: " + entity);
            return Option.fromEntity(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void save(Option option) {
        OptionEntity entity = new OptionEntity(
            1,
            option.getMaxWorktimeofMonth(),
            option.getMaxWorktimeofDay(),
            option.getNewcomerThresholdMinutes(),
            option.getRequiredSeniorWorkers(),
            option.getGenerateDays(),
            option.getFirstDate()
        );

        repository.update(entity);
    }
}