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
          v-if="checkWeight(100)"
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
      <el-table-column
        type="selection"
        align="center"
        width="40"
        :selectable="checkSelectable"
      />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column prop="avatar" label="头像" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            :src="scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[scope.row.avatar]"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="昵称"
        align="center"
        width="120"
      />
      <el-table-column prop="email" label="邮箱" align="center" width="120" />
      <el-table-column prop="intro" label="介绍" align="center" />
      <el-table-column prop="website" label="网站" align="center" />
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        width="200"
        align="center"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-popconfirm
            v-if="deletedFlag"
            title="确定恢复吗？"
            style="margin-left:10px"
            @confirm="updateUsersStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-button
            v-else
            :disabled="checkRoot(scope.row.userId, true)"
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
            <el-button
              :disabled="checkRoot(scope.row.userId)"
              type="danger"
              size="mini"
              slot="reference"
            >
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
      <el-form :model="user" size="medium" label-width="80">
        <el-form-item label="账号" v-if="!user.id && user.id !== 0">
          <el-input
            v-model="user.username"
            :maxlength="50"
            ref="input"
            style="width:200px"
            @keyup.native="usernameInputChange($event)"
            @keyup.enter.native="getUsernameExistFlag"
          />&nbsp;
          <span
            v-if="usernameExistStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            该账号已存在!</span
          >
          <span
            v-if="usernameExistStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input
            v-model="user.nickname"
            :maxlength="50"
            :ref="user.id || user.id === 0 ? 'input' : ''"
            style="width:200px"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="user.email"
            :maxlength="50"
            style="width:200px"
            @keyup.native="emailInputChange($event)"
            @keyup.enter.native="getEmailExistFlag"
          />&nbsp;
          <span
            v-if="emailExistStatus === -1"
            class="el-icon-error"
            style="color: red;"
          >
            该邮箱不合法!</span
          >
          <span
            v-if="emailExistStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            该邮箱已存在!</span
          >
          <span
            v-if="emailExistStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item label="介绍">
          <el-input v-model="user.intro" :maxlength="50" style="width:200px" />
        </el-form-item>
        <el-form-item label="网站">
          <el-input
            v-model="user.website"
            :maxlength="255"
            style="width:200px"
          />
        </el-form-item>
        <el-form-item label="头像" v-if="user.id || user.id === 0">
          <el-upload
            :limit="1"
            action=""
            class="upload-cover"
            drag
            :on-remove="updateAvatar"
            :http-request="uploadAvatar"
          >
            <i class="el-icon-upload" v-if="!user.avatar" />
            <div class="el-upload__text" v-if="!user.avatar">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="user.avatar" width="200" height="200" />
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button
          type="primary"
          :disabled="
            (usernameExistStatus !== 2 && user.id === undefined) ||
              emailExistStatus !== 2
          "
          @click="addOrEditUser"
        >
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
      user: {},
      userList: [],
      userIdList: [],
      keywords: null,
      oldEmail: null,
      loading: true,
      editStatus: false,
      deletedFlag: false,
      removeStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1,
      lastTimeStamp: 0,
      emailExistStatus: 0,
      usernameExistStatus: 0
    };
  },
  methods: {
    openModel(user) {
      if (user != null) {
        this.user = {
          id: user.id,
          intro: user.intro,
          email: user.email,
          avatar: user.avatar,
          website: user.website,
          nickname: user.nickname
        };
        this.oldEmail = user.email;
        this.emailExistStatus = 2;
        this.$refs.userTitle.innerHTML = "修改用户";
      } else {
        this.user = {
          email: "",
          username: "",
          nickname: ""
        };
        this.emailExistStatus = 0;
        this.usernameExistStatus = 0;
        this.$refs.userTitle.innerHTML = "添加用户";
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    checkRoot(id, flag = false) {
      const rootFlag = this.$store.state.rootUserId.some(e => e === id);
      if (flag) {
        return rootFlag && !this.checkWeight(100);
      } else {
        return rootFlag;
      }
    },
    sizeChange(size) {
      this.size = size;
      this.listUsers();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listUsers();
    },
    checkSelectable(row) {
      return !this.checkRoot(row.userId);
    },
    selectionChange(userList) {
      this.userIdList = [];
      userList.forEach(item => {
        this.userIdList.push(item.id);
      });
    },
    emailInputChange(event) {
      if (this.user.email.trim() === this.oldEmail && this.user.id) {
        this.lastTimeStamp = 0;
        this.emailExistStatus = 2;
      } else {
        if (event.key !== "Enter") {
          this.lastTimeStamp = event.timeStamp;
          setTimeout(() => {
            if (this.lastTimeStamp === event.timeStamp) {
              const emailRegex = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
              if (emailRegex.test(this.user.email.trim())) {
                this.getEmailExistFlag();
              } else {
                this.emailExistStatus = -1;
              }
            }
          }, 1000);
        } else {
          this.lastTimeStamp = 0;
        }
      }
    },
    getEmailExistFlag() {
      const keywords = this.user.email.trim();
      if (keywords === "" || keywords === this.oldEmail) {
        return;
      }
      this.axios
        .get("/api/back/user/exist", { params: { keywords } })
        .then(({ data }) => {
          if (data.data) {
            this.emailExistStatus = 1;
          } else {
            this.emailExistStatus = 2;
          }
        });
    },
    usernameInputChange(event) {
      if (this.user.username.trim() === "") {
        this.usernameExistStatus = 0;
      } else {
        if (event.key !== "Enter") {
          this.lastTimeStamp = event.timeStamp;
          setTimeout(() => {
            if (this.lastTimeStamp === event.timeStamp) {
              this.getUsernameExistFlag();
            }
          }, 1000);
        } else {
          this.lastTimeStamp = 0;
        }
      }
    },
    getUsernameExistFlag() {
      const keywords = this.user.username.trim();
      if (keywords === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/exist", { params: { keywords } })
        .then(({ data }) => {
          if (data.data) {
            this.usernameExistStatus = 1;
          } else {
            this.usernameExistStatus = 2;
          }
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
    updateAvatar() {
      const url = this.user.avatar;
      this.user.avatar = "";
      let param = { data: url };
      this.axios.put("/api/back/user/avatar", param);
    },
    uploadAvatar(form) {
      if (this.user.avatar !== "") {
        this.updateAvatar();
      }
      let formData = new FormData();
      formData.append("file", form.file);
      formData.append("userId", this.user.id);
      this.axios.post("/api/back/user/avatar", formData).then(({ data }) => {
        this.user.avatar = data.data;
      });
    },
    addOrEditUser() {
      if (this.user.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      this.axios.post("/api/back/user", this.user).then(({ data }) => {
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
    updateUsersStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.userIdList);
      }
      param.append("deletedFlag", !this.deletedFlag);
      this.axios.put("/api/back/userAuths", param).then(({ data }) => {
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

<style scoped>
/deep/ .el-upload .el-upload-dragger {
  width: 200px;
  height: 200px;
}
</style>
