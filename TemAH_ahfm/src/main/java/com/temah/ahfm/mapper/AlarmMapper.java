package com.temah.ahfm.mapper;

import com.temah.ahfm.model.Alarm;
import com.temah.common.web.BaseBLOBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper extends BaseBLOBMapper<Alarm, Integer> {

}