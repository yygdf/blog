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
          v-model="loginMethod"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectMethod')"
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
          :placeholder="$t('input.selectPlatform')"
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
          @change="getLoginLogs(true)"
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
          @change="getLoginLogs(true)"
        >
        </el-date-picker>
      </div>
    </div>
    <el-table v-loading="loading" :data="loginLogList" border>
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginMethod"
        prop="loginMethod"
        :label="$t('table.loginMethod')"
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
        v-if="showColumnConfig.loginPlatform"
        prop="loginPlatform"
        :label="$t('table.loginPlatform')"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag>
            {{
              scope.row.loginPlatform
                ? $t("option.backend")
                : $t("option.frontend")
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginDevice"
        prop="loginDevice"
        :label="$t('table.loginDevice')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginSystem"
        prop="loginSystem"
        :label="$t('loginLog.system')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.loginBrowser"
        prop="loginBrowser"
        :label="$t('loginLog.browser')"
        align="center"
        width="120"
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
        v-if="showColumnConfig.loginTime"
        prop="loginTime"
        :label="$t('table.loginTime')"
        align="center"
        width="200"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="80">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username">{{
                $t("table.user")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginMethod">{{
                $t("table.loginMethod")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginPlatform">{{
                $t("table.loginPlatform")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginDevice">{{
                $t("table.loginDevice")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginSystem">{{
                $t("loginLog.system")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginBrowser">{{
                $t("loginLog.browser")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.ipAddress">{{
                $t("table.ipAddress")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.ipSource">{{
                $t("table.ipSource")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.loginTime">{{
                $t("table.loginTime")
              }}</el-checkbox>
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
    this.loadColumnConfig();
    this.getLoginLogs();
  },
  data: function() {
    return {
      options: [],
      options2: [],
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
      loginLogList: [],
      usernameList: [],
      showColumnConfig: {},
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
    saveColumnConfig() {
      localStorage.setItem(
        "LoginColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("LoginColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("LoginColumnSet")
        );
      } else {
        this.showColumnConfig = {
          username: true,
          loginMethod: true,
          loginPlatform: true,
          loginDevice: true,
          loginSystem: true,
          loginBrowser: true,
          ipAddress: true,
          ipSource: true,
          loginTime: true
        };
      }
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
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: false,
            label: this.$t("option.frontend")
          },
          {
            value: true,
            label: this.$t("option.backend")
          }
        ];
        this.options2 = [
          {
            value: 1,
            label: this.$t("option.email")
          },
          {
            value: 2,
            label: this.$t("option.qq")
          },
          {
            value: 3,
            label: this.$t("option.wx")
          },
          {
            value: 4,
            label: this.$t("option.phone")
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
    switchLoginMethod() {
      return function(type) {
        switch (type) {
          case 1:
            return this.$t("option.email");
          case 2:
            return this.$t("option.qq");
          case 3:
            return this.$t("option.wx");
          case 4:
            return this.$t("option.phone");
        }
      };
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>
