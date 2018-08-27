var appName = GetRequest().appName.replace("?v", "");

$.jgrid.defaults.styleUI = "Bootstrap";
//SELECT sum(success) success，sum(fail) fail,avg(excutetime) avgtime ,max(excutetime) maxtime ,count(*) allcount FROM  admin_log where spanname ="demo-socket-client" group by  uri
$("#table_list_1").jqGrid({
    url:server + "admin_log/spanNameSQLlist?spanname="+appName,
    datatype: "json",
    mtype: "GET",
    height: "100%",
    autowidth: true,
    shrinkToFit: true,
    //colNames: ["序号", "接口路径", "请求总次数", "成功次数", "失败次数", "请求平均时间(ms)", "最长请求时间(ms)"],
    colModel: [
        //{label:"序号",name: "id",width: 30, sorttype: "int", align: "center" },
        {label:"sql语句",name: "sql",width: "80%",align: "left"},
        {label:"请求时间(ms)",name: "excutetime",width: "10%",align: "right", sorttype: "int"},
        {label:"执行时间",name: "eventstarttimestr",width: "10%",align: "right", sorttype: "int"},

    ],
    loadonce: true,
    // pager: "#pager_list_1",
    viewrecords: true,
    caption: "接口统计分析",
    hidegrid: false
});
