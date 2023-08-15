package com.temah.lam.service;

import com.temah.common.web.BaseService;
import com.temah.lam.mapper.ScheduleSettingMapper;
import com.temah.lam.model.ScheduleSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleSettingService extends BaseService<ScheduleSetting, Integer> {

    private ScheduleSettingMapper scheduleSettingMapper;

    public ScheduleSettingService(ScheduleSettingMapper mapper) {
        super(mapper);
        scheduleSettingMapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ScheduleSetting> selectByJobStatus(Integer jobStatus) {
        return scheduleSettingMapper.selectByJobStatus(jobStatus);
    }
}
