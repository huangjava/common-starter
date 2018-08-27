package com.demo.router.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    /**
     * http://localhost:8081/demo/helloworld
     * http://localhost:8081/v1/simple2/demo/helloworld
     * @return
     */
    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public String helloworld() {
        System.out.println("helloworld");
        return "成功";
    }

}
