package com.demo.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.yihu.base.es.config.ElastricSearchHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by chenweida on 2018/2/24.
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    private Logger logger = LoggerFactory.getLogger("hbase_logger");
    @Autowired
    private ElastricSearchHelper elastricSearchHelper;

    @RequestMapping(value = "/loginfo", method = RequestMethod.GET)
    public String loginfo() {
        boolean flag = true;
        Random random1 = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        while (flag) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", UUID.randomUUID().toString().substring(0, 6));
            jsonObject.put("age", random1.nextInt(100) + "");
            jsonObject.put("date", simpleDateFormat.format(new Date()));
            logger.info(jsonObject.toString());
        }
        return "成功";
    }

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public String helloworld() {
        System.out.println("helloworld");
        return "成功";
    }

    @RequestMapping(value = "/esSQL", method = RequestMethod.GET)
    public String esSQL(
            String sql
    ) throws SQLException {
      //  com.alibaba.druid.pool.DruidAbstractDataSource;
        System.out.println(elastricSearchHelper.excuceCountSQL(sql));
        return "成功";
    }
}
