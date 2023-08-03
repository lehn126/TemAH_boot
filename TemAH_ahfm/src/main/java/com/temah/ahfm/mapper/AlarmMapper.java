package com.temah.ahfm.mapper;

import com.temah.ahfm.model.Alarm;
import com.temah.common.web.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper extends BaseMapper<Alarm, Integer> {

    int updateByPrimaryKeyWithBLOBs(Alarm row);

}