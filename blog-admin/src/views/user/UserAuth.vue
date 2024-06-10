<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <div style="margin-left:auto">
        <el-select
          v-model="roleId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择角色"
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
          placeholder="请选择禁用状态"
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
          placeholder="请选择锁定状态"
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
          placeholder="请选择"
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
          placeholder="请输入用户名"
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
          搜索
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="userAuthList" border>
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.roleDTOList"
        prop="roleDTOList"
        label="角色"
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
        label="禁用"
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
        label="锁定"
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
        label="登录方式"
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
        v-if="showColumnConfig.loginTime"
        prop="loginTime"
        label="上次登录时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.loginTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.loginTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="80">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.roleDTOList"
                >角色</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.disabledFlag"
                >禁用</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.lockedFlag"
                >锁定</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginMethod"
                >登录方式</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipAddress"
                >ip地址</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.ipSource"
                >ip来源</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.loginTime"
                >上次登录时间</el-checkbox
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
          <el-button
            :disabled="checkRootUser(scope.row.userId)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
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
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="userAuthTitle" />
      <el-form :model="userAuth" size="medium" label-width="80">
        <el-form-item label="账号">
          <el-input
            v-model="userAuth.username"
            class="form-input-width"
            disabled
          />
        </el-form-item>
        <el-form-item label="密码">
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
            密码长度至少6位!</span
          >
          <span
            v-if="passwordStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item label="重复">
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
            前后密码不一致!</span
          >
        </el-form-item>
      </el-form>
      <el-form :model="userAuth" :inline="true" size="medium" label-width="80">
        <el-form-item label="禁用">
          <el-switch
            v-model="userAuth.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="锁定">
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
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button
          type="primary"
          :disabled="passwordStatus === 1 || confirmPasswordStatus === 1"
          @click="editUserAuth"
        >
          确 定
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
      options: [
        {
          value: null,
          label: "未删除"
        },
        {
          value: 7,
          label: "已删除"
        }
      ],
      options2: [
        {
          value: false,
          label: "未锁定"
        },
        {
          value: true,
          label: "已锁定"
        }
      ],
      options3: [
        {
          value: false,
          label: "未禁用"
        },
        {
          value: true,
          label: "已禁用"
        }
      ],
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
      passwordStatus: 0,
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
      this.$refs.userAuthTitle.innerHTML = "修改账号";
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
        loginMethods.push("邮箱");
      }
      if (loginMethod & 2) {
        loginMethods.push("QQ");
      }
      if (loginMethod & 4) {
        loginMethods.push("微信");
      }
      if (loginMethod & 8) {
        loginMethods.push("手机号");
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
      } else {
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
      }
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
            title: "成功",
            message: data.message
          });
          this.getUserAuths();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    },
    updateUserAuthStatus(userAuth, flag) {
      let text = flag
        ? userAuth.lockedFlag
          ? "解锁"
          : "锁定"
        : userAuth.disabledFlag
        ? "启用"
        : "禁用";
      this.$confirm("是否" + text + "该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let param = {
            idList: [userAuth.userId]
          };
          if (flag) {
            param.type = 12;
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
                  title: "失败",
                  message: data.message
                });
              }
            });
        })
        .catch(() => {});
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
    }
  }
};
</script>
