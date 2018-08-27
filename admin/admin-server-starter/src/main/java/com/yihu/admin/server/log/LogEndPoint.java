package com.yihu.admin.server.log;

import com.yihu.admin.server.properties.AdminServerProperties;
import com.yihu.admin.server.util.DateUtil;
import com.yihu.base.es.config.ElastricSearchHelper;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by chenweida on 2018/5/24 0024.1
 */
@RestController
@RequestMapping("admin_log")
public class LogEndPoint {
    @Autowired
    private ElastricSearchHelper elastricSearchHelper;
    @Autowired
    private AdminServerProperties adminServerProperties;

    @GetMapping("list")
    public List<LogModel> list(
            @RequestParam(value = "traceId", required = false) String traceId,  // GET POST DELETE PUT
            @RequestParam(value = "time", required = false) Integer time,  // GET POST DELETE PUT
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize

    ) {
        List<LogModel> logs = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT traceId, max(eventEndTime) eventEndTime ,min(eventStartTime) eventStartTime, max(excuteTime) excuteTime FROM " + adminServerProperties.getRealDefaultIndexName() + " where eventName='system_http_tracer'  ");
        StringBuffer whereSQL = new StringBuffer();
        if (!StringUtils.isEmpty(traceId)) {
            whereSQL.append(" and traceId='" + traceId + "'");
        }
        if (time != null && time > 0) {
            whereSQL.append(" and  excuteTime >" + time + "");
        }
        StringBuffer groupby = new StringBuffer(" group by traceId  order by eventStartTime desc limit " + (page * pageSize) + "," + pageSize);
        String finalSql = sql.toString() + whereSQL.toString() + groupby.toString();
        List<Map<String, Object>> traceidList = elastricSearchHelper.excuceSQL(finalSql);
        for (int i = 0; i < traceidList.size(); i++) {
            LogModel logTemp = new LogModel();
            Map<String, Object> value = traceidList.get(i);
            if (value.size() == 0) {
                continue;
            }
            logTemp.setTraceid(value.get("traceId").toString());
            Long startTime = ((Double) value.get("eventStartTime")).longValue();
            logTemp.setEventstarttime(startTime);
            logTemp.setEventstarttimestr(DateUtil.dateToStr(new Date(startTime), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS));
            Long endtime = ((Double) value.get("eventEndTime")).longValue();
            logTemp.setEventendtime(endtime);
            logTemp.setEventendtimestr(DateUtil.dateToStr(new Date(endtime), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS));
            logs.add(logTemp);
        }
        return logs;
    }

    @GetMapping("tracelist")
    public List<LogModel> tracelist(
            @RequestParam(value = "traceId", required = false) String traceId,
            @RequestParam(value = "spanname", required = false) String spanname,
            @RequestParam(value = "time", required = false) Integer time,  // GET POST DELETE PUT
            @RequestParam(value = "eventname", required = false) String eventname,
            @RequestParam(value = "method", required = false) String method,
            @RequestParam(value = "uri", required = false) String uri,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        if (!StringUtils.isEmpty(spanname)) {
            spanname = spanname.toLowerCase();
        }
        List<LogModel> logs = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT * FROM " + adminServerProperties.getRealDefaultIndexName());
        StringBuffer whereSQL = initWhere(traceId, spanname, eventname, time, uri, method);

        StringBuffer groupBY = new StringBuffer("order by eventStartTime "+sort);

        String finalSQL = sql.append(whereSQL).append(groupBY).toString();

        List<Map<String, Object>> traceidList = elastricSearchHelper.excuceSQL(finalSQL);
        for (int i = 0; i < traceidList.size(); i++) {
            LogModel logTemp = new LogModel();
            Map<String, Object> value = traceidList.get(i);


            logTemp.setTraceid(value.get("traceId").toString());
            logTemp.setSpanid(value.get("spanId").toString());
            logTemp.setSpanname(value.get("spanName").toString());
            logTemp.setEventname(value.get("eventName").toString());
            if (value.containsKey("eventStartTime") && value.get("eventStartTime") != null) {
                Long startTime = (Long) value.get("eventStartTime");
                logTemp.setEventstarttime(startTime);
                logTemp.setEventstarttimestr(DateUtil.dateToStr(new Date(startTime), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS));
            }
            if (value.containsKey("eventEndTime") && value.get("eventEndTime") != null) {
                Long endtime = (Long) value.get("eventEndTime");
                logTemp.setEventendtime(endtime);
                logTemp.setEventendtimestr(DateUtil.dateToStr(new Date(endtime), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS));
            }
            if (value.containsKey("method") && value.get("method") != null) {
                logTemp.setMethod(value.get("method").toString());
            }
            if (value.containsKey("sql") && value.get("sql") != null) {
                logTemp.setSql(value.get("sql").toString());
            }
            if (value.containsKey("ip") && value.get("ip") != null) {
                logTemp.setIp(value.get("ip").toString());
            }
            if (value.containsKey("uri") && value.get("uri") != null) {
                logTemp.setUri(value.get("uri").toString());
            }
            if (value.containsKey("excuteTime") && value.get("excuteTime") != null) {
                logTemp.setExcutetime((Integer) value.get("excuteTime"));
            }

            //设置header
            JSONObject headerJO = new JSONObject();
            JSONObject requestJO = new JSONObject();
            JSONObject responseJO = new JSONObject();
            for (Map.Entry<String, Object> one : value.entrySet()) {
                if (one.getKey().contains("header")) {
                    headerJO.put(one.getKey(), one.getValue());
                } else if (one.getKey().contains("requestParams")) {
                    requestJO.put(one.getKey(), one.getValue());
                } else if (one.getKey().contains("responseParams")) {
                    responseJO.put(one.getKey(), one.getValue());
                }
            }
            logTemp.setHeader(headerJO.toString());
            logTemp.setRequestparams(requestJO.toString());
            logTemp.setResponseparams(responseJO.toString());
            logs.add(logTemp);
        }
        return logs;
    }

    private StringBuffer initWhere(String traceId, String spanname, String eventname, Integer time, String uri, String method) {
        StringBuffer whereSQL = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(traceId)) {
            params.put("traceId", traceId);
        }
        if (!StringUtils.isEmpty(spanname)) {
            params.put("spanName", spanname);
        }
        if (!StringUtils.isEmpty(eventname)) {
            params.put("eventName", eventname);
        }
        if (!StringUtils.isEmpty(uri)) {
            params.put("uri", uri);
        }
        if (!StringUtils.isEmpty(method)) {
            params.put("method", method);
        }
        if (time != null && time > 0) {
            params.put("time", time);
        }
        String key = " where ";
        for (Map.Entry<String, Object> one : params.entrySet()) {
            if ("time".equals(one.getKey())) {
                whereSQL.append(key).append("excuteTime > '" + one.getValue() + "'");
            } else if ("uri".equals(one.getKey())) {
                whereSQL.append(key).append("uri like '%" + one.getValue() + "%'");
            } else {
                whereSQL.append(key).append(one.getKey() + " = '" + one.getValue() + "'");
            }

            key = " and ";
        }

        return whereSQL;
    }

    @GetMapping("spanNameHttplist")
    public List<QuotaModel> spanNameHttplist(
            @RequestParam(value = "spanname", required = true) String spanname) {
        spanname = spanname.toLowerCase();
        List<QuotaModel> logs = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT uri,sum(success) success，sum(fail) fail,avg(excuteTime) avgtime ,max(excuteTime) maxtime ,count(*) allcount FROM  " + adminServerProperties.getRealDefaultIndexName() + " " +
                " where spanName ='" + spanname + "' and eventName='system_http_tracer'  " +
                " group by  uri ");
        List<Map<String, Object>> traceidList = elastricSearchHelper.excuceSQL(sql.toString());
        if (traceidList.size() > 0) {
            for (Map<String, Object> one : traceidList) {
                if (one.size() > 0) {
                    QuotaModel quotaModel = new QuotaModel();
                    quotaModel.setUri(one.get("uri").toString());
                    //quotaModel.setAllcount(.toString());
                    quotaModel.setAllcount(Double.valueOf(one.get("allcount").toString()).intValue());
                    quotaModel.setSuccess(Double.valueOf(one.get("success").toString()).intValue());
                    quotaModel.setFail(Double.valueOf(one.get("fail").toString()).intValue());
                    quotaModel.setAvgtime(Double.valueOf(one.get("avgtime").toString()));
                    quotaModel.setMaxtime(Double.valueOf(one.get("maxtime").toString()).intValue());

                    logs.add(quotaModel);
                }
            }
        }
        return logs;
    }

    @GetMapping("spanNameSQLlist")
    public List<QuotaSQLModel> spanNameSQLlist(
            @RequestParam(value = "spanname", required = true) String spanname) {
        spanname = spanname.toLowerCase();
        List<QuotaSQLModel> logs = new ArrayList<>();
        StringBuffer sql = new StringBuffer("SELECT sql,excuteTime,eventStartTime FROM  " + adminServerProperties.getRealDefaultIndexName() + " " +
                " where spanName ='" + spanname + "' and eventName='system_sql_springJDBC_tracer' and excuteTime>1500 order by excuteTime desc");
        List<Map<String, Object>> traceidList = elastricSearchHelper.excuceSQL(sql.toString());
        if (traceidList.size() > 0) {
            for (Map<String, Object> one : traceidList) {
                if (one.size() > 0) {
                    QuotaSQLModel quotaSQLModel = new QuotaSQLModel();
                    quotaSQLModel.setSql(one.get("sql").toString());
                    quotaSQLModel.setExcutetime(Double.valueOf(one.get("excuteTime").toString()).intValue());
                    if (one.containsKey("eventStartTime") && one.get("eventStartTime") != null) {
                        Long startTime = (Long) one.get("eventStartTime");
                        quotaSQLModel.setEventstarttime(startTime);
                        quotaSQLModel.setEventstarttimestr(DateUtil.dateToStr(new Date(startTime), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS));
                    }
                    logs.add(quotaSQLModel);
                }
            }
        }
        return logs;
    }
}
