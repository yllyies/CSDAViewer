function init_charts_lf() {

    console.log('run_charts  typeof [' + typeof (Chart) + ']');

    if( typeof (Chart) === 'undefined'){ return; }

    console.log('init_charts_lf');


    Chart.defaults.global.legend = {
        enabled: false
    };
    // Bar chart
    if ($('#mybarChart1').length ){

      var ctx = document.getElementById("mybarChart1");
      var mybarChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ["4/1/20", "4/10/20", "4/20/20", "5/1/20", "5/10/20", "5/20/20", "6/1/20", "6/10/20"],
          datasets: [{
            label: '# unit ',
            backgroundColor: "#10A1D2",
            data: [1.30, 2.20, 2.5, 2.7, 2.6, 2.7, 2.6, 3.75]
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
          labels: ["4/1/20", "4/10/20", "4/20/20", "5/1/20", "5/10/20", "5/20/20", "6/1/20", "6/10/20"],
          datasets: [{
            label: "My First dataset",
//            backgroundColor: "rgba(38, 185, 154, 0.31)",
            borderColor: "#E4B83C",
            pointBorderColor: "#E4B83C",
            pointBackgroundColor: "rgba(220,220,220,1)",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgba(220,220,220,1)",
            pointBorderWidth: 1,
            data: [1.30, 2.20, 2.5, 2.7, 2.6, 2.7, 2.6, 3.75]
          }]
        },
      });

    }

    if ($('#canvasDoughnut1').length ){

      var ctx = document.getElementById("canvasDoughnut1");
      var data = {
        labels: [
          "Dark Grey",
          "Green Color",
          "Blue Color"
        ],
        datasets: [{
          data: [13, 22, 65],
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