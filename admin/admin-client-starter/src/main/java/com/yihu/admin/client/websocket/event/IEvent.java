package com.yihu.admin.client.websocket.event;

import java.util.Date;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
public class IEvent {

    /**
     * 整个請求的ID  在分布式的情况下,用来串起各個微服務的日誌
     *
     * @return
     */
    protected String traceId;

    /**
     * 模块ID  在分布式的情况下 代表這個事件是屬於哪個微服務
     *
     * @return
     */
    protected String spanId;
    /**
     * 模块ID  在分布式的情况下 代表這個事件是屬於哪個微服務
     *
     * @return
     */
    protected String spanName;

    /**
     * 事件名称
     *
     * @return
     */
    protected String eventName;

    /**
     * 时间发生的时间
     *
     * @return
     */
    protected Long eventStartTime;
    /**
     * 时间发生的时间
     *
     * @return
     */
    protected Long eventEndTime;

    /**
     * 执行时间
     */
    protected Long excuteTime;

    protected Integer success=0; //1成功 0失败
    protected Integer fail=0; //1成功 0失败

    public IEvent() {

    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSpanName() {
        return spanName;
    }

    public void setSpanName(String spanName) {
        this.spanName = spanName;
    }

    public Long getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(Long excuteTime) {
        this.excuteTime = excuteTime;
    }

    public Long getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Long eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public Long getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(Long eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }
}
