**项目必须是springboot工程
quartz 版本 2.3.0**

# 如何在项目中使用？
## 1.依赖common-quartz工程``

```
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${版本以项目中最新的版本为主}</version>
        </dependency>
        
```

## 2.在项目yml添加配置
    quartz:
      namespace: svr-demo ##quartz的命名空间，名称一样实现消费负载
      overwriteExistingJobs: true ##是否覆盖job
      
      
## 3.因为quartz使用的是数据库保存job的方式，所以spring Bean容器中必须要有javax.sql.DataSource数据源对象.同时库中要存在对应的表（建表sql在common-quartz 项目中）

## 4.以上配置均确认没问题后，可以在项目中用spring注入QuartzHelper工具类.
    
    @Autowired
    private QuartzHelper quartzHelper;
    
    
##     QuartzHelper方法说明

          根据cronString表达式添加任务到quartz框架中
        public void addJob(
            Class jobClass,  job类
            String cronString,    cronString表达式
            String jobKey, 任务ID
            Map<String, Object> params 需要传递的参数
            ) throws Exception {
            
        }
        
         删除quartz框架中任务
        public void removeJob(
            String jobKeyString  任务ID
        ) throws Exception {
        }
        
         根据任务ID判断任务是否存在
        public boolean isExistJob(
            String jobKey 任务ID
        ) throws SchedulerException {
          
        }
        
        立即执行任务。
        public void startNow(
            Class jobClass,  job类
            String id,  任务ID
            Map<String, Object> params 需要传递的参数
        ) throws Exception {
        }
        
        在指定时间点执行。
        public void startAt(
            Date time,  时间
            Class jobClass,job类
            String id, 任务ID
            Map<String, Object> params 需要传递的参数
        ) throws Exception {

        }
        
        
###         1.新增一个类实现 org.quartz.job接口
        例如：   
        @Component
        @Scope("prototype")
        @DisallowConcurrentExecution//防止到了执行时间点前一任务还在执行中，但是这时有空闲的线程，那么马上又会执行，这样一来就会存在同一job被并行执行
        public class FollowUpJob implements Job {
            @Autowired
            private WlyyQuotaResultDao wlyyQuotaResultDao;//指标结果Dao

            @Override
            public void execute(JobExecutionContext context)
                    throws JobExecutionException {

            }

        }
        
        2.在方法中注入QuartzHelper对象并且把例子中的任务添加到quartz框架中  
        
        
        quartzHelper.startNow(FollowUpJob.class, "followUpjobID", params);

    
    