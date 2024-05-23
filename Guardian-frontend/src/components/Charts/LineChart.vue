<template>
  <div>
    <div ref="chart" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      chart: null,
      chartData: {
        TotalIn: [],
        TotalOut: []
      },
      timeLabels: []
    };
  },
  mounted() {
    this.initChart();
    this.fetchData();
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart);
      this.chart.setOption({
        title: {
          text: ' 上传带宽 vs 下载带宽'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['上传带宽', '下载带宽']
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.timeLabels
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '上传带宽',
            type: 'line',
            data: this.chartData.TotalIn
          },
          {
            name: '下载带宽',
            type: 'line',
            data: this.chartData.TotalOut
          }
        ]
      });
    },
    fetchData() {
      this.request.get('/ipfs/getNet') // 修改为后端 API 地址
          .then(response => {
            const data = response.data;
            const jsonData = JSON.parse(data);
            this.chartData.TotalIn.push(jsonData.TotalIn); // 将 TotalIn 数据添加到数组中
            this.chartData.TotalOut.push(jsonData.TotalOut); // 将 TotalOut 数据添加到数组中
            this.timeLabels.push(jsonData.time); // 添加当前时间作为时间标签
            this.updateChart();
            setTimeout(this.fetchData, 2000); // 每秒轮询一次
          })
          .catch(error => {
            console.error('Error fetching data:', error);
          });
    },
    updateChart() {
      this.chart.setOption({
        xAxis: {
          data: this.timeLabels
        },
        series: [
          {
            name: '上传带宽',
            data: this.chartData.TotalIn
          },
          {
            name: '下载带宽',
            data: this.chartData.TotalOut
          }
        ]
      });
    }
  }
};
</script>

<style scoped>
/* 添加一些样式以确保图表大小合适 */
</style>
