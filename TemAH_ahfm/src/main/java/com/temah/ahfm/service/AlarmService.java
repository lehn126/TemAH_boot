package com.temah.ahfm.service;

import com.temah.ahfm.model.Alarm;
import com.temah.common.web.BaseMapper;
import com.temah.common.web.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlarmService extends BaseService<Alarm, Integer> {

    public AlarmService(BaseMapper<Alarm, Integer> mapper) {
        super(mapper);
    }

    @Transactional(readOnly = false)
    public int clearAlarm(Integer id) {
        Alarm alarm = mapper.selectByPrimaryKey(id);
        int affectedRow = 0;
        if (alarm != null) {
            alarm.setClearFlag(1);
            affectedRow = mapper.updateByPrimaryKey(alarm);
        }
        return affectedRow;
    }
}
