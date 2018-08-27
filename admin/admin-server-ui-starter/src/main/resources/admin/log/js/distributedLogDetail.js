var traceId = GetRequest().traceId.replace("?v","");
var sort="asc"
var contentLogDetail = new Vue({
    el: '#ibox-content-log-detail',
    data: {
        messages: []
    }
});
do_get(
    server + "admin_log/tracelist",
    {
        traceId: traceId,
        sort:sort
    },
    function (data) {
        contentLogDetail.messages = data
    },
    function (data) {
        alert("获取日志详情失败");
    }
)
function closeOrOpen(div){
    var childDiv= $($($($(div).parent()).children("div")[1]).children("div")[0]);
    childDiv.toggle(1000);
}