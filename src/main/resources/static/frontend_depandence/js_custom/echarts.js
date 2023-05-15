color = ['#0085D5', '#26B99A', '#34495E', '#BDC3C7', '#9B59B6', '#8abb6f', '#759c6a', '#bfd3b7'];

/* 柱状图 */
function init_echarts_bar() {
    if (!$('#echarts_bar').length) {
        return;
    }
    var theme = {
        color: color,
        title: {
//            itemGap: 8,
            textStyle: {
                fontWeight: 'normal',
                color: '#0085D5'
            }
        },
        grid: {
            left: '6%',
            borderWidth: 0
        },
        /*timeline: {
            lineStyle: {
                color: '#408829'
            },
            controlStyle: {
                normal: { color: '#408829' },
                emphasis: { color: '#408829' }
            }
        }*/
    };
    var echartsBar = echarts.init(document.getElementById('echarts_bar'), 'dark');
    var option = {
         title: {
             text: '仪器排行',
             subtext: '2023-05-11'
         },
         tooltip: {
             trigger: 'axis'
         },
         /*legend: {
             show: false,
             x: 100,
             data: ['2015']
         },*/
         toolbox: {
             show: true,
             feature: {
                 saveAsImage: {
                     show: true,
                     title: "Save Image"
                 }
             }
         },
         xAxis: {
//                max: 'dataMax',
            type: 'value',
            boundaryGap: [0, 0.01]
         },
         yAxis: [{
            inverse: true,
            type: 'category',
            data: ['GC01', 'GC02', 'GC03', 'GC04', 'GC05', 'GC06'],
            animationDuration: 300,
            animationDurationUpdate: 300
         }],
         series: [{
             name: '2015',
             type: 'bar',
             realtimeSort: true,
             data: [311, 380, 219, 213, 112, 199],
             itemStyle: {
                normal: {
                    label: {
                        show: true, //开启显示
                        position: 'right', //在上方显示
                        textStyle: { //数值样式
                            color: 'black'
                        }
                    }
                }
             },
         }],
         /*legend: {
            show: true,
            textStyle: {
                color:'#000',				//---所有图例的字体颜色
                backgroundColor:'black',	//---所有图例的字体背景色
            }
                formatter: (params) => {
                return "test";
            }
         },*/
         animationDuration: 0,
         animationDurationUpdate: 3000,
         animationEasing: 'linear',
         animationEasingUpdate: 'linear'
    }

    echartsBar.setOption(option);
}

/*环状图*/
function init_echarts_pie() {
    if (!$('#echarts_pie').length) {
        return;
    }
    var theme = {
        color: color
    };
    var echartsPie = echarts.init(document.getElementById('echarts_pie'), 'dark');
    var option = {
      tooltip: {
        trigger: 'item'
      },
      title: {
           text: '运行时间占比',
//           subtext: '2023-05-11'
      },
      legend: {
        top: '7%',
        left: 'center'
      },
      series: [
        {
//              name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          /*label: {
            show: false, // 是否显示数值
            position: 'center'
          },*/
          emphasis: {
            label: {
              show: true,
              fontSize: 40,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          top: '10%',
          data: [
            { value: 1048, name: 'GC01 20%' },
            { value: 735, name: 'GC02 25%' },
            { value: 580, name: 'GC MS 20%' },
            { value: 484, name: 'LC01 10%' },
            { value: 300, name: 'LC MS01 5%' },
            { value: 300, name: 'LC MS02 5%' },
            { value: 300, name: 'GC MS01 5%' },
            { value: 300, name: 'GC MS02 5%' }
          ]
        }
      ]
    };
    echartsPie.setOption(option);
}

/*环状图*/
function init_echarts_gauge() {
    if (!$('#echarts_gauge').length) {
        return;
    }
    var theme = {
        color: color
    };
    var echartsGauge = echarts.init(document.getElementById('echarts_gauge'), 'dark');
    var option = {
        title: {
            text: '仪器运行统计',
        },
        series: [{
            type: 'gauge',
            startAngle: 180,
            endAngle: 0,
            center: ['50%', '75%'],
            radius: '90%',
            min: 0,
            max: 1,
            splitNumber: 8,
            axisLine: {
                lineStyle: {
                    width: 6,
                    color: [
                        [0.25, '#7CFFB2'],
                        [0.75, '#58D9F9'],
                        [1, '#FF6E76']
                    ]/*,
                    color: [
                        [0.25, '#7CFFB2'],
                        [0.5, '#58D9F9'],
                        [0.75, '#FDDD60'],
                        [1, '#FF6E76']
                    ]*/
                }
            },
            pointer: {
                icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
                length: '12%',
                width: 20,
                offsetCenter: [0, '-60%'],
                itemStyle: {
                    color: 'inherit'
                }
            },
            axisTick: {
                length: 12,
                lineStyle: {
                    color: 'inherit',
                    width: 2
                }
            },
            splitLine: {
                length: 20,
                lineStyle: {
                    color: 'inherit',
                    width: 5
                }
            },
            axisLabel: {
                color: '#464646',
                fontSize: 20,
                distance: -60,
                rotate: 'tangential',
                formatter: function (value) {
                    if (value === 0.125) {
                        return '低';
                    } else if (value === 0.5) {
                        return '正常';
                    } else/* if (value === 0.625) {
                        return 'Grade C';
                    } else*/ if (value === 0.875) {
                        return '满负荷';
                    }
                    return '';
                }
            },
            title: {
                offsetCenter: [0, '-10%'],
                fontSize: 20
            },
            detail: {
                fontSize: 30,
                offsetCenter: [0, '-35%'],
                valueAnimation: true,
                formatter: function (value) {
                    return Math.round(value * 100) + '台';
                },
                color: 'inherit'
            },
            data: [{
                value: 0.45,
                name: '仪器运行个数'
            }]
        }]
    };
    option && echartsGauge.setOption(option);
}