function init_charts_lf() {
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
          datasets: [{
            label: '# unit ',
            backgroundColor: "#10A1D2",
            data: dateset
          }]
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

    if ($('#lineChart1').length ){
      var ctx = document.getElementById("lineChart1");
      var lineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: "My First dataset",
//            backgroundColor: "rgba(38, 185, 154, 0.31)",
            borderColor: "#E4B83C",
            pointBorderColor: "#E4B83C",
            pointBackgroundColor: "rgba(220,220,220,1)",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointBorderWidth: 1,
            data: dateset
          }]
        },
      });

    }

    if ($('#canvasDoughnut1').length ){

      var ctx = document.getElementById("canvasDoughnut1");
      var data = {
        labels: labels,
        datasets: [{
          data: dateset,
          backgroundColor: [
            "#455C73",
            "#26B99A",
            "#3498DB"
          ],
          hoverBackgroundColor: [
            "#34495E",
            "#36CAAB",
            "#49A9EA"
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