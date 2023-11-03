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
          v-model="optModule"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择模块"
          clearable
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
          placeholder="请选择类型"
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
          size="small"
          type="datetime"
          align="center"
          style="margin-right:1rem"
          placeholder="起始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions"
          @change="getExceptionLogs(true)"
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
          @change="getExceptionLogs(true)"
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
        prop="optModule"
        label="操作模块"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.optModule }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="optType"
        label="操作类型"
        align="center"
        width="120"
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
        width="200"
      />
      <el-table-column prop="exceptionMessage" label="简略信息" align="center">
        <template slot-scope="scope">
          {{ scope.row.exceptionMessage | subStr }}
        </template>
      </el-table-column>
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
        prop="createTime"
        label="发生时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120">
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
    <el-dialog :visible.sync="checkFlag" width="60%">
      <div class="dialog-title-container" slot="title">
        详细信息
      </div>
      <el-form size="medium" label-width="80">
        <el-form-item label="操作人员: ">
          {{ exceptionLog.username }}
        </el-form-item>
        <el-form-item label="操作模块: ">
          <el-tag>
            {{ exceptionLog.optModule }}
          </el-tag>
        </el-form-item>
        <el-form-item label="操作类型: ">
          <el-tag>
            {{ switchOptType(exceptionLog.optType) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="操作路径: ">
          {{ exceptionLog.optUri }}
        </el-form-item>
        <el-form-item label="操作方法: ">
          {{ exceptionLog.optMethod }}
        </el-form-item>
        <el-form-item label="操作描述: ">
          {{ exceptionLog.optDesc }}
        </el-form-item>
        <el-form-item label="ip地址: ">
          {{ exceptionLog.ipAddress }}
        </el-form-item>
        <el-form-item label="ip来源: ">
          {{ exceptionLog.ipSource }}
        </el-form-item>
        <el-form-item label="发生时间: ">
          {{ exceptionLog.createTime | dateTime }}
        </el-form-item>
        <el-form-item label="请求参数: " style="white-space: pre-line">
          {{ exceptionLog.requestParam }}
        </el-form-item>
        <el-form-item label="简略信息: " style="white-space: pre-line">
          {{ exceptionLog.exceptionMessage }}
        </el-form-item>
        <el-form-item label="详细信息: " style="white-space: pre-line">
          {{ exceptionLog.exceptionStackTrace }}
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getExceptionLogs();
    this.getModuleNames();
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
      this.getExceptionLogs(true);
    },
    currentChange(current) {
      this.current = current;
      this.getExceptionLogs();
    },
    getExceptionLogs(resetCurrentPage = false) {
      if (resetCurrentPage) {
        this.current = 1;
      }
      let params = {
        flag: this.illegalFlag,
        size: this.size,
        userId: this.userId,
        endTime: this.endTime,
        current: this.current,
        keywords: this.optModule,
        startTime: this.startTime,
        categoryId: this.optType
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/exceptionLogs", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.exceptionLogList = data.data.pageList;
          this.loading = false;
        });
    },
    getModuleNames() {
      this.axios.get("/api/back/resource/moduleNames").then(({ data }) => {
        this.moduleNameList = data.data;
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
      this.getExceptionLogs(true);
    },
    optType() {
      this.getExceptionLogs(true);
    },
    optModule() {
      this.getExceptionLogs(true);
    },
    illegalFlag() {
      this.getExceptionLogs(true);
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
