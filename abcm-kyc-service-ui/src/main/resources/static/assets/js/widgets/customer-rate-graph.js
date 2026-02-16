'use strict';

let kycChart; // <-- GLOBAL chart instance

function renderKycChart(counts) {
  const options = {
    chart: { type: 'area', height: 230, toolbar: { show: false } },
    colors: ['#0d6efd'],
    fill: {
      type: 'gradient',
      gradient: {
        shadeIntensity: 1,
        type: 'vertical',
        inverseColors: false,
        opacityFrom: 0.5,
        opacityTo: 0
      }
    },
    dataLabels: { enabled: false },
    stroke: { width: 1 },
    grid: { strokeDashArray: 4 },
    series: [{ name: 'KYC Count', data: counts }],
    xaxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
                   'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
      axisBorder: { show: false },
      axisTicks: { show: false }
    }
  };

  kycChart = new ApexCharts(document.querySelector('#customer-rate-graph'), options);
  kycChart.render();
}

