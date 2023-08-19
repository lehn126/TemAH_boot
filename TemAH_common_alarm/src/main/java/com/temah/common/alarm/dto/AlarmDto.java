package com.temah.common.alarm.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 告警消息结构
 */
public class AlarmDto {
    private String eventTime;

    private String managedObject;

    private String alarmType;

    private String probableCause;

    private String perceivedSeverity;

    private String specificProblem;

    private String additionalText;

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(String managedObject) {
        this.managedObject = managedObject;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getProbableCause() {
        return probableCause;
    }

    public void setProbableCause(String probableCause) {
        this.probableCause = probableCause;
    }

    public String getPerceivedSeverity() {
        return perceivedSeverity;
    }

    public void setPerceivedSeverity(String perceivedSeverity) {
        this.perceivedSeverity = perceivedSeverity;
    }

    public String getSpecificProblem() {
        return specificProblem;
    }

    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public static AlarmBuilder builder() {
        return new AlarmBuilder();
    }

    public static class AlarmBuilder {
        private Date eventTime;

        private String managedObject;

        private String alarmType;

        private String probableCause;

        private String perceivedSeverity;

        private String specificProblem;

        private String additionalText;

        public AlarmBuilder setEventTime(Date eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public AlarmBuilder setManagedObject(String managedObject) {
            this.managedObject = managedObject;
            return this;
        }

        public AlarmBuilder setAlarmType(String alarmType) {
            this.alarmType = alarmType;
            return this;
        }

        public AlarmBuilder setProbableCause(String probableCause) {
            this.probableCause = probableCause;
            return this;
        }

        public AlarmBuilder setPerceivedSeverity(String perceivedSeverity) {
            this.perceivedSeverity = perceivedSeverity;
            return this;
        }

        public AlarmBuilder setSpecificProblem(String specificProblem) {
            this.specificProblem = specificProblem;
            return this;
        }

        public AlarmBuilder setAdditionalText(String additionalText) {
            this.additionalText = additionalText;
            return this;
        }

        public AlarmDto build() {
            ZonedDateTime t = eventTime != null ? ZonedDateTime.ofInstant(
                    eventTime.toInstant(), ZoneId.systemDefault()) : ZonedDateTime.now();

            AlarmDto alarmDto = new AlarmDto();
            alarmDto.eventTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t);
            alarmDto.managedObject = managedObject != null ? managedObject : "";
            alarmDto.alarmType = alarmType != null ? alarmType : "EquipmentAlarm";
            alarmDto.probableCause = probableCause != null ? probableCause : "UnKnow";
            alarmDto.perceivedSeverity = perceivedSeverity != null ? perceivedSeverity : "Critical";
            alarmDto.specificProblem = specificProblem != null ? specificProblem : "";
            alarmDto.additionalText = additionalText != null ? additionalText : "Default additionalText";
            return alarmDto;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " {" +
                "eventTime:" + eventTime + "," +
                "managedObject:" + managedObject + "," +
                "alarmType:" + alarmType + "," +
                "probableCause:" + probableCause + "," +
                "perceivedSeverity:" + perceivedSeverity + "," +
                "specificProblem:" + specificProblem + "," +
                "additionalText:" + additionalText +
                "}";
    }
}
