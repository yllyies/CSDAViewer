function toggleFullscreen() {
    if (document.fullscreen) {
        document.exitFullscreen();
        var obj = document.getElementById('datatable-div');
//        obj.style.height = "400px";
    } else {
        var obj2 = document.getElementById('fullScreenPanel');
        obj2.requestFullscreen();
        var obj = document.getElementById('datatable-div');
//        obj.style.height = "calc(80vh)";
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
function autoScroll(eleId,childId) {
	let pause = false,
		scrollItem = $('#' + eleId), //需要上下移动内容的父元素
		ele = document.getElementById(eleId),
		scrollItemchildren = $('#' + childId).height() * 10, //每次移动的距离
		scrollTimeTnterval = 30000,  //滚动间隔, 单位毫秒      必须大于下面的 滚动动画的持续时间(超过的多一点好)  !!!!!!  否则会越滚越慢 (  $(eleId).scrollTop() 会慢慢变小的BUG )
		scrollAnimateTime = 1000,  //滚动动画的持续时间, 毫秒
		innerHeight = $(eleId).innerHeight();
	window.scrollCount = Math.round(ele.scrollHeight / scrollItemchildren);
	function scrollFun() {
        if (scrollCount == 0){
            window.location.reload();
        }
        if (pause) {
            return
        }
        scrollItem.animate({
            scrollTop: scrollItem.scrollTop() + scrollItemchildren
        }, scrollAnimateTime,'linear', function(){
            window.scrollCount --;
        }); //滚动动画时间

	}
	let sItval = setInterval(scrollFun, scrollTimeTnterval); //多久滚动一次
	ele.onmouseover = function() {
	    clearInterval(sItval);
	}
	ele.onmouseleave = function() {
        autoScroll('datatable-div', 'datatable-tr');
    }
}
