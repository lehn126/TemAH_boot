package com.temah.ahfm.service;

import com.temah.ahfm.common.web.BaseMapper;
import com.temah.ahfm.common.web.BaseService;
import com.temah.ahfm.model.Alarm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmService extends BaseService<Alarm, Integer> {

    @Autowired
    public AlarmService(BaseMapper<Alarm, Integer> mapper) {
        super(mapper);
    }
}
