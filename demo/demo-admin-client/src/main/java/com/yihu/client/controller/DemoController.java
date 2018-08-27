package com.yihu.client.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * Created by chenweida on 2018/5/23 0023.
 */
@RestController
@RequestMapping("demo")
@Api(description = "demo例子")
public class DemoController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "ceshisql")
    @RequestMapping(value = "/testSQL", method = RequestMethod.GET)
    public String loginfoGET(
            String sql
    ) {
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        return "成功";
    }

    @ApiOperation(value = "ceshisql")
    @RequestMapping(value = "/testSQL", method = RequestMethod.DELETE)
    public String loginfoDELETE(
            String sql
    ) {
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        return "成功";
    }

    @ApiOperation(value = "ceshisql")
    @RequestMapping(value = "/testSQL", method = RequestMethod.PUT)
    public String loginfoPUT(
            String sql
    ) {
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        return "成功";
    }

    @ApiOperation(value = "ceshisql")
    @RequestMapping(value = "/testSQL", method = RequestMethod.POST)
    public String loginfoPOST(
            String sql
    ) throws InterruptedException {
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql);
        return "成功";
    }

    @ApiOperation(value = "ceshisql")
    @RequestMapping(value = "/testSQL/{value}", method = RequestMethod.GET)
    public String loginfoValue(
            @PathVariable(value = "value") String value
    ) {
        jdbcTemplate.queryForList(value);
        jdbcTemplate.queryForList(value);
        jdbcTemplate.queryForList(value);
        return "成功";
    }
}
