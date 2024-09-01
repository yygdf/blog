<template>
  <div>
    <el-card v-if="checkWeight(300)">
      <el-select
        v-model="userId"
        size="small"
        style="margin-right:1rem"
        placeholder="请选择用户"
        remote
        clearable
        filterable
        :remote-method="getUsernames"
      >
        <el-option
          v-for="item in usernameList"
          :key="item.id"
          :value="item.id"
          :label="item.label"
        />
      </el-select>
    </el-card>
    <el-card style="margin-top:1.25rem">
      <el-date-picker
        v-model="dateRange"
        size="small"
        type="daterange"
        align="right"
        range-separator="至"
        value-format="yyyy-MM-dd"
        style="margin-top:-0.5rem;float: right"
        end-placeholder="结束日期"
        start-placeholder="开始日期"
        :picker-options="pickerOptions"
        @change="getData()"
        unlink-panels
      >
      </el-date-picker>
      <div class="e-title">阅读量趋势</div>
      <div style="height:300px">
        <v-chart :options="viewCount" v-loading="loading" />
      </div>
    </el-card>
    <el-row :gutter="30" style="margin-top:1.25rem">
      <el-col :span="12">
        <el-card>
          <div class="e-title">阅读量排行</div>
          <div style="height:300px">
            <v-chart :options="viewCountRank" v-loading="loading" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div class="e-title">点赞量排行</div>
          <div style="height:300px">
            <v-chart :options="likeCountRank" v-loading="loading" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  created() {
    this.getData();
  },
  data: function() {
    return {
      usernameList: [],
      userId: null,
      dateRange: null,
      loading: true,
      viewCount: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          }
        },
        color: ["#3888fa"],
        legend: {
          data: ["阅读量"]
        },
        grid: {
          left: "0%",
          right: "0%",
          bottom: "0%",
          top: "10%",
          containLabel: true
        },
        xAxis: {
          data: [],
          axisLine: {
            lineStyle: {
              color: "#666"
            }
          }
        },
        yAxis: {
          axisLine: {
            lineStyle: {
              color: "#048CCE"
            }
          }
        },
        series: [
          {
            name: "阅读量",
            type: "line",
            data: [],
            smooth: true
          }
        ]
      },
      viewCountRank: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          }
        },
        color: ["#58AFFF"],
        grid: {
          left: "0%",
          right: "0%",
          bottom: "0%",
          top: "10%",
          containLabel: true
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [
          {
            name: ["浏览量"],
            type: "bar",
            data: [],
            itemStyle: {
              normal: {
                color: function() {
                  return (
                    "#" +
                    Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(
                      16
                    )
                  );
                }
              }
            }
          }
        ]
      },
      likeCountRank: {
        color: [
          "#7EC0EE",
          "#FF9F7F",
          "#FFD700",
          "#C9C9C9",
          "#E066FF",
          "#C0FF3E"
        ],
        legend: {
          data: [],
          bottom: "bottom"
        },
        tooltip: {
          trigger: "item"
        },
        series: [
          {
            name: "文章分类",
            type: "pie",
            data: []
          }
        ]
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近半个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 14);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 29);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      }
    };
  },
  methods: {
    getData() {
      let params = {};
      if (this.dateRange != null) {
        params.endDate = this.dateRange[1] + " 00:00:00";
        params.days =
          Math.floor(
            (Date.parse(this.dateRange[1]) - Date.parse(this.dateRange[0])) /
              86400000
          ) + 1;
      }
      if (this.userId != null) {
        params.userId = this.userId;
      }
      this.axios
        .get("/api/back/blog/articleStatistic", {
          params
        })
        .then(({ data }) => {
          if (data.data.viewCount != null) {
            this.viewCount.xAxis.data = [];
            this.viewCount.series[0].data = [];
            data.data.viewCount.forEach(item => {
              this.viewCount.xAxis.data.push(item.name);
              this.viewCount.series[0].data.push(item.value);
            });
          }
          if (data.data.viewCountRankDTOList != null) {
            data.data.viewCountRankDTOList.forEach(item => {
              this.viewCountRank.series[0].data.push(item.viewsCount);
              this.viewCountRank.xAxis.data.push(item.articleTitle);
            });
          }
          if (data.data.likeCountRankDTOList != null) {
            data.data.likeCountRankDTOList.forEach(item => {
              this.likeCountRank.series[0].data.push({
                value: item.articleCount,
                name: item.likeCountRankName
              });
              this.likeCountRank.legend.data.push(item.likeCountRankName);
            });
          }
          this.loading = false;
        });
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    getUsernames(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    }
  },
  watch: {
    userId() {
      this.getData();
    }
  }
};
</script>

<style scoped>
.card-icon-container {
  display: inline-block;
  font-size: 3rem;
}
.card-desc {
  font-weight: bold;
  float: right;
}
.card-title {
  margin-top: 0.3rem;
  line-height: 18px;
  color: rgba(0, 0, 0, 0.45);
  font-size: 1rem;
}
.card-count {
  margin-top: 0.75rem;
  color: #666;
  font-size: 1.25rem;
}
.echarts {
  width: 100%;
  height: 100%;
}
.e-title {
  font-size: 13px;
  color: #202a34;
  font-weight: 700;
}
</style>
