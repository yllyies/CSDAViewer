function init_charts() {
    if( typeof (Chart) === 'undefined'){ return; }
    // Bar chart
    if ($('#mybarChart1').length){
      var ctx = document.getElementById("mybarChart1");
      var mybarChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: barLabels,
          datasets: barDatasets
        },
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              }
            }]
          },
          hover: {
            animationDuration: 0  // 防止鼠标移上去，数字闪烁
          },
          animation: {           // 这部分是数值显示的功能实现
            onComplete: function () {
              var chartInstance = this.chart,
              ctx = chartInstance.ctx;
              // 以下属于canvas的属性（font、fillStyle、textAlign...）
              ctx.font = Chart.helpers.fontString("10", Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);
              ctx.fillStyle = "black";
              ctx.textAlign = 'center';
              ctx.textBaseline = 'bottom';
              this.data.datasets.forEach(function (dataset, i) {
                  var meta = chartInstance.controller.getDatasetMeta(i);
                  meta.data.forEach(function (bar, index) {
                      var data = dataset.data[index];
                      var label = dataset.label;
                     ctx.fillText(label + ":" + data + "H", bar._model.x, bar._model.y-5);
                  });
              });
            }
          }
        }
      });
    }
    // 线图：x轴为时间区间
    if ($('#lineChart1').length){
      var ctx = document.getElementById("lineChart1");
      var lineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: lineLabels,
          datasets: lineDatasets
        },
         options: {
           scales: {
             yAxes: [{
               ticks: {
                 beginAtZero: true
               }
             }]
           },
           hover: {
             animationDuration: 0  // 防止鼠标移上去，数字闪烁
           },
           animation: {           // 这部分是数值显示的功能实现
             onComplete: function () {
               var chartInstance = this.chart,
               ctx = chartInstance.ctx;
               // 以下属于canvas的属性（font、fillStyle、textAlign...）
               ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, Chart.defaults.global.defaultFontStyle, Chart.defaults.global.defaultFontFamily);
               ctx.fillStyle = "black";
               ctx.textAlign = 'center';
               ctx.textBaseline = 'bottom';
               this.data.datasets.forEach(function (dataset, i) {
                   var meta = chartInstance.controller.getDatasetMeta(i);
                   meta.data.forEach(function (bar, index) {
                       var data = dataset.data[index];
                       ctx.fillText(data + "H", bar._model.x, bar._model.y-5);
                   });
               });
             }
           }
         }
      });
    }

    if ($('#canvasDoughnut1').length && doughnutLabels.length && doughnutDatasets.length){
      var ctx = document.getElementById("canvasDoughnut1");
      var data = {
        labels: doughnutLabels,
        datasets: [{
          data: doughnutDatasets,
          backgroundColor: [
            "#d51818",
            "#00f91a",
            "#455C73",
            "#26B99A",
            "#3498DB"
          ]
        }]
      };
      var canvasDoughnut = new Chart(ctx, {
        type: 'doughnut',
        tooltipFillColor: "rgba(51, 51, 51, 0.55)",
        data: data
      });
    }
}

function init_form(requestParams) {
    var daterange = requestParams?.daterange;
    var timeUnit = requestParams?.timeUnit;
    if ($('#year_selector').length){
        $('#year_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '80%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
                $('#daterange_input').val($('#year_selector').val());
            },
            nonSelectedText: '请选择年份',
            numberDisplayed: 10,
            enableFiltering: true,
            allSelectedText:'全部'
        });
        if (daterange && daterange.length) {
            $('#year_selector').multiselect('select', daterange);
            $('#daterange_input').val($('#year_selector').val());
        }
    }

    if ($('#dateunit_selector').length){
        $('#dateunit_selector').multiselect({
            buttonWidth: '80%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
            },
            nonSelectedText: '请选择',
            numberDisplayed: 1
        });
        if (timeUnit) {
            $('#dateunit_selector').multiselect('select', timeUnit);
            $('#instrument_input').val($('#instrument_selector').val());
        }
    }
    if ($('#instrument_selector').length) {
        $('#instrument_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '80%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
                $('#instrument_input').val($('#instrument_selector').val());
            },
            nonSelectedText: '请选择',
            numberDisplayed: 20,
            enableFiltering: true,
            allSelectedText:'全部'

        });
        if (barLabels && barLabels.length) {
            $('#instrument_selector').multiselect('select', barLabels);
            $('#instrument_input').val($('#instrument_selector').val());
        }
    }
    if ($('#project_selector').length) {
        $('#project_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '80%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
                $('#project_input').val($('#project_selector').val());
            },
            nonSelectedText: '请选择',
            numberDisplayed: 20,
            enableFiltering: true,
            allSelectedText:'全部'

        });
        if (barLabels && barLabels.length) {
            $('#project_selector').multiselect('select', barLabels);
            $('#project_input').val($('#project_selector').val());
        }
    }
    if ($('#creator_selector').length) {
        $('#creator_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '80%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
                $('#creator_input').val($('#creator_selector').val());
            },
            nonSelectedText: '请选择',
            numberDisplayed: 20,
            enableFiltering: true,
            allSelectedText:'全部'

        });
        if (barLabels && barLabels.length) {
            $('#creator_selector').multiselect('select', barLabels);
            $('#creator_input').val($('#creator_selector').val());
        }
    }
    var divName = requestParams?.instrumentNames?.length ? "instrument" : requestParams?.projectNames?.length ? "project" : requestParams?.creatorNames?.length ? "creator": "instrument";
    changeForm(divName);
}

function changeForm(divName) {
    let divIds = ["instrument", "project", "creator"];
    for (key in divIds) {
        if(divName && divIds[key] == divName) {
            $("#" + divIds[key] + "_selector_div").show();
        } else {
            $("#" + divIds[key] + "_selector_div").hide();
            $('#' + divIds[key] + '_selector').multiselect('deselectAll');
            $('#' + divIds[key] + '_input').val(null);
        }
    }
}
/*
    toggle search form
*/
function toggleSearchForm() {
    var $BOX_PANEL = $('#toggle_panel'),
        $ICON = $('#toggle_button'),
        $BOX_CONTENT = $('#toggle_form');
    if ($BOX_PANEL.attr('style')) {
        $BOX_CONTENT.slideToggle(200, function(){
            $BOX_PANEL.removeAttr('style');
        });
    } else {
        $BOX_CONTENT.slideToggle(200);
        $BOX_PANEL.css('height', 'auto');
    }

    $ICON.toggleClass('fa-chevron-up fa-chevron-down');
}



