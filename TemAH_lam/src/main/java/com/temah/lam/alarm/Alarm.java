package com.temah.lam.alarm;

import org.springframework.format.datetime.joda.LocalDateParser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Alarm {
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

        public Alarm build() {
            ZonedDateTime t = eventTime != null ? ZonedDateTime.ofInstant(
                    eventTime.toInstant(), ZoneId.systemDefault()) : ZonedDateTime.now();

            Alarm alarm = new Alarm();
            alarm.eventTime = DateTimeFormatter.ISO_LOCAL_DATE.format(t);
            alarm.managedObject = managedObject != null ? managedObject : "";
            alarm.alarmType = alarmType != null ? alarmType : "EquipmentAlarm";
            alarm.probableCause = probableCause != null ? probableCause : "UnKnow";
            alarm.perceivedSeverity = perceivedSeverity != null ? perceivedSeverity : "Critical";
            alarm.specificProblem = specificProblem != null ? specificProblem : "";
            alarm.additionalText = additionalText != null ? additionalText : "Default additionalText";
            return alarm;
        }
    }
}
