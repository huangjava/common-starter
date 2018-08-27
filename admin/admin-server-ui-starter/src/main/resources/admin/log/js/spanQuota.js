var appName = GetRequest().appName.replace("?v", "");

$.jgrid.defaults.styleUI = "Bootstrap";
//SELECT sum(success) success，sum(fail) fail,avg(excutetime) avgtime ,max(excutetime) maxtime ,count(*) allcount FROM  admin_log where spanname ="demo-socket-client" group by  uri
$("#table_list_1").jqGrid({
    url:server + "admin_log/spanNameHttplist?spanname="+appName,
    datatype: "json",
    mtype: "GET",
    height: "100%",
    autowidth: true,
    shrinkToFit: true,
    //colNames: ["序号", "接口路径", "请求总次数", "成功次数", "失败次数", "请求平均时间(ms)", "最长请求时间(ms)"],
    colModel: [
        //{label:"序号",name: "id",width: 30, sorttype: "int", align: "center" },
        {label:"接口路径",name: "uri",width: 150,align: "left"},
        {label:"请求总次数",name: "allcount",align: "right",  width: 100, sorttype: "int"},
        {label:"成功次数",name: "success",width: 80,align: "right", sorttype: "int"},
        {label:"失败次数",name: "fail",  width: 80, align: "right", sorttype: "int" },
        {label:"请求平均时间(ms)",name: "avgtime",width: 80,align: "right", sorttype: "int"},
        {label:"最长请求时间(ms)",name: "maxtime",  width: 80,align: "right", sorttype: "int"}
    ],
    loadonce: true,
    // pager: "#pager_list_1",
    viewrecords: true,
    caption: "接口统计分析",
    hidegrid: false
});
