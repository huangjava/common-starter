//获取uuid
var server_message_contentVM;
var env_message_contentVM;
var metrics_gc_message_contentVM;
var metrics_nothead_message_contentVM;
var metrics_head_message_contentVM;
var  metrics_classes_message_contentVM;
var  metrics_thread_message_contentVM;

var uuid = GetRequest().data.replace("?v", "");
getServer();
getEnv();
getMetrics();
getMetricsGCMessage();
getMetricsHeadMessage();
getMetricsNotHeadMessage();
getMetricsClassesMessage();
getMetricsThreadMessage();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//获取server相关信息
function getServer() {
    do_get(
        server + "discovery/getServer?",
        {"uuid": uuid},
        function (data) {
            data.address = server + uuid + "/logfile";
            //初始化html
            server_message_contentVM = new Vue({
                el: '#server_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取失败");
        }
    )
}
function getEnv() {
    do_get(
        server + uuid + "/env",
        {},
        function (data) {
            //初始化连接
            env_message_contentVM = new Vue({
                el: '#env_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取失败");
        }
    )
}

//内存使用情况图表----------------------------start
var metricsMemMessageChartObj = echarts.init(document.getElementById('metrics_mem_message_chart'));
var metricsMemMessageChartObjOption = {
    title: {
        text: '单位(MB)'
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            console.info(params);
            return params[0].data.value[0] + ',总量:' + params[0].data.value[1] + ",剩余:" + params[1].data.value[1];
        },
        axisPointer: {
            animation: false
        }
    },
    legend: {
        data: ['内存总量', '内存剩余量']
    },
    xAxis: {
        type: 'time',
        splitLine: {
            show: false
        }
    },
    yAxis: {
        type: 'value',
        boundaryGap: [0, '100%'],
        splitLine: {
            show: false
        }
    },
    series: [
        {
            name: '内存总量',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false
        },
        {
            name: '内存剩余量',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false
        }
    ]
};
metricsMemMessageChartObj.setOption(metricsMemMessageChartObjOption);

var metricsMemMessageChartObjData1 = [];
var metricsMemMessageChartObjData2 = [];
setInterval(function () {
    do_get(
        server  + uuid + "/metrics/mem.*",
        {},
        function (data) {
            var menFree = data['mem.free'] / 1024;
            var memAll = data.mem / 1024;
            var now = new Date();

            var nameDay = [now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/');
            var nameTime = [now.getHours(), now.getMinutes(), now.getSeconds()].join(':');

            metricsMemMessageChartObjData1.push({
                name: now.toString(),
                value: [
                    nameDay + " " + nameTime,
                    memAll
                ]
            })
            metricsMemMessageChartObjData2.push({
                name: now.toString(),
                value: [
                    nameDay + " " + nameTime,
                    menFree
                ]
            })

            metricsMemMessageChartObj.setOption({
                series: [{
                    data: metricsMemMessageChartObjData1
                }, {
                    data: metricsMemMessageChartObjData2
                }]
            });
        },
        function (data) {

        }
    )
}, 2000);
//内存使用情况图表----------------------------end

//gc 详情----------------------------start

function getMetricsGCMessage() {
    do_get(
        server + uuid + "/metrics/gc.*",
        {},
        function (data) {
            //初始化连接
            metrics_gc_message_contentVM = new Vue({
                el: '#metrics_gc_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取失败");
        }
    )
}

//gc 详情----------------------------end
function getMetricsHeadMessage() {
    do_get(
        server  + uuid + "/metrics/heap.*",
        {},
        function (data) {
            metrics_head_message_contentVM = new Vue({
                el: '#metrics_head_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取堆失败");
        }
    )
}
function getMetricsNotHeadMessage() {
    do_get(
        server + uuid + "/metrics/nonheap.*",
        {},
        function (data) {
            metrics_nothead_message_contentVM = new Vue({
                el: '#metrics_nothead_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取非堆失败");
        }
    )
}
function getMetricsClassesMessage() {
    do_get(
        server  + uuid + "/metrics/classes.*",
        {},
        function (data) {
            metrics_classes_message_contentVM = new Vue({
                el: '#metrics_classes_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取非堆失败");
        }
    )
}
function getMetricsThreadMessage() {
    do_get(
        server + uuid + "/metrics/threads.*",
        {},
        function (data) {
            metrics_thread_message_contentVM = new Vue({
                el: '#metrics_threads_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取非堆失败");
        }
    )
}

function getMetrics() {
    do_get(
        server  + uuid + "/metrics",
        {},
        function (data) {
            var color = ["#1ab394", "#F5F5F5"];
            if ((data['mem.free'] / data.mem) < 0.2) {
                color = ["#ed5565", "#F5F5F5"];
            }
            //初始化连接
            $("#sparkline7").sparkline([data.mem - data['mem.free'], data.mem], {
                type: "pie",
                height: "140",
                sliceColors: color
            });
            metrics_message_contentVM = new Vue({
                el: '#metrics_message',
                data: {
                    data: data
                }
            });
        },
        function (data) {
            alert("获取失败");
        }
    )
}