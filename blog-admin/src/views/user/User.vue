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
        <el-input
          v-model="keywords"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入用户名或昵称"
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
      <el-table-column
        prop="avatar"
        label="头像"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <img :src="scope.row.avatar" width="40" height="40" />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="昵称"
        align="center"
        width="120"
      />
      <el-table-column
        prop="email"
        label="邮箱"
        align="center"
        width="120"
      />
      <el-table-column
        prop="loginType"
        label="登录方式"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag
            v-for="(item, index) of scope.row.loginTypeList"
            :key="index"
            style="margin-right:4px;margin-top:4px"
          >
            {{ item.loginTypeName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleList" label="用户角色" align="center">
        <template slot-scope="scope">
          <el-tag
            v-for="(item, index) of scope.row.roleList"
            :key="index"
            style="margin-right:4px;margin-top:4px"
          >
            {{ item.roleName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="disabledFlag" label="禁用" align="center" width="80">
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
          <el-button
            type="primary"
            size="mini"
            @click="openModel(scope.row)"
          >
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
              v-for="item of roleList"
              :key="item.id"
              :label="item.id"
            >
              {{ item.roleName }}
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
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "可使用"
        },
        {
          value: true,
          label: "已删除"
        }
      ],
      user: {},
      roleList: [],
      userList: [],
      roleIdList: [],
      userIdList: [],
      ipSourceList: [],
      loginTypeList: [],
      keywords: null,
      loading: true,
      editStatus: false,
      deletedFlag: false,
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
        this.$refs.categoryTitle.innerHTML = "修改用户";
      } else {
        this.user = {};
        this.$refs.categoryTitle.innerHTML = "添加用户";
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
            deletedFlag: this.deletedFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.userList = data.data.pageList;
          this.loading = false;
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
    deletedFlag() {
      this.listUsers();
    }
  }
};
</script>
