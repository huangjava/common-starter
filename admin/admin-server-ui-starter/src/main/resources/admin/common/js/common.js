//var server="http://127.0.0.1:8999/";
var server="/";
var not_login=-1000;


function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
        }
    }
    //添加toName的信息
    if (theRequest.toName) {
        localStorage.setItem("toName", theRequest.toName);
    }
    return theRequest;
}