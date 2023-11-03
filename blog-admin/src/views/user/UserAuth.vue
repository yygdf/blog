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
        prop="username"
        label="用户"
        align="center"
        width="160"
      />
      <el-table-column
        prop="roleDTOList"
        label="角色"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <el-tag
            v-for="item of scope.row.roleIdList.split(',')"
            :key="item"
            style="margin-right:4px;margin-top:4px"
          >
            {{ roleNameList.filter(e => e.id === Number(item))[0].label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
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
            @change="changeUserAuthDisabledStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="lockedFlag" label="锁定" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.lockedFlag"
            :disabled="checkRootUser(scope.row.userId)"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeUserAuthLockedStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="loginMethod" label="登录方式" align="center">
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
        <template slot-scope="scope">
          <el-button
            :disabled="checkRootUser(scope.row.userId)"
            type="primary"
            size="mini"
            @click="openModel(scope.row)"
          >
            编辑
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
          <el-input v-model="userAuth.username" style="width: 200px" disabled />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="userAuth.password"
            ref="input"
            style="width: 200px"
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
            style="width: 200px"
            show-password
            @keyup.native="passwordInputChange()"
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
          value: null,
          label: ""
        },
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
          value: null,
          label: ""
        },
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
    passwordInputChange(flag = false) {
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
        this.editStatus = false;
      });
    },
    changeUserAuthLockedStatus(userAuth) {
      let lockedFlag = userAuth.lockedFlag;
      let text = lockedFlag ? "解锁" : "锁定";
      this.$confirm("是否" + text + "该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          userAuth.lockedFlag = !lockedFlag;
          this.axios.put("/api/back/userAuth/status", {
            idList: [userAuth.userId],
            type: 12
          });
        })
        .catch(() => {});
    },
    changeUserAuthDisabledStatus(userAuth) {
      let disabledFlag = userAuth.disabledFlag;
      let text = disabledFlag ? "启用" : "禁用";
      this.$confirm("是否" + text + "该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          userAuth.disabledFlag = !disabledFlag;
          this.axios.put("/api/back/userAuth/status", {
            idList: [userAuth.userId]
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
