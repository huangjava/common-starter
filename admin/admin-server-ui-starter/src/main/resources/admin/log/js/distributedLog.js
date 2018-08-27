//获取日志信息
var contentVM = new Vue({
    el: '#ibox-content',
    data: {
        messages: []
    }
});
var page = 0
var pageSize = 10
searchDistributedLog();

function searchDistributedLog() {
    do_get(
        server + "admin_log/list",
        {
            page: page,
            pageSize: pageSize,
            traceId: $("#search_trace_iD").val()?$("#search_trace_iD").val():"",
            time:$("#time_trace_iD").val()?$("#time_trace_iD").val():0
        },
        function (data) {
            contentVM.messages = data
        },
        function (data) {
            alert("获取日志失败");
        }
    )
}
function clearDistributedLog() {
    $("#search_trace_iD").val("");
    return
}


function seeLog(traceid) {
    parent.layer.open({
        type: 2,
        title: '流程详情',
        shadeClose: true,
        shade: 0.8,
        area: ['80%', '80%'],
        content: '../log/html/distributedLogDetail.html?traceId=' + traceid //iframe的url
    });
    return
}