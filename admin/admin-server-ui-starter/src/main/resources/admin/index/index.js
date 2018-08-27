//获取用户的菜单和角色
var contentVM = new Vue({
        el: '#wrapper',
        data: {
            menus: '',
            logs: ''
        },
        updated: function () {
            //渲染结束之后调用菜单初始化
            $('#side-menu').metisMenu();
            //初始化原先内容
            Hinit();
        }
    });
do_get(
    server+"discovery/listMicroService",
    {},
    function (data) {
        contentVM.logs = data.microservers;
        contentVM.menus = data.servers;
    },
    function(data){
        alert("获取角色,菜单失败");
    }
)

/**
 * 退出
 */
function loginout() {
    do_get(
        server+"loginout",
        {},
        function (data) {
            //清空用户缓存
            cleanUserCode();
            alert("退出成功")
            window.location.href = server+'login/login.html';
        }
    )
}

function Hinit(){

    $("#fixednavbar").click(function () {
        if ($("#fixednavbar").is(":checked")) {
            $(".navbar-static-top").removeClass("navbar-static-top").addClass("navbar-fixed-top");
            $("body").removeClass("boxed-layout");
            $("body").addClass("fixed-nav");
            $("#boxedlayout").prop("checked", false);
            if (localStorageSupport) {
                localStorage.setItem("boxedlayout", "off")
            }
            if (localStorageSupport) {
                localStorage.setItem("fixednavbar", "on")
            }
        } else {
            $(".navbar-fixed-top").removeClass("navbar-fixed-top").addClass("navbar-static-top");
            $("body").removeClass("fixed-nav");
            if (localStorageSupport) {
                localStorage.setItem("fixednavbar", "off")
            }
        }
    });
    $("#collapsemenu").click(function () {
        if ($("#collapsemenu").is(":checked")) {
            $("body").addClass("mini-navbar");
            SmoothlyMenu();
            if (localStorageSupport) {
                localStorage.setItem("collapse_menu", "on")
            }
        } else {
            $("body").removeClass("mini-navbar");
            SmoothlyMenu();
            if (localStorageSupport) {
                localStorage.setItem("collapse_menu", "off")
            }
        }
    });
    $("#boxedlayout").click(function () {
        if ($("#boxedlayout").is(":checked")) {
            $("body").addClass("boxed-layout");
            $("#fixednavbar").prop("checked", false);
            $(".navbar-fixed-top").removeClass("navbar-fixed-top").addClass("navbar-static-top");
            $("body").removeClass("fixed-nav");
            if (localStorageSupport) {
                localStorage.setItem("fixednavbar", "off")
            }
            if (localStorageSupport) {
                localStorage.setItem("boxedlayout", "on")
            }
        } else {
            $("body").removeClass("boxed-layout");
            if (localStorageSupport) {
                localStorage.setItem("boxedlayout", "off")
            }
        }
    });
    //$(".spin-icon").click(function () {
    //    $(".theme-config-box").toggleClass("show")
    //});
    //$(".s-skin-0").click(function () {
    //    $("body").removeClass("skin-1");
    //    $("body").removeClass("skin-2");
    //    $("body").removeClass("skin-3")
    //});
    //$(".s-skin-1").click(function () {
    //    $("body").removeClass("skin-2");
    //    $("body").removeClass("skin-3");
    //    $("body").addClass("skin-1")
    //});
    //$(".s-skin-3").click(function () {
    //    $("body").removeClass("skin-1");
    //    $("body").removeClass("skin-2");
    //    $("body").addClass("skin-3")
    //});
    if (localStorageSupport) {
        var collapse = localStorage.getItem("collapse_menu");
        var fixednavbar = localStorage.getItem("fixednavbar");
        var boxedlayout = localStorage.getItem("boxedlayout");
        if (collapse == "on") {
            $("#collapsemenu").prop("checked", "checked")
        }
        if (fixednavbar == "on") {
            $("#fixednavbar").prop("checked", "checked")
        }
        if (boxedlayout == "on") {
            $("#boxedlayout").prop("checked", "checked")
        }
    }
}
