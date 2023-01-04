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
      minDate: '01/01/2019',
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



