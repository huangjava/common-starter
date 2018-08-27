package com.yihu.base.router;

import com.alibaba.fastjson.JSON;
import com.yihu.base.router.model.Ratelimit;
import com.yihu.base.router.model.RuleRoute;
import com.yihu.base.router.ratelimit.storage.IRateLimitStorage;
import com.yihu.base.router.rule.storage.IRuleStorage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by chenweida on 2018/4/27 0027.
 * 路由规则端点
 */
@RestController
@RequestMapping("/route")
public class RouteEndPoint {
    @Autowired
    private IRuleStorage ruleStorage;
    @Autowired
    private IRateLimitStorage rateLimitStorage;
    @Autowired
    private RefreshRouteService refreshRouteService;

    @PostMapping(value = "/rule/save")
    @ApiOperation(value = "新增路由规则", notes = "")
    public String saveRule(
            @ApiParam(name = "id", value = "主键", required = true) @RequestParam(required = true, name = "id") String id,
            @ApiParam(name = "path", value = "网关暴露出去的路径  例:/v1/simple2/**", required = true) @RequestParam(required = true, name = "path") String path,
            @ApiParam(name = "serviceId", value = "注册到微服务的Id", required = false) @RequestParam(required = false, name = "serviceId") String serviceId,
            @ApiParam(name = "url", value = "跳转的路径 例:http://localhost:10010/", required = false) @RequestParam(required = false, name = "url") String url,
            @ApiParam(name = "retryable", value = "失败是否重试 ", required = false) @RequestParam(required = false, name = "retryable", defaultValue = "true") Boolean retryable,
            @ApiParam(name = "enabled", value = "是否有用", required = false) @RequestParam(required = false, name = "enabled", defaultValue = "true") Boolean enabled
    ) {
        RuleRoute ruleRoute = new RuleRoute();
        ruleRoute.setId(id);
        ruleRoute.setEnabled(enabled);
        ruleRoute.setPath(path);
        ruleRoute.setServiceId(serviceId);
        ruleRoute.setRetryable(retryable);
        ruleRoute.setUrl(url);
        ruleStorage.saveRuleStorage(ruleRoute);
        return "成功";
    }

    @PostMapping(value = "/rule/update")
    @ApiOperation(value = "修改路由规则", notes = "")
    public String updateRule(
            @ApiParam(name = "id", value = "主键", required = true) @RequestParam(required = true, name = "id") String id,
            @ApiParam(name = "path", value = "网关暴露出去的路径 例:/v1/simple2/**", required = true) @RequestParam(required = true, name = "path") String path,
            @ApiParam(name = "serviceId", value = "注册到微服务的Id", required = false) @RequestParam(required = false, name = "serviceId") String serviceId,
            @ApiParam(name = "url", value = "跳转的路径 例:http://localhost:10010/", required = false) @RequestParam(required = false, name = "url") String url,
            @ApiParam(name = "retryable", value = "失败是否重试 ", required = false) @RequestParam(required = false, name = "retryable", defaultValue = "true") Boolean retryable,
            @ApiParam(name = "enabled", value = "是否有用", required = false) @RequestParam(required = false, name = "enabled", defaultValue = "true") Boolean enabled
    ) {
        RuleRoute ruleRoute = ruleStorage.getRuleStorageById(id);
        if (!StringUtils.isEmpty(path)) {
            ruleRoute.setPath(path);
        }
        if (!StringUtils.isEmpty(path)) {
            ruleRoute.setServiceId(serviceId);
        }
        if (!StringUtils.isEmpty(path)) {
            ruleRoute.setRetryable(retryable);
        }
        if (!StringUtils.isEmpty(path)) {
            ruleRoute.setEnabled(enabled);
        }
        if (!StringUtils.isEmpty(path)) {
            ruleRoute.setUrl(url);
        }
        ruleStorage.updateRuleStorage(ruleRoute);
        return "成功";
    }

    @DeleteMapping(value = "/rule/delete")
    @ApiOperation(value = "删除路由规则", notes = "")
    public String saveRule(
            @ApiParam(name = "id", value = "主键", required = true) @RequestParam(required = true, name = "id") String id
    ) {
        ruleStorage.deleteRuleStorageById(id);
        return "成功";
    }

    @PostMapping(value = "/rule/refresh")
    @ApiOperation(value = "刷新路由列表", notes = "")
    public String refresh(
    ) {
        refreshRouteService.refreshRoute();
        return "成功";
    }

    /**
     * @return
     */
    @PostMapping(value = "/rule/exportAll")
    @ApiOperation(value = "导出路由列表", notes = "")
    public List<RuleRoute> export(
    ) {
        return ruleStorage.listRuleStorage();
    }

    /**
     * @return
     */
    @PostMapping(value = "/rule/importAll")
    @ApiOperation(value = "导出路由列表", notes = "")
    public String importAll(
            @ApiParam(name = "jsonData", value = " 路由列表list", defaultValue = "")
            @RequestBody String jsonData
    ) {
        try {
            List<RuleRoute> ruleRoutes = JSON.parseArray(jsonData, RuleRoute.class);
            for (int i = 0; i < ruleRoutes.size(); i++) {
                ruleStorage.saveRuleStorage(ruleRoutes.get(i));
            }
            return "成功";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping(value = "/ratelimit/save")
    @ApiOperation(value = "新增路由限制规则", notes = "")
    public String saveRule(
            @ApiParam(name = "id", value = "主键", required = true) @RequestParam(required = true, name = "id") String id,
            @ApiParam(name = "excludeIp", value = "ip地址", required = false) @RequestParam(required = false, name = "excludeIp") String excludeIp,
            @ApiParam(name = "includeIp", value = "ip地址", required = false) @RequestParam(required = false, name = "includeIp") String includeIp,
            @ApiParam(name = "path", value = "限制访问的路径", required = false) @RequestParam(required = false, name = "path") String path,
            @ApiParam(name = "accessCount", value = "次数", required = false) @RequestParam(required = false, name = "accessCount",defaultValue = "0") Integer accessCount,
            @ApiParam(name = "accessUnit", value = "访问的次数限制单位 默认是分钟  1秒 2 分钟 3小时 4天 结合accessCount使用 ", required = false) @RequestParam(required = false, name = "accessUnit",defaultValue = "2") String accessUnit
    ) {
        Ratelimit ratelimit=new Ratelimit();
        ratelimit.setId(id);
        ratelimit.setAccessCount(accessCount);
        ratelimit.setAccessUnit(accessUnit);
        ratelimit.setExcludeIp(excludeIp);
        ratelimit.setIncludeIp(includeIp);
        ratelimit.setPath(path);
        rateLimitStorage.saveRateLimit(ratelimit);
        return "成功";
    }

    @DeleteMapping(value = "/ratelimit/delete")
    @ApiOperation(value = "删除路由限制规则", notes = "")
    public String deleteRule(
            @ApiParam(name = "id", value = "主键", required = true) @RequestParam(required = true, name = "id") String id
      ) {
        rateLimitStorage.deleteRateLimitById(id);
        return "成功";
    }
    /**
     * @return
     */
    @PostMapping(value = "/ratelimit/exportAll")
    @ApiOperation(value = "导出路由拦截规则列表", notes = "")
    public List<Ratelimit> ratelimitExport(
    ) {
        return rateLimitStorage.findAllRatelimit();
    }

    /**
     * @return
     */
    @PostMapping(value = "/ratelimit/importAll")
    @ApiOperation(value = "导出路由拦截规则列表", notes = "")
    public String ratelimitImportAll(
            @ApiParam(name = "jsonData", value = " 路由列表list", defaultValue = "")
            @RequestBody String jsonData
    ) {
        try {
            List<Ratelimit> ratelimits = JSON.parseArray(jsonData, Ratelimit.class);
            for (int i = 0; i < ratelimits.size(); i++) {
                rateLimitStorage.saveRateLimit(ratelimits.get(i));
            }
            return "成功";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
