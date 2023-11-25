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
        v-if="type !== 7"
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="gender"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择性别"
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
          placeholder="请输入用户名或昵称"
          clearable
          @keyup.enter.native="getUsers(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getUsers(true)"
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
            :src="scope.row.avatar === '' ? defaultAvatar : scope.row.avatar"
            style="width: 40px;height: 40px;"
            :preview-src-list="[
              scope.row.avatar === '' ? defaultAvatar : scope.row.avatar
            ]"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="昵称"
        align="center"
        width="120"
      />
      <el-table-column prop="gender" label="性别" align="center" width="80">
        <template slot-scope="scope">
          <img :src="switchGender(scope.row.gender)" width="30" height="30" />
        </template>
      </el-table-column>
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
          <el-button
            v-if="type !== 7"
            :disabled="checkRootUser(scope.row.id)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateUsersStatus(scope.row.id)"
          >
            <el-button
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> 恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateUsersStatus(scope.row.id)"
          >
            <el-button
              :disabled="checkRootUser(scope.row.id)"
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteUsers(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
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
    <el-dialog
      :visible.sync="addOrEditStatus"
      width="30%"
      @close="cancelAddOrEditUser"
    >
      <div class="dialog-title-container" slot="title" ref="userTitle" />
      <el-form :model="user" size="medium" label-width="80">
        <el-form-item label="账号">
          <el-input
            :disabled="user.id != null"
            v-model="user.username"
            :ref="user.id != null ? '' : 'input'"
            style="width: 200px"
            maxlength="50"
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
            :ref="user.id != null ? 'input' : ''"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入昵称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input
            v-model="user.email"
            style="width: 200px"
            maxlength="50"
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
          <el-input
            v-model="user.intro"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入介绍(可为空)"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="网站">
          <el-input
            v-model="user.website"
            class="word-limit-input2"
            style="width: 200px"
            maxlength="255"
            placeholder="请输入网站(可为空)"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="头像" v-if="user.id != null">
          <el-upload
            ref="upload"
            action=""
            class="upload-cover"
            :on-change="changeAvatar"
            :on-remove="updateAvatar"
            :http-request="uploadAvatar"
            :before-upload="beforeUpload"
            drag
          >
            <i class="el-icon-upload" v-if="!user.avatar" />
            <div class="el-upload__text" v-if="!user.avatar">
              将文件拖到此处, 或<em>点击上传</em><br />
              支持jpg/png/gif文件, 且不超过5MB
            </div>
            <img v-else :src="user.avatar" width="200" height="200" />
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="user.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
            <el-radio :label="3">可男可女</el-radio>
            <el-radio :label="4">非男非女</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button
          type="primary"
          :disabled="usernameExistStatus !== 2 || emailExistStatus !== 2"
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
    this.getUsers();
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
          value: 1,
          label: "男"
        },
        {
          value: 2,
          label: "女"
        },
        {
          value: 3,
          label: "可男可女"
        },
        {
          value: 4,
          label: "非男非女"
        }
      ],
      userList: [],
      userIdList: [],
      rootUserIdList: [],
      user: {},
      userOrigin: {},
      type: null,
      gender: null,
      keywords: null,
      oldEmail: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      addOrEditStatus: false,
      avatarUploadFlag: false,
      size: 10,
      count: 0,
      current: 1,
      lastTimeStamp: 0,
      emailExistStatus: 0,
      usernameExistStatus: 0,
      defaultAvatar: require("../../assets/img/defaultAvatar.png"),
      gender1: require("../../assets/img/gender1.png"),
      gender2: require("../../assets/img/gender2.png"),
      gender3: require("../../assets/img/gender3.png"),
      gender4: require("../../assets/img/gender4.png"),
      gender5: require("../../assets/img/gender5.png")
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
          gender: user.gender,
          website: user.website,
          username: user.username,
          nickname: user.nickname
        };
        this.oldEmail = user.email;
        this.emailExistStatus = 2;
        this.usernameExistStatus = 2;
        this.$refs.userTitle.innerHTML = "修改用户";
      } else {
        this.user = {
          id: null,
          intro: "",
          email: "",
          gender: null,
          website: "",
          username: "",
          nickname: ""
        };
        this.emailExistStatus = 0;
        this.usernameExistStatus = 0;
        this.$refs.userTitle.innerHTML = "添加用户";
      }
      this.userOrigin = JSON.parse(JSON.stringify(this.user));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
      this.avatarUploadFlag = false;
    },
    sizeChange(size) {
      this.size = size;
      this.getUsers(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getUsers();
    },
    checkRootUser(userId) {
      return (
        !this.checkWeight(100) && this.rootUserIdList.some(e => e === userId)
      );
    },
    checkSelectable(row) {
      return !this.checkRootUser(row.id);
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
        .get("/api/back/user/email", { params: { keywords } })
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
    cancelAddOrEditUser() {
      if (this.avatarUploadFlag) {
        this.updateImage();
        this.$refs.upload.clearFiles();
        this.avatarUploadFlag = false;
      }
    },
    getUsernameExistFlag() {
      const keywords = this.user.username.trim();
      if (keywords === "") {
        return;
      }
      this.axios
        .get("/api/back/user/username", { params: { keywords } })
        .then(({ data }) => {
          if (data.data) {
            this.usernameExistStatus = 1;
          } else {
            this.usernameExistStatus = 2;
          }
        });
    },
    getUsers(resetCurrentPage = false) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        current: this.current,
        keywords: this.keywords,
        categoryId: this.gender
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/users", {
          params
        })
        .then(({ data }) => {
          this.rootUserIdList = data.data.rootUserIdList;
          this.count = data.data.pagePojo.count;
          this.userList = data.data.pagePojo.pageList;
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
          if (param.data.length === this.userList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUsers();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    changeAvatar(file, fileList) {
      if (fileList.length > 1) {
        fileList.splice(0, 1);
      }
    },
    updateAvatar(file) {
      if (file && file.status === "success") {
        this.updateImage();
        this.user.avatar = "";
        this.avatarUploadFlag = false;
      }
    },
    updateImage() {
      let pathArr = this.user.avatar.split("/");
      let fileName = pathArr[pathArr.length - 1].split(".")[0];
      this.axios.put("/api/back/user/avatars", [fileName]);
    },
    beforeUpload(file) {
      if (
        file.type !== "image/jpeg" &&
        file.type !== "image/png" &&
        file.type !== "image/gif"
      ) {
        this.$message.error("上传的图片只能是jpg, png, gif格式");
        return false;
      }
      if (file.size >>> 20 > 5) {
        this.$message.error("上传图片的大小不能超过5MB");
        return false;
      }
      return true;
    },
    uploadAvatar(form) {
      if (this.avatarUploadFlag) {
        this.updateImage();
      }
      let formData = new FormData();
      formData.append("file", form.file);
      formData.append("userId", this.user.id);
      this.axios.post("/api/back/user/avatar", formData).then(({ data }) => {
        if (data.flag) {
          this.user.avatar = data.data;
          this.avatarUploadFlag = true;
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    addOrEditUser() {
      if (this.user.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.user,
        this.userOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.user.id != null) {
        param.id = this.user.id;
      }
      this.axios.post("/api/back/user", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.avatarUploadFlag = false;
          this.getUsers();
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
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.userIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/users/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.userList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUsers();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  watch: {
    type() {
      this.getUsers(true);
    },
    gender() {
      this.getUsers(true);
    }
  },
  computed: {
    switchGender() {
      return function(type) {
        switch (type) {
          case 1:
            return this.gender1;
          case 2:
            return this.gender2;
          case 3:
            return this.gender3;
          case 4:
            return this.gender4;
          default:
            return this.gender5;
        }
      };
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
