var index_v1_body_contentVM;

do_get(
    server + "discovery/index_v1_num",
    {},
    function (data) {
        index_v1_body_contentVM = new Vue({
            el: '#index_v1_body',
            data: {
                data: data
            }
        });
    },
    function (data) {
        alert("获取失败");
    }
)

/**
 *
 * @param code
 * @param type       type 0,后台版本 ,   type 1, url版本
 */
function add() {
    $("#myModal-add-info").modal({
        remote: "../server/html/addServer.html",
        show: true
    });
    return
}