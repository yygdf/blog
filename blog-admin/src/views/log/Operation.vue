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
          placeholder="请选择类型"
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
          @change="getOperationLogs(true)"
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
          @change="getOperationLogs(true)"
        >
        </el-date-picker>
      </div>
    </div>
    <el-table v-loading="loading" :data="operationLogList" border>
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        label="操作人员"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.optModule"
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
        v-if="showColumnConfig.optType"
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
        v-if="showColumnConfig.optDesc"
        prop="optDesc"
        label="操作描述"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.optMethod"
        prop="optMethod"
        label="操作方法"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.ipAddress"
        prop="ipAddress"
        label="ip地址"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.ipSource"
        prop="ipSource"
        label="ip来源"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        label="操作时间"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >操作人员</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.optModule"
                >操作模块</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.optType"
                >操作类型</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.optDesc"
                >操作描述</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.optMethod"
                >操作方法</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipAddress"
                >ip地址</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipSource"
                >ip来源</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.createTime"
                >操作时间</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  保存
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
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
          {{ operationLog.username }}
        </el-form-item>
        <el-form-item label="操作模块: ">
          <el-tag>
            {{ operationLog.optModule }}
          </el-tag>
        </el-form-item>
        <el-form-item label="操作类型: ">
          <el-tag>
            {{ switchOptType(operationLog.optType) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="操作路径: ">
          {{ operationLog.optUri }}
        </el-form-item>
        <el-form-item label="操作方法: ">
          {{ operationLog.optMethod }}
        </el-form-item>
        <el-form-item label="操作描述: ">
          {{ operationLog.optDesc }}
        </el-form-item>
        <el-form-item label="ip地址: ">
          {{ operationLog.ipAddress }}
        </el-form-item>
        <el-form-item label="ip来源: ">
          {{ operationLog.ipSource }}
        </el-form-item>
        <el-form-item label="操作时间: ">
          {{ operationLog.createTime | dateTime }}
        </el-form-item>
        <el-form-item label="请求参数: " style="white-space: pre-line">
          {{ operationLog.requestParam }}
        </el-form-item>
        <el-form-item label="响应数据: " style="white-space: pre-line">
          {{ operationLog.responseData }}
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.loadColumnConfig();
    this.getModuleNames();
    this.getOperationLogs();
  },
  data: function() {
    return {
      options: [
        {
          value: 1,
          label: "新增"
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
      usernameList: [],
      moduleNameList: [],
      operationLogList: [],
      operationLog: {},
      showColumnConfig: {},
      userId: null,
      endTime: null,
      optType: null,
      optModule: null,
      startTime: null,
      loading: true,
      checkFlag: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    check(operationLog) {
      this.operationLog = JSON.parse(JSON.stringify(operationLog));
      this.checkFlag = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getOperationLogs(true);
    },
    currentChange(current) {
      this.current = current;
      this.getOperationLogs();
    },
    saveColumnConfig() {
      localStorage.setItem(
        "OperationColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("OperationColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("OperationColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          optModule: true,
          optType: true,
          optDesc: true,
          optMethod: true,
          ipAddress: true,
          ipSource: true,
          createTime: true
        };
      }
    },
    getOperationLogs(resetCurrentPage) {
      if (resetCurrentPage) {
        this.current = 1;
      }
      let params = {
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
        .get("/api/back/operationLogs", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.operationLogList = data.data.pageList;
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
      this.getOperationLogs(true);
    },
    optType() {
      this.getOperationLogs(true);
    },
    optModule() {
      this.getOperationLogs(true);
    }
  },
  computed: {
    switchOptType() {
      return function(type) {
        switch (type) {
          case 1:
            return "新增";
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
