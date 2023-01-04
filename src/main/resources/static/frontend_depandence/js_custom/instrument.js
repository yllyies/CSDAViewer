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

function time() {
    var vWeek,vWeek_s,vDay;
    vWeek = ["星期天","星期一","星期二","星期三","星期四","星期五","星期六"];
    var date =  new Date();
    year = date.getFullYear();
    month = date.getMonth() + 1;
    day = date.getDate();
    vWeek_s = date.getDay();
    document.getElementById("clock").innerHTML = year + "年" + month + "月" + day + "日" + "\t" + date.toLocaleTimeString('chinese', {hour12:false}) + "\t" + vWeek[vWeek_s] ;
};

// 滚动方法
function autoScroll(father,children) {
    debugger
	let isreset = false,
		scrollItem = $(father), //需要上下移动内容的父元素
		scrollItemchildren = 500, //每次移动的距离
		scrollTimeTnterval = 3000,  //滚动间隔, 单位毫秒      必须大于下面的 滚动动画的持续时间(超过的多一点好)  !!!!!!  否则会越滚越慢 (  $(father).scrollTop() 会慢慢变小的BUG )
		scrollAnimateTime = 1500,  //滚动动画的持续时间, 毫秒
 		istoBottom = true,
 		scr
		innerHeight = $(father).innerHeight();
	function scrollFun() {
		if (isreset) {
			isreset = false;
			return
		}
		let a = scrollItem.scrollTop();
		if (istoBottom) {
		    console.log('向下滚动');
			scrollItem.animate({
				scrollTop: a + scrollItemchildren
			}, scrollAnimateTime,'linear'); //滚动动画时间
		}

		if (istoBottom) {
			if(scrollItem.scrollTop() + innerHeight >= $(father).prop("scrollHeight")){
				// 重新查询结果
                window.location.reload();
			}
		} else {
			if (scrollItem.scrollTop() <= 0) {
				istoBottom = true;
			}
		}
	}
	let sItval = setInterval(scrollFun, scrollTimeTnterval); //多久滚动一次
}
/* 异步查询仪器信息*/
function query() {
    $('#instrumentTable_tbody').load("/instrument/list2");
     /*$.ajax({
        url:"/instrument/list2",
        data: "value='test'" ,
        type:"GET",
        cache: false,
        success:function(result){
        debugger
            $("#instrumentTable_tbody").html(result);
        }
    });*/
}



