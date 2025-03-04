<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <div style="margin-left:auto">
        <el-select
          v-model="roleId"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectRole')"
          clearable
          filterable
        >
          <el-option
            v-for="item in roleNameList"
            :key="item.id"
            :value="item.id"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="disabledFlag"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectDisabled')"
          clearable
        >
          <el-option
            v-for="item in options3"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="lockedFlag"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectLocked')"
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
          v-if="checkWeight(100)"
          v-model="type"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.select')"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          :placeholder="$t('auth.inputUsername')"
          clearable
          @keyup.enter.native="getUserAuths(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUserAuths(true)"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="userAuthList" border>
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.roleDTOList"
        prop="roleDTOList"
        :label="$t('auth.role')"
        align="center"
        min-width="240"
      >
        <template slot-scope="scope">
          <el-tag
            v-for="item of scope.row.roleIdList.split(',')"
            :key="item"
            style="margin-right:4px;margin-top:4px"
          >
            {{
              roleNameList.length > 0
                ? roleNameList.filter(e => e.id === Number(item))[0].label
                : ""
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.disabledFlag"
        prop="disabledFlag"
        :label="$t('switch.disabled')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.disabledFlag"
            :disabled="checkRootUser(scope.row.userId)"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateUserAuthStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.lockedFlag"
        prop="lockedFlag"
        :label="$t('switch.locked')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.lockedFlag"
            :disabled="checkRootUser(scope.row.userId)"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateUserAuthStatus(scope.row, true)"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.loginMethod"
        prop="loginMethod"
        :label="$t('table.loginMethod')"
        align="center"
        min-width="240"
      >
        <template slot-scope="scope" v-if="scope.row.loginMethod">
          <el-tag
            v-for="item of parseLoginMethod(scope.row.loginMethod)"
            :key="item"
            style="margin-right:4px;margin-top:4px"
          >
            {{ item }}
          </el-tag>
        </template>
      </el-table-column>
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
        :label="$t('auth.lastLoginTime')"
        width="200"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.loginTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="80">
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
                v-model="showColumnConfig.roleDTOList"
                @change="handleColumnCheckedChange"
                >{{ $t("auth.role") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.disabledFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.disabled") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.lockedFlag"
                @change="handleColumnCheckedChange"
                >{{ $t("switch.locked") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.loginMethod"
                @change="handleColumnCheckedChange"
                >{{ $t("table.loginMethod") }}</el-checkbox
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
                v-model="showColumnConfig.loginTime"
                @change="handleColumnCheckedChange"
                >{{ $t("auth.lastLoginTime") }}</el-checkbox
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
              @click="getUserAuths(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            :disabled="checkRootUser(scope.row.userId)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
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
    <el-dialog :visible.sync="editStatus" width="35%">
      <div class="dialog-title-container" slot="title" ref="userAuthTitle" />
      <el-form :model="userAuth" size="medium" label-width="90px">
        <el-form-item :label="$t('user.username')">
          <el-input
            v-model="userAuth.username"
            class="form-input-width"
            disabled
          />
        </el-form-item>
        <el-form-item :label="$t('auth.password')">
          <el-input
            v-model="userAuth.password"
            ref="input"
            class="form-input-width"
            show-password
            @keyup.native="passwordInputChange(true)"
          />&nbsp;
          <span
            v-if="passwordStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            {{ $t("login.passwordRule2") }}</span
          >
          <span
            v-if="passwordStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item :label="$t('auth.password2')">
          <el-input
            v-model="confirmPassword"
            class="form-input-width"
            show-password
            @keyup.native="passwordInputChange(false)"
          />&nbsp;
          <span
            v-if="confirmPasswordStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
          <span
            v-if="confirmPasswordStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            {{ $t("auth.passwordRule1") }}</span
          >
        </el-form-item>
      </el-form>
      <el-form
        :model="userAuth"
        :inline="true"
        size="medium"
        label-width="90px"
      >
        <el-form-item :label="$t('switch.disabled')">
          <el-switch
            v-model="userAuth.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item :label="$t('switch.locked')">
          <el-switch
            v-model="userAuth.lockedFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item>
          <el-checkbox-group v-model="userAuth.roleIdList" :min="1">
            <el-checkbox
              v-for="item of roleNameList"
              :disabled="
                !checkWeight(100) && rootRoleIdList.some(e => e === item.id)
              "
              :key="item.id"
              :label="item.id"
            >
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button
          type="primary"
          :disabled="passwordStatus === 1 || confirmPasswordStatus === 1"
          @click="editUserAuth"
        >
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import md5 from "js-md5";
export default {
  created() {
    this.loadColumnConfig();
    this.getUserAuths();
    this.getRoleNames();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
      options2: [],
      options3: [],
      userAuthList: [],
      roleNameList: [],
      rootUserIdList: [],
      rootRoleIdList: [],
      userAuth: {},
      userAuthOrigin: {},
      showColumnConfig: {},
      confirmPassword: "",
      type: null,
      roleId: null,
      keywords: null,
      lockedFlag: null,
      oldKeywords: null,
      disabledFlag: null,
      loading: true,
      editStatus: false,
      size: 10,
      count: 0,
      current: 1,
      columnCount: 8,
      passwordStatus: 0,
      columnCheckedCount: 0,
      confirmPasswordStatus: 0
    };
  },
  methods: {
    openModel(userAuth) {
      this.userAuth = {
        id: userAuth.id,
        username: userAuth.username,
        lockedFlag: userAuth.lockedFlag,
        disabledFlag: userAuth.disabledFlag,
        roleIdList: userAuth.roleIdList.split(",").map(e => Number(e)),
        password: ""
      };
      this.confirmPassword = "";
      this.passwordStatus = 0;
      this.confirmPasswordStatus = 0;
      this.userAuthOrigin = JSON.parse(JSON.stringify(this.userAuth));
      this.$refs.userAuthTitle.innerHTML = this.$t("auth.edit");
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.editStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getUserAuths(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    checkRootUser(userId) {
      return (
        !this.checkWeight(100) && this.rootUserIdList.some(e => e === userId)
      );
    },
    currentChange(current) {
      this.current = current;
      this.getUserAuths();
    },
    parseLoginMethod(loginMethod) {
      let loginMethods = [];
      loginMethod = parseInt(loginMethod, 2);
      if (loginMethod & 1) {
        loginMethods.push(this.$t("option.email"));
      }
      if (loginMethod & 2) {
        loginMethods.push(this.$t("option.qq"));
      }
      if (loginMethod & 4) {
        loginMethods.push(this.$t("option.wx"));
      }
      if (loginMethod & 8) {
        loginMethods.push(this.$t("option.phone"));
      }
      return loginMethods;
    },
    passwordInputChange(flag) {
      if (
        this.userAuth.password.trim() === "" &&
        this.confirmPassword.trim() === ""
      ) {
        this.passwordStatus = 0;
        this.confirmPasswordStatus = 0;
        return;
      }
      if (flag) {
        if (this.userAuth.password.trim().length < 6) {
          this.passwordStatus = 1;
          return;
        }
        this.passwordStatus = 2;
      }
      if (this.userAuth.password.trim() !== this.confirmPassword.trim()) {
        this.confirmPasswordStatus = 1;
        return;
      }
      this.confirmPasswordStatus = 2;
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
          roleDTOList: false,
          disabledFlag: false,
          lockedFlag: false,
          loginMethod: false,
          ipAddress: false,
          ipSource: false,
          loginTime: false
        };
        this.columnCheckedCount = 0;
      }
    },
    saveColumnConfig() {
      localStorage.setItem(
        "UserAuthColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("UserAuthColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("UserAuthColumnSet")
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
        roleDTOList: true,
        disabledFlag: true,
        lockedFlag: true,
        loginMethod: true,
        ipAddress: true,
        ipSource: true,
        loginTime: true
      };
      this.columnCheckedCount = 8;
    },
    getUserAuths(resetCurrentPage = false) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        flag: this.disabledFlag,
        status: this.lockedFlag,
        current: this.current,
        keywords: this.keywords,
        categoryId: this.roleId
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/userAuths", {
          params
        })
        .then(({ data }) => {
          this.rootUserIdList = data.data.rootUserIdList;
          this.rootRoleIdList = data.data.rootRoleIdList;
          this.count = data.data.pagePojo.count;
          this.userAuthList = data.data.pagePojo.pageList;
          this.loading = false;
        });
    },
    getRoleNames() {
      this.axios.get("/api/back/role/roleNames").then(({ data }) => {
        this.roleNameList = data.data;
      });
    },
    editUserAuth() {
      let param = this.$commonMethod.skipIdenticalValue(
        this.userAuth,
        this.userAuthOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      param.id = this.userAuth.id;
      if (this.userAuth.password.trim() !== "") {
        param.password = md5(this.userAuth.password);
      }
      this.axios.put("/api/back/userAuth", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getUserAuths();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatus = false;
    },
    updateUserAuthStatus(userAuth, flag) {
      let text = flag
        ? userAuth.lockedFlag
          ? this.$t("confirm.content11")
          : this.$t("confirm.content12")
        : userAuth.disabledFlag
        ? this.$t("confirm.content13")
        : this.$t("confirm.content14");
      this.$confirm(text, this.$t("confirm.tip"), {
        confirmButtonText: this.$t("confirm.yes"),
        cancelButtonText: this.$t("confirm.no"),
        type: "warning"
      })
        .then(() => {
          let param = {
            idList: [userAuth.id]
          };
          if (flag) {
            param.type = 12;
            if (userAuth.lockedFlag) {
              param.status = true;
            }
          } else {
            if (userAuth.disabledFlag) {
              param.status = true;
            }
          }
          this.axios
            .put("/api/back/userAuth/status", param)
            .then(({ data }) => {
              if (data.flag) {
                if (flag) {
                  userAuth.lockedFlag = !userAuth.lockedFlag;
                } else {
                  userAuth.disabledFlag = !userAuth.disabledFlag;
                }
              } else {
                this.$notify.error({
                  title: this.$t("failure"),
                  message: data.message
                });
              }
            });
        })
        .catch(() => {});
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  },
  watch: {
    type() {
      this.getUserAuths(true);
    },
    roleId() {
      this.getUserAuths(true);
    },
    lockedFlag() {
      this.getUserAuths(true);
    },
    disabledFlag() {
      this.getUserAuths(true);
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: null,
            label: this.$t("option.available")
          },
          {
            value: 7,
            label: this.$t("option.deleted")
          }
        ];
        this.options2 = [
          {
            value: false,
            label: this.$t("option.unlocked")
          },
          {
            value: true,
            label: this.$t("option.locked")
          }
        ];
        this.options3 = [
          {
            value: false,
            label: this.$t("option.enabled")
          },
          {
            value: true,
            label: this.$t("option.disabled")
          }
        ];
      },
      immediate: true
    }
  }
};
</script>
