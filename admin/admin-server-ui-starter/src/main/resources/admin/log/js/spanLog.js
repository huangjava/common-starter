var appName = GetRequest().appName.replace("?v","");
var page = 0
var pageSize = 20
var sort="desc"
var contentLogDetail = new Vue({
    el: '#ibox-content-log-detail',
    data: {
        messages: []
    }
});
do_get(
    server + "admin_log/tracelist",
    {
        spanname: appName,
        eventname:"system_http_tracer",
        sort:sort
    },
    function (data) {
        contentLogDetail.messages = data
    },
    function (data) {
        alert("获取日志详情失败");
    }
)
function searchSpanLog() {
    do_get(
        server + "admin_log/tracelist",
        {
            page: page,
            pageSize: pageSize,
            spanname: appName,
            sort:sort,
            eventname:"system_http_tracer",
            uri: $("#spanLog_search_url").val()?$("#spanLog_search_url").val():"",
            time:$("#spanLog_time_search").val()?$("#spanLog_time_search").val():0
        },
        function (data) {
            contentLogDetail.messages = data
        },
        function (data) {
            alert("获取日志失败");
        }
    )
}
function clearSpanLog() {
    $("#spanLog_search_url").val("");
    $("#spanLog_time_search").val("");
    return
}

function closeOrOpen(div){
    var childDiv= $($($($(div).parent()).children("div")[1]).children("div")[0]);
    childDiv.toggle(1000);
}