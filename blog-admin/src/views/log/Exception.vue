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
          :remote-method="listAllUsername"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :value="item.userId"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="optModule"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择模块"
          clearable
          filterable
        >
          <el-option
            v-for="item in moduleNameList"
            :key="item.label"
            :value="item.label"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="optType"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择操作类型"
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
          v-model="illegalFlag"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择异常类型"
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
          type="datetime"
          placeholder="起始时间"
          align="right"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
        <el-date-picker
          v-model="endTime"
          type="datetime"
          placeholder="结束时间"
          align="right"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
      </div>
    </div>
    <el-table v-loading="loading" :data="exceptionLogList" border>
      <el-table-column
        prop="username"
        label="操作人员"
        align="center"
        width="160"
      />
      <el-table-column
        prop="optType"
        label="操作类型"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag>
            {{ switchOptType(scope.row.optType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="optDesc"
        label="操作描述"
        align="center"
        width="160"
      />
      <el-table-column
        prop="optModule"
        label="操作模块"
        align="center"
        width="160"
      />
      <el-table-column
        prop="exceptionMessage"
        label="简略信息"
        align="center"
        width="160"
      />
      <el-table-column
        prop="ipAddress"
        label="登录ip"
        align="center"
        width="160"
      />
      <el-table-column
        prop="ipSource"
        label="登录地址"
        align="center"
        width="160"
      />
      <el-table-column
        prop="loginTime"
        label="发生时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="80">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="check(scope.row)">
            <i class="el-icon-view" /> 查看
          </el-button>
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
    <el-dialog :visible.sync="checkFlag" width="30%">
      <div class="dialog-title-container" slot="title">
        详细信息
      </div>
      <el-form size="medium" label-width="80">
        <el-form-item label="操作人员: ">
          {{ exceptionLog.username }}
        </el-form-item>
        <el-form-item label="操作路径: ">
          {{ exceptionLog.optUri }}
        </el-form-item>
        <el-form-item label="操作类型: ">
          <el-tag>
            {{ switchOptType(exceptionLog.optType) }}
          </el-tag>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listExceptionLogs();
    this.listAllModuleName();
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "普通异常"
        },
        {
          value: true,
          label: "非法请求"
        }
      ],
      options2: [
        {
          value: 1,
          label: "上传"
        },
        {
          value: 2,
          label: "删除"
        },
        {
          value: 3,
          label: "修改"
        },
        {
          value: 4,
          label: "查询"
        },
        {
          value: 5,
          label: "新增或修改"
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
      exceptionLog: {},
      usernameList: [],
      moduleNameList: [],
      exceptionLogList: [],
      userId: null,
      endTime: null,
      optType: null,
      optModule: null,
      startTime: null,
      loading: true,
      checkFlag: false,
      illegalFlag: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    check(exceptionLog) {
      this.exceptionLog = JSON.parse(JSON.stringify(exceptionLog));
      this.checkFlag = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listExceptionLogs(true);
    },
    currentChange(current) {
      this.current = current;
      this.listExceptionLogs();
    },
    listExceptionLogs(resetPageFlag = false) {
      if (resetPageFlag) {
        this.current = 1;
      }
      this.axios
        .get("/api/back/exceptionLogs", {
          params: {
            size: this.size,
            userId: this.userId,
            endTime: this.endTime,
            current: this.current,
            keywords: this.optModule,
            startTime: this.startTime,
            categoryId: this.optType,
            deletedFlag: this.illegalFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.exceptionLogList = data.data.pageList;
          this.loading = false;
        });
    },
    listAllModuleName() {
      this.axios.get("/api/back/resource/moduleNames").then(({ data }) => {
        this.moduleNameList = data.data;
      });
    },
    listAllUsername(keywords) {
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
      this.listExceptionLogs(true);
    },
    endTime() {
      this.listExceptionLogs(true);
    },
    optType() {
      this.listExceptionLogs(true);
    },
    optModule() {
      this.listExceptionLogs(true);
    },
    startTime() {
      this.listExceptionLogs(true);
    },
    illegalFlag() {
      this.listExceptionLogs(true);
    }
  },
  computed: {
    switchOptType() {
      return function(type) {
        switch (type) {
          case 1:
            return "上传";
          case 2:
            return "删除";
          case 3:
            return "修改";
          case 4:
            return "查询";
          case 5:
            return "新增或修改";
        }
      };
    }
  }
};
</script>
