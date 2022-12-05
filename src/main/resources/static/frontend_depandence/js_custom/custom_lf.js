function init_charts() {
    if( typeof (Chart) === 'undefined'){ return; }
    Chart.defaults.global.legend = {
        enabled: false
    };

    // Bar chart
    if ($('#mybarChart1').length ){
      var ctx = document.getElementById("mybarChart1");
      var mybarChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: datasets
        },
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true
              }
            }]
          }
        }
      });
    }
    // 线图：x轴为时间区间
    if ($('#lineChart1').length ){
      var ctx = document.getElementById("lineChart1");
      var lineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: "运行时间(s)",
//            backgroundColor: "rgba(38, 185, 154, 0.31)",
            borderColor: "#E4B83C",
            pointBorderColor: "#E4B83C",
            pointBackgroundColor: "rgba(220,220,220,1)",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointBorderWidth: 1,
            data: datasets
          }]
        },
      });

    }

    if ($('#canvasDoughnut1').length ){

      var ctx = document.getElementById("canvasDoughnut1");
      var data = {
        labels: labels,
        datasets: [{
          data: datasets,
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

function init_DataTables() {
    $('#datatable').dataTable();
    $('#datatable-custom').dataTable({
      autoWidth : true,
//      fixedHeader: false,
//      deferRender: true,
      scrollY: 380,
      scrollCollapse: false,
      scroller: true
    });
};

/* DATERANGEPICKER */

function init_daterangepicker() {

    if( typeof ($.fn.daterangepicker) === 'undefined'){ return; }
    console.log('init_daterangepicker_lf');

    var cb = function(start, end, label) {
      console.log(start.toISOString(), end.toISOString(), label);
      $('#reportrange1 span').html(start.format('YYYY年MM月DD日') + ' - ' + end.format('YYYY年MM月DD日'));
    };

    var optionSet1 = {
      startDate: moment().subtract(29, 'days'),
      endDate: moment(),
      minDate: '01/01/2020',
      maxDate: '01/01/2023',
      dateLimit: {
        days: 60
      },
      showDropdowns: true,
      showWeekNumbers: true,
      timePicker: false,
      timePickerIncrement: 1,
      timePicker12Hour: true,
      /*ranges: {
        'Today': [moment(), moment()],
        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
      },*/
      opens: 'left',
      buttonClasses: ['btn btn-default'],
      applyClass: 'btn-small btn-primary',
      cancelClass: 'btn-small',
      format: 'YYYY年MM月DD日',
      separator: ' to ',
      locale: {
        applyLabel: '确定',
        cancelLabel: '清除',
        fromLabel: 'From',
        toLabel: 'To',
        customRangeLabel: 'Custom',
        daysOfWeek: ['周一', '周二', '周三', '周四', '周五', '周六', '周天'],
        monthNames: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
        firstDay: 1
      }
    };

    $('#reportrange1 span').html(moment().subtract(29, 'days').format('YYYY年MM月DD日') + ' - ' + moment().format('YYYY年MM月DD日'));
    $('#reportrange1').daterangepicker(optionSet1, cb);
    $('#reportrange1').on('show.daterangepicker', function() {
      console.log("show event fired");
    });
    $('#reportrange1').on('hide.daterangepicker', function() {
      console.log("hide event fired");
    });
    $('#reportrange1').on('apply.daterangepicker', function(ev, picker) {
      $('#startDate').val(picker.endDate.format('YYYY-MM-DD'));
      $('#endDate').val(picker.startDate.format('YYYY-MM-DD'));
    });
    $('#reportrange1').on('cancel.daterangepicker', function(ev, picker) {
      console.log("cancel event fired");
    });
    $('#options1').click(function() {
      $('#reportrange1').data('daterangepicker').setOptions(optionSet1, cb);
    });
    $('#options2').click(function() {
      $('#reportrange1').data('daterangepicker').setOptions(optionSet2, cb);
    });
    $('#destroy').click(function() {
      $('#reportrange1').data('daterangepicker').remove();
    });
}

function init_instrumentSelector() {
    $('#dateunit_selector').multiselect({
//            enableClickableOptGroups: true,
//            enableCollapsibleOptGroups: true,
//            includeSelectAllOption: true,
            buttonWidth: '50%',
            dropRight: true,
            maxHeight: 300,
            onChange: function(option, checked) {
                if (option.val()=="YEAR") {
                    $('#daterange_selector_year_div').show();
                    $('#daterange_selector_quarter_div').hide();
                    $('#daterange_selector_month_div').hide();
                } else if (option.val()=="QUARTER") {
                    $('#daterange_selector_year_div').hide();
                    $('#daterange_selector_quarter_div').show();
                    $('#daterange_selector_month_div').hide();
                } else if (option.val()=="MONTH") {
                    $('#daterange_selector_year_div').hide();
                    $('#daterange_selector_quarter_div').hide();
                    $('#daterange_selector_month_div').show();
                }
            },
            nonSelectedText: '请选择',
            numberDisplayed: 1,
//            enableFiltering: true,
//            allSelectedText:'全部'
        });
    $('#daterange_selector_month').multiselect({
        enableClickableOptGroups: true,
        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        buttonWidth: '100%',
        dropRight: true,
        maxHeight: 300,
        onChange: function(option, checked) {
            $('#daterange_input').val($('#daterange_selector_month').val());
        },
        nonSelectedText: '请选择月份',
        numberDisplayed: 10,
        enableFiltering: true,
        allSelectedText:'全部'
    });
    $('#daterange_selector_quarter').multiselect({
        enableClickableOptGroups: true,
        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        buttonWidth: '100%',
        dropRight: true,
        maxHeight: 300,
        onChange: function(option, checked) {
            $('#daterange_input').val($('#daterange_selector_quarter').val());
        },
        nonSelectedText: '请选择季度',
        numberDisplayed: 10,
        enableFiltering: true,
        allSelectedText:'全部'
    });
    $('#daterange_selector_year').multiselect({
//        enableClickableOptGroups: true,
//        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        buttonWidth: '100%',
        dropRight: true,
        maxHeight: 300,
        onChange: function(option, checked) {
            $('#daterange_input').val($('#daterange_selector_year').val());
        },
        nonSelectedText: '请选择年份',
        numberDisplayed: 10,
        enableFiltering: true,
        allSelectedText:'全部'
    });
    $('#daterange_selector_year_div').hide();
    $('#daterange_selector_quarter_div').show();
    $('#daterange_selector_month_div').hide();

    $('#instrument_selector').multiselect({
//        enableClickableOptGroups: true,
//        enableCollapsibleOptGroups: true,
        includeSelectAllOption: true,
        buttonWidth: '100%',
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
}

$(document).ready(function() {
    init_daterangepicker();
    init_charts();
    init_DataTables();
    init_instrumentSelector();
});