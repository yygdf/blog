<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <div style="margin-left:auto">
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
        <el-select
          v-model="loginMethod"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择登录方式"
          clearable
        >
          <el-option
            v-for="item in options2"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="loginPlatform"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择登录平台"
          clearable
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-date-picker
          v-model="startTime"
          size="small"
          type="datetime"
          align="center"
          style="margin-right:1rem"
          placeholder="起始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          @change="getLoginLogs(true)"
        >
        </el-date-picker>
        <el-date-picker
          v-model="endTime"
          size="small"
          type="datetime"
          align="right"
          style="margin-right:1rem"
          placeholder="结束时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          @change="getLoginLogs(true)"
        >
        </el-date-picker>
      </div>
    </div>
    <el-table v-loading="loading" :data="loginLogList" border>
      <el-table-column
        prop="username"
        label="登录用户"
        align="center"
        width="160"
      />
      <el-table-column
        prop="loginMethod"
        label="登录方式"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag>
            {{ switchLoginMethod(scope.row.loginMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="loginPlatform"
        label="登录平台"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.loginPlatform ? "后台" : "前台" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="loginDevice" label="登录设备" align="center" />
      <el-table-column prop="loginSystem" label="操作系统" align="center" />
      <el-table-column prop="loginBrowser" label="浏览器类型" align="center" />
      <el-table-column
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
        prop="loginTime"
        label="登录时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :current-page.sync="current"
      :page-sizes="[10, 20]"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
  </el-card>
</template>

<script>
export default {
  created() {
    this.getLoginLogs();
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "前台"
        },
        {
          value: true,
          label: "后台"
        }
      ],
      options2: [
        {
          value: 1,
          label: "邮箱"
        },
        {
          value: 2,
          label: "QQ"
        },
        {
          value: 3,
          label: "微信"
        },
        {
          value: 4,
          label: "手机号"
        }
      ],
      pickerOptions: {
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            }
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            }
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            }
          }
        ]
      },
      loginLogList: [],
      usernameList: [],
      userId: null,
      endTime: null,
      optModule: null,
      startTime: null,
      loginMethod: null,
      loading: true,
      checkFlag: false,
      loginPlatform: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    sizeChange(size) {
      this.size = size;
      this.getLoginLogs(true);
    },
    currentChange(current) {
      this.current = current;
      this.getLoginLogs();
    },
    getLoginLogs(resetCurrentPage) {
      if (resetCurrentPage) {
        this.current = 1;
      }
      let params = {
        flag: this.loginPlatform,
        size: this.size,
        userId: this.userId,
        endTime: this.endTime,
        current: this.current,
        startTime: this.startTime,
        categoryId: this.loginMethod
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/loginLogs", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.loginLogList = data.data.pageList;
          this.loading = false;
        });
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
      this.getLoginLogs(true);
    },
    loginMethod() {
      this.getLoginLogs(true);
    },
    loginPlatform() {
      this.getLoginLogs(true);
    }
  },
  computed: {
    switchLoginMethod() {
      return function(type) {
        switch (type) {
          case 1:
            return "邮箱";
          case 2:
            return "QQ";
          case 3:
            return "微信";
          case 4:
            return "手机号";
        }
      };
    }
  }
};
</script>
