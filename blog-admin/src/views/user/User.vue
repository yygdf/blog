<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        新增
      </el-button>
      <el-button
        v-if="deletedFlag"
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
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
          placeholder="请选择"
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
          placeholder="请选择"
        >
          <el-option
            v-for="item in options2"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          />
        </el-select>
        <el-select
          v-model="deletedFlag"
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
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入用户名或昵称"
          clearable
          @keyup.enter.native="listUsers"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listUsers"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="userList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column prop="avatar" label="头像" align="center" width="80">
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" align="center" />
      <el-table-column prop="email" label="邮箱" align="center" width="120" />
      <el-table-column
        prop="roleList"
        label="用户角色"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-tag
            v-for="(item, index) of scope.row.roleDTOList"
            :key="index"
            style="margin-right:4px;margin-top:4px"
          >
            {{ item.label }}
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
            v-model="scope.row.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeUserStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="lockedFlag" label="锁定" align="center" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.lockedFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeUserStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="loginType" label="登录方式" align="center">
        <template slot-scope="scope">
          <el-tag
            v-for="(item, index) of scope.row.loginMethod"
            :key="index"
            style="margin-right:4px;margin-top:4px"
          >
            {{ item.loginMethodName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="ipAddress"
        label="登录ip"
        align="center"
        width="120"
      />
      <el-table-column
        prop="ipSource"
        label="登录地址"
        align="center"
        width="120"
      />
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="120"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
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
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            v-if="deletedFlag"
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteUsers(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateUsersStatus(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="count"
      :page-size="size"
      :current-page="current"
      :page-sizes="[10, 20]"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="editStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateUsersStatus(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">取 消</el-button>
        <el-button type="primary" @click="deleteUsers(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="userTitle" />
      <el-form :model="user" size="medium" label-width="60">
        <el-form-item label="昵称">
          <el-input v-model="user.nickname" style="width:200px" />
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="roleIdList">
            <el-checkbox
              v-for="item of roleNameList"
              :key="item.id"
              :label="item.id"
            >
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditUser">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listUsers();
    this.listAllRoleName();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "未删除"
        },
        {
          value: true,
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
      user: {},
      userList: [],
      roleIdList: [],
      userIdList: [],
      roleNameList: [],
      roleId: null,
      keywords: null,
      loading: true,
      editStatus: false,
      lockedFlag: false,
      deletedFlag: false,
      disabledFlag: false,
      removeStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(user) {
      if (user != null) {
        this.user = JSON.parse(JSON.stringify(user));
        this.$refs.userTitle.innerHTML = "修改用户";
      } else {
        this.user = {};
        this.$refs.userTitle.innerHTML = "添加用户";
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listUsers();
    },
    currentChange(current) {
      this.current = current;
      this.listUsers();
    },
    selectionChange(userList) {
      this.userIdList = [];
      userList.forEach(item => {
        this.userIdList.push(item.id);
      });
    },
    listUsers() {
      this.axios
        .get("/api/back/users", {
          params: {
            size: this.size,
            current: this.current,
            keywords: this.keywords,
            draftFlag: this.lockedFlag,
            categoryId: this.roleId,
            deletedFlag: this.deletedFlag,
            recycleFlag: this.disabledFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.userList = data.data.pageList;
          this.loading = false;
        });
    },
    listAllRoleName() {
      this.axios.get("/api/back/role/roleName").then(({ data }) => {
        this.roleNameList = data.data;
      });
    },
    deleteUsers(id) {
      let param = {};
      if (id == null) {
        param = { data: this.userIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/users", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listUsers();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.removeStatus = false;
      });
    },
    addOrEditUser() {
      this.user.roleIdList = this.roleIdList;
      this.axios.put("/api/back/user", this.user).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listUsers();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.addOrEditStatus = false;
      });
    },
    changeUserStatus(user) {
      let param = {
        id: user.id,
        topFlag: user.lockedFlag,
        publicFlag: user.disabledFlag
      };
      this.axios.put("/api/back/user/status", param);
    },
    updateUsersStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.userIdList);
      }
      param.append("deletedFlag", !this.deletedFlag);
      this.axios.put("/api/back/users", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listUsers();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.editStatus = false;
      });
    }
  },
  watch: {
    roleId() {
      this.listUsers();
    },
    lockedFlag() {
      this.listUsers();
    },
    deletedFlag() {
      this.listUsers();
    },
    disabledFlag() {
      this.listUsers();
    }
  }
};
</script>
