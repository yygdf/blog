<template>
  <div>
    <el-card v-if="checkWeight(300)">
      <el-select
        v-model="userId"
        size="small"
        style="margin-right:1rem"
        :placeholder="$t('input.selectUser')"
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
        :range-separator="$t('input.separator')"
        value-format="yyyy-MM-dd"
        style="margin-top:-0.5rem;float: right"
        :end-placeholder="$t('input.endDate')"
        :start-placeholder="$t('input.startDate')"
        :picker-options="pickerOptions"
        @change="getData()"
        unlink-panels
      >
      </el-date-picker>
      <div class="e-title">{{ $t("statistic.viewCountTrend") }}</div>
      <div style="height:300px">
        <v-chart :options="viewCount" v-loading="loading" />
      </div>
    </el-card>
    <el-row :gutter="30" style="margin-top:1.25rem">
      <el-col :span="12">
        <el-card>
          <div class="e-title">{{ $t("statistic.viewCountRank") }}</div>
          <div style="height:300px">
            <v-chart :options="viewCountRank" v-loading="loading" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div class="e-title">{{ $t("statistic.likeCountRank") }}</div>
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
          data: []
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
            name: [],
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
            name: [],
            type: "bar",
            data: [],
            barWidth: 50,
            itemStyle: {
              color: function(params) {
                const colorMap = [
                  "#FF0000",
                  "#FFA500",
                  "#FFFF00",
                  "#008000",
                  "#00FFFF",
                  "#0000FF",
                  "#800080",
                  "#C0FF3E",
                  "#FFC0CB",
                  "#FF9F7F"
                ];
                return colorMap[params.dataIndex];
              }
            }
          }
        ]
      },
      likeCountRank: {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross"
          }
        },
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
            name: [""],
            type: "bar",
            data: [],
            barWidth: 50,
            itemStyle: {
              color: function(params) {
                const colorMap = [
                  "#FF0000",
                  "#FFA500",
                  "#FFFF00",
                  "#008000",
                  "#00FFFF",
                  "#0000FF",
                  "#800080",
                  "#C0FF3E",
                  "#FFC0CB",
                  "#FF9F7F"
                ];
                return colorMap[params.dataIndex];
              }
            }
          }
        ]
      },
      pickerOptions: {
        shortcuts: [
          {
            text: "",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 14);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "",
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
      if (this.userId != null && this.userId !== "") {
        params.userId = this.userId;
      }
      this.axios
        .get("/api/back/blog/articleStatistic", {
          params
        })
        .then(({ data }) => {
          if (data.data.viewCountDTOList != null) {
            this.viewCount.xAxis.data = [];
            this.viewCount.series[0].data = [];
            data.data.viewCountDTOList.forEach(item => {
              this.viewCount.xAxis.data.push(item.name);
              this.viewCount.series[0].data.push(item.value);
            });
          }
          if (data.data.viewCountRankDTOList != null) {
            this.viewCountRank.xAxis.data = [];
            this.viewCountRank.series[0].data = [];
            data.data.viewCountRankDTOList.forEach(item => {
              this.viewCountRank.xAxis.data.push(item.name);
              this.viewCountRank.series[0].data.push(item.value);
            });
          }
          if (data.data.likeCountRankDTOList != null) {
            this.likeCountRank.xAxis.data = [];
            this.likeCountRank.series[0].data = [];
            data.data.likeCountRankDTOList.forEach(item => {
              this.likeCountRank.xAxis.data.push(item.name);
              this.likeCountRank.series[0].data.push(item.value);
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
    },
    "$i18n.locale": {
      handler() {
        this.pickerOptions.shortcuts[0].text = this.$t("input.lastWeek");
        this.pickerOptions.shortcuts[1].text = this.$t("input.lastHalfMonth");
        this.pickerOptions.shortcuts[2].text = this.$t("input.lastMonth");
        this.viewCount.legend.data = [this.$t("statistic.viewCount")];
        this.viewCount.series[0].name = [this.$t("statistic.viewCount")];
        this.viewCountRank.series[0].name = [this.$t("statistic.viewCount")];
        this.likeCountRank.series[0].name = [this.$t("statistic.likeCount")];
      },
      immediate: true
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
