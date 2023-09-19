package com.temah.ahfm.entity;

import java.io.Serializable;

public class Alarm implements Serializable {

    // MongoDB在不特殊指认的情况下会把 id 默认对应到主键 "_id", 类型隐式转换为 ObjectId 或 String
    // @MongoId(targetType = FieldType.STRING)
    // @Field("_id")
    private String id;

    private String eventTime;

    private String managedObject;

    private String alarmType;

    private String probableCause;

    private String perceivedSeverity;

    private String specificProblem;

    private Integer clearFlag = 0;

    private Integer terminateState = 0;

    private String additionalText;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime == null ? null : eventTime.trim();
    }

    public String getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(String managedObject) {
        this.managedObject = managedObject == null ? null : managedObject.trim();
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType == null ? null : alarmType.trim();
    }

    public String getProbableCause() {
        return probableCause;
    }

    public void setProbableCause(String probableCause) {
        this.probableCause = probableCause == null ? null : probableCause.trim();
    }

    public String getPerceivedSeverity() {
        return perceivedSeverity;
    }

    public void setPerceivedSeverity(String perceivedSeverity) {
        this.perceivedSeverity = perceivedSeverity == null ? null : perceivedSeverity.trim();
    }

    public String getSpecificProblem() {
        return specificProblem;
    }

    public void setSpecificProblem(String specificProblem) {
        this.specificProblem = specificProblem == null ? null : specificProblem.trim();
    }

    public Integer getClearFlag() {
        return clearFlag;
    }

    public void setClearFlag(Integer clearFlag) {
        this.clearFlag = clearFlag;
    }

    public Integer getTerminateState() {
        return terminateState;
    }

    public void setTerminateState(Integer terminateState) {
        this.terminateState = terminateState;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText == null ? null : additionalText.trim();
    }
}