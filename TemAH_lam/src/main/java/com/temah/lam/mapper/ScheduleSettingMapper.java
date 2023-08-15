package com.temah.lam.mapper;

import com.temah.common.web.BaseMapper;
import com.temah.lam.model.ScheduleSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleSettingMapper extends BaseMapper<ScheduleSetting, Integer> {

    /**
     * 根据jobStatus查询，jobStatus=null则返回全部
     */
    List<ScheduleSetting> selectByJobStatus(Integer jobStatus);
}