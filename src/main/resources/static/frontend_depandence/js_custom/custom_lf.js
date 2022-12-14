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

function init_DataTables() {
    /*$('#datatable-custom').dataTable({
      autoWidth : true,
      scrollCollapse: false,
      pagingType: "simple",
      paging: false,
      pageLength: 12,
      info: true,
      searching: false,
      ordering: false,
      fixedColumns: true,
      "lengthChange": false
    });
    $('#datatable-custom2').dataTable({
      autoWidth : true,
      scrollCollapse: false,
      pagingType: "simple",
      paging: false,
      pageLength: 12,
      searching: false,
      ordering: false,
      "lengthChange": false
    });*/
};

function time() {
    var vWeek,vWeek_s,vDay;
    vWeek = ["星期天","星期一","星期二","星期三","星期四","星期五","星期六"];
    var date =  new Date();
    year = date.getFullYear();
    month = date.getMonth() + 1;
    day = date.getDate();
    hours = date.getHours();
    minutes = date.getMinutes();
    seconds = date.getSeconds();
    vWeek_s = date.getDay();
    document.getElementById("clock").innerHTML = year + "年" + month + "月" + day + "日" + "\t" + hours + ":" + minutes +":" + seconds + "\t" + vWeek[vWeek_s] ;
};

function clock() {
    time()
    return setInterval("time()", 1000);
}

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

function init_form(daterange, timeUnit, barLabels) {
    if ($('#year_selector').length){
        $('#year_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '100%',
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
            buttonWidth: '50%',
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
        if (barLabels && barLabels.length) {
            $('#instrument_selector').multiselect('select', barLabels);
            $('#instrument_input').val($('#instrument_selector').val());
        }
    }
    if ($('#project_selector').length) {
        $('#project_selector').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '100%',
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
            buttonWidth: '100%',
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
}

function toggleFullscreen() {
    if (document.fullscreen) {
        document.exitFullscreen();
        var obj = document.getElementById('datatable-div');
        obj.style.height = "500px";
    } else {
        var obj2 = document.getElementById('fullScreenPanel');
        obj2.requestFullscreen();
        var obj = document.getElementById('datatable-div');
        obj.style.height = "calc(80vh)";fullscreen
    }
}

// 滚动方法
function autoScroll(father,children) {
	let isreset = false,
		scrollItem = $(father), //需要上下移动内容的父元素
		scrollItemchildren = 500, //每次移动的距离
		scrollTimeTnterval = 5000,  //滚动间隔, 单位毫秒      必须大于下面的 滚动动画的持续时间(超过的多一点好)  !!!!!!  否则会越滚越慢 (  $(father).scrollTop() 会慢慢变小的BUG )
		scrollAnimateTime = 500,  //滚动动画的持续时间, 毫秒
 		istoBottom = true,
		innerHeight = $(father).innerHeight();
	function time() {
		if (isreset) {
			isreset = false;
			return
		}
		let a = scrollItem.scrollTop();
		// console.log(a,scrollItemchildren,scroll,istoBottom)
		if (istoBottom) {
			scrollItem.animate({
				scrollTop: a + scrollItemchildren
			}, scrollAnimateTime,'linear'); //滚动动画时间
		} else {
			scrollItem.animate({
				scrollTop: a - scrollItemchildren
			}, scrollAnimateTime,'linear'); //滚动动画时间
		}

		if (istoBottom) {
			if(scrollItem.scrollTop() + innerHeight >= $(father).prop("scrollHeight")){
				// console.log('到底了',scrollItem.scrollTop())
				isreset = true;
				istoBottom = false;
			}
		} else {
			if (scrollItem.scrollTop() <= 0) {
				// console.log('到顶了',scrollItem.scrollTop())
				istoBottom = true;
			}
		}
	}
	let sItval = setInterval(time, scrollTimeTnterval); //多久滚动一次
}


function changeForm(divName) {
    let divIds = ["instrument", "project", "creator"];
    for (key in divIds) {
        if(divName && divIds[key] == divName) {
            $("#" + divIds[key] + "_selector_div").show();
        } else {
            $("#" + divIds[key] + "_selector_div").hide();
            $('#' + divIds[key] + '_selector').multiselect('deselectAll');
        }
    }
}




