package com.temah.ahfm.repository;

import com.temah.ahfm.entity.Alarm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlarmRepository extends MongoRepository<Alarm, String> {
}
