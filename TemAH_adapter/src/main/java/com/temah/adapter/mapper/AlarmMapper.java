package com.temah.adapter.mapper;

import com.temah.adapter.dto.AlarmDto;
import com.temah.common.mapper.FieldMapper;
import com.temah.common.mapper.ObjectMapper;

import java.util.Map;
import java.util.Objects;

public class AlarmMapper {
    private final ObjectMapper<Map<String, Object>, AlarmDto> mapper = new ObjectMapper<>();

    public AlarmMapper() {
        this.mapper.init(
                new FieldMapper<Map<String, Object>, AlarmDto, String>("eventTime",
                        (Map<String, Object> source) -> {
                            return (String) source.get("eventTime");
                        },
                        (Map<String, Object> source) -> {
                            return source != null && source.containsKey("eventTime");
                        },
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setEventTime(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("managedObject",
                        (Map<String, Object> source) -> {
                            return (String) source.get("managedObject");
                        },
                        (Map<String, Object> source) -> {
                            return source != null && source.containsKey("managedObject");
                        },
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setManagedObject(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("alarmType",
                        (Map<String, Object> source) -> {
                            return (String) source.get("alarmType");
                        },
                        (Map<String, Object> source) -> {
                            return source != null && source.containsKey("alarmType");
                        },
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setAlarmType(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("probableCause",
                        (Map<String, Object> source) -> {
                            return (String) source.get("probableCause");
                        },
                        Objects::nonNull,
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setProbableCause(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("perceivedSeverity",
                        (Map<String, Object> source) -> {
                            return (String) source.get("perceivedSeverity");
                        },
                        (Map<String, Object> source) -> {
                            return source != null && source.containsKey("perceivedSeverity");
                        },
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setPerceivedSeverity(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("specificProblem",
                        (Map<String, Object> source) -> {
                            return (String) source.get("specificProblem");
                        },
                        Objects::nonNull,
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setSpecificProblem(fieldValue);
                        }
                ),
                new FieldMapper<Map<String, Object>, AlarmDto, String>("additionalText",
                        (Map<String, Object> source) -> {
                            return (String) source.get("additionalText");
                        },
                        Objects::nonNull,
                        (AlarmDto alarm, String fieldValue) -> {
                            alarm.setAdditionalText(fieldValue);
                        }
                )
        );
    }

    public AlarmDto mapTo(Map<String, Object> map) {
        AlarmDto alarm = new AlarmDto();
        return mapper.mapTo(map, alarm);
    }
}
