<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <div style="margin-left:auto">
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
        <el-select
          v-model="optModule"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectModule')"
          clearable
          filterable
        >
          <el-option
            v-for="item in moduleNameList"
            :key="item.label"
            :value="item.label"
            :label="isEn ? item.label2 : item.label"
          />
        </el-select>
        <el-select
          v-model="optType"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectType')"
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
          :placeholder="$t('input.startTime')"
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
          :placeholder="$t('input.endTime')"
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
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.optModule"
        prop="optModule"
        :label="$t('table.optModule')"
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
        :label="$t('table.optType')"
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
        v-if="showColumnConfig.optDesc"
        prop="optDesc"
        :label="$t('table.optDesc')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.optMethod"
        prop="optMethod"
        :label="$t('table.optMethod')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.ipAddress"
        prop="ipAddress"
        :label="$t('table.ipAddress')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.ipSource"
        prop="ipSource"
        :label="$t('table.ipSource')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        :label="$t('table.createTime')"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        :label="$t('table.operate')"
        align="center"
        width="120"
      >
        <template slot="header">
          <el-popover placement="bottom" width="160">
            <div>
              <el-checkbox
                :indeterminate="
                  columnCheckedCount > 0 && columnCheckedCount < columnCount
                "
                :value="columnCheckedCount === columnCount"
                @change="handleColumnCheckedAllChange"
                >{{ $t("table.showColumn") }}</el-checkbox
              >
              <el-divider></el-divider>
              <el-checkbox
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.optModule"
                @change="handleColumnCheckedChange"
                >{{ $t("table.optModule") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.optType"
                @change="handleColumnCheckedChange"
                >{{ $t("table.optType") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.optDesc"
                @change="handleColumnCheckedChange"
                >{{ $t("table.optDesc") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.optMethod"
                @change="handleColumnCheckedChange"
                >{{ $t("table.optMethod") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.ipAddress"
                @change="handleColumnCheckedChange"
                >{{ $t("table.ipAddress") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.ipSource"
                @change="handleColumnCheckedChange"
                >{{ $t("table.ipSource") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.createTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.createTime") }}</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  {{ $t("button.save") }}
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
          <el-tooltip
            class="item"
            effect="dark"
            :content="$t('table.refresh')"
            placement="top"
          >
            <i
              class="el-icon-refresh table-refresh-icon"
              @click="getOperationLogs(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="check(scope.row)">
            <i class="el-icon-view" /> {{ $t("button.view") }}
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
    <el-dialog :visible.sync="checkFlag" width="40%">
      <div class="dialog-title-container" slot="title">
        {{ $t("exception.detail") }}
      </div>
      <el-form size="medium" label-width="120px">
        <el-form-item :label="$t('table.user')">
          {{ operationLog.username }}
        </el-form-item>
        <el-form-item :label="$t('table.optModule')">
          <el-tag>
            {{ operationLog.optModule }}
          </el-tag>
        </el-form-item>
        <el-form-item :label="$t('table.optType')">
          <el-tag>
            {{ switchOptType(operationLog.optType) }}
          </el-tag>
        </el-form-item>
        <el-form-item :label="$t('table.optUri')">
          {{ operationLog.optUri }}
        </el-form-item>
        <el-form-item :label="$t('table.optMethod')">
          {{ operationLog.optMethod }}
        </el-form-item>
        <el-form-item :label="$t('table.optDesc')">
          {{ operationLog.optDesc }}
        </el-form-item>
        <el-form-item :label="$t('table.ipAddress')">
          {{ operationLog.ipAddress }}
        </el-form-item>
        <el-form-item :label="$t('table.ipSource')">
          {{ operationLog.ipSource }}
        </el-form-item>
        <el-form-item :label="$t('table.createTime')">
          {{ operationLog.createTime | dateTime }}
        </el-form-item>
        <el-form-item
          :label="$t('table.requestParam')"
          style="white-space: pre-line"
        >
          {{ operationLog.requestParam }}
        </el-form-item>
        <el-form-item
          :label="$t('operation.responseData')"
          style="white-space: pre-line"
        >
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
      options: [],
      pickerOptions: {
        shortcuts: [
          {
            text: "",
            onClick(picker) {
              picker.$emit("pick", new Date());
            }
          },
          {
            text: "",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            }
          },
          {
            text: "",
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
      current: 1,
      columnCount: 8,
      columnCheckedCount: 0
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
    handleColumnCheckedChange(value) {
      if (value) {
        this.columnCheckedCount++;
      } else {
        this.columnCheckedCount--;
      }
    },
    handleColumnCheckedAllChange(value) {
      if (value) {
        this.initColumnConfig();
      } else {
        this.showColumnConfig = {
          username: false,
          optModule: false,
          optType: false,
          optDesc: false,
          optMethod: false,
          ipAddress: false,
          ipSource: false,
          createTime: false
        };
        this.columnCheckedCount = 0;
      }
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
        this.columnCheckedCount = Object.values(this.showColumnConfig).reduce(
          (count, value) => {
            return value ? ++count : count;
          },
          0
        );
      } else {
        this.initColumnConfig();
      }
    },
    initColumnConfig() {
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
      this.columnCheckedCount = 8;
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
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: 1,
            label: this.$t("option.insert")
          },
          {
            value: 2,
            label: this.$t("option.delete")
          },
          {
            value: 3,
            label: this.$t("option.update")
          },
          {
            value: 4,
            label: this.$t("option.select")
          },
          {
            value: 5,
            label: this.$t("option.insertOrUpdate")
          }
        ];
        this.pickerOptions.shortcuts[0].text = this.$t("input.today");
        this.pickerOptions.shortcuts[1].text = this.$t("input.yesterday");
        this.pickerOptions.shortcuts[2].text = this.$t("input.lastWeek");
      },
      immediate: true
    }
  },
  computed: {
    switchOptType() {
      return function(type) {
        switch (type) {
          case 1:
            return this.$t("option.insert");
          case 2:
            return this.$t("option.delete");
          case 3:
            return this.$t("option.update");
          case 4:
            return this.$t("option.select");
          case 5:
            return this.$t("option.insertOrUpdate");
        }
      };
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>
