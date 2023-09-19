package com.temah.ahfm.service;

import com.temah.ahfm.entity.Alarm;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlarmService extends BaseSpringDataService<Alarm, String>{
    public AlarmService(MongoRepository<Alarm, String> repository) {
        super(repository);
    }

    @Transactional(readOnly = false)
    public int clearAlarm(String id) {
        Alarm alarm = repository.findById(id).orElse(null);
        int affectedRow = 1;
        if (alarm != null) {
            alarm.setClearFlag(1);
            // save operation will never be null in spring data
            repository.save(alarm);
        }
        return affectedRow;
    }
}
