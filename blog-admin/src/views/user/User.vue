<template>
  <el-card class="main-card">
    <div class="title">
      {{ isEn ? this.$route.meta.nameEn : this.$route.name }}
    </div>
    <div class="operation-container">
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="openModel(null)"
      >
        {{ $t("button.add") }}
      </el-button>
      <el-button
        v-if="type !== 7"
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="userIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="gender"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectGender')"
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
          style="width: 280px"
          prefix-icon="el-icon-search"
          :placeholder="$t('user.input')"
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
          {{ $t("button.search") }}
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
        v-if="showColumnConfig.id"
        prop="id"
        label="ID"
        align="center"
        width="80"
      />
      <el-table-column
        v-if="showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.avatar"
        prop="avatar"
        :label="$t('table.avatar')"
        align="center"
        width="80"
      >
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
        v-if="showColumnConfig.nickname"
        prop="nickname"
        :label="$t('table.nickname')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.gender"
        prop="gender"
        :label="$t('user.gender')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <img :src="switchGender(scope.row.gender)" width="30" height="30" />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.email"
        prop="email"
        :label="$t('user.email')"
        align="center"
        width="120"
      />
      <el-table-column
        v-if="showColumnConfig.intro"
        prop="intro"
        :label="$t('user.intro')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.website"
        prop="website"
        :label="$t('user.website')"
        align="center"
        min-width="240"
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
        v-if="showColumnConfig.updateTime"
        prop="updateTime"
        :label="$t('table.updateTime')"
        align="center"
        width="200"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | dateTime }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        :label="$t('table.operate')"
        align="center"
        width="160"
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
                v-model="showColumnConfig.id"
                @change="handleColumnCheckedChange"
                >ID&nbsp;</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.avatar"
                @change="handleColumnCheckedChange"
                >{{ $t("table.avatar") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.nickname"
                @change="handleColumnCheckedChange"
                >{{ $t("table.nickname") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.gender"
                @change="handleColumnCheckedChange"
                >{{ $t("user.gender") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.email"
                @change="handleColumnCheckedChange"
                >{{ $t("user.email") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.intro"
                @change="handleColumnCheckedChange"
                >{{ $t("user.intro") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.website"
                @change="handleColumnCheckedChange"
                >{{ $t("user.website") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.createTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.createTime") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.updateTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.updateTime") }}</el-checkbox
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
              @click="getUsers(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            :disabled="checkRootUser(scope.row.id)"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-else
            :title="$t('confirm.content2')"
            @confirm="updateUsersStatus(scope.row.id)"
          >
            <el-button
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> {{ $t("button.restore") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            :title="$t('confirm.content3')"
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
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteUsers(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
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
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content5") }}</div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateUsersStatus(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content6") }}</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="deleteUsers(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="addOrEditStatus"
      width="35%"
      style="margin-top: -10vh"
      @close="cancelAddOrEditUser"
    >
      <div class="dialog-title-container" slot="title" ref="userTitle" />
      <el-form :model="user" size="medium" label-width="90px">
        <el-form-item :label="$t('user.username')">
          <el-input
            :disabled="user.id != null"
            v-model="user.username"
            :ref="user.id != null ? '' : 'input'"
            class="form-input-width"
            maxlength="50"
            :placeholder="$t('auth.inputUsername')"
            @keyup.native="usernameInputChange($event)"
            @keyup.enter.native="getUsernameExistFlag"
          />&nbsp;
          <span
            v-if="usernameExistStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            {{ $t("user.usernameRule1") }}</span
          >
          <span
            v-if="usernameExistStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item :label="$t('table.nickname')">
          <el-input
            v-model="user.nickname"
            :ref="user.id != null ? 'input' : ''"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('user.inputNickname')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('user.email')">
          <el-input
            v-model="user.email"
            class="form-input-width"
            maxlength="50"
            :placeholder="$t('user.inputEmail')"
            @keyup.native="emailInputChange($event)"
            @keyup.enter.native="getEmailExistFlag"
          />&nbsp;
          <span
            v-if="emailExistStatus === -1"
            class="el-icon-error"
            style="color: red;"
          >
            {{ $t("user.emailRule1") }}</span
          >
          <span
            v-if="emailExistStatus === 1"
            class="el-icon-error"
            style="color: red;"
          >
            {{ $t("user.emailRule2") }}</span
          >
          <span
            v-if="emailExistStatus === 2"
            class="el-icon-success"
            style="color: green;"
          ></span>
        </el-form-item>
        <el-form-item :label="$t('user.intro')">
          <el-input
            v-model="user.intro"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('user.inputIntro')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('user.website')">
          <el-input
            v-model="user.website"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('user.inputWebsite')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('table.avatar')" v-if="user.id != null">
          <el-upload
            ref="upload"
            action=""
            :on-change="changeAvatar"
            :on-remove="updateAvatar"
            :http-request="uploadAvatar"
            :before-upload="beforeUpload"
            drag
          >
            <i class="el-icon-upload" v-if="!user.avatar" />
            <div class="el-upload__text" v-if="!user.avatar">
              {{ $t("article.tip1") }} <em>{{ $t("article.tip2") }}</em
              ><br />
              {{ $t("article.tip3") }}
            </div>
            <img v-else :src="user.avatar" width="280" height="280" />
          </el-upload>
        </el-form-item>
        <el-form-item :label="$t('user.gender')">
          <el-radio-group v-model="user.gender">
            <el-radio :label="1">{{ $t("option.gender1") }}</el-radio>
            <el-radio :label="2">{{ $t("option.gender2") }}</el-radio>
            <el-radio :label="3">{{ $t("option.gender3") }}</el-radio>
            <el-radio :label="4">{{ $t("option.gender4") }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button
          type="primary"
          :disabled="usernameExistStatus !== 2 || emailExistStatus !== 2"
          @click="addOrEditUser"
        >
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.loadColumnConfig();
    this.getUsers();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
      options2: [],
      userList: [],
      userIdList: [],
      rootUserIdList: [],
      user: {},
      userOrigin: {},
      showColumnConfig: {},
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
      columnCount: 10,
      lastTimeStamp: 0,
      emailExistStatus: 0,
      columnCheckedCount: 0,
      usernameExistStatus: 0,
      defaultAvatar: process.env.VUE_APP_STATIC_URL + "img/avatar.png",
      gender1: process.env.VUE_APP_STATIC_URL + "img/gender1.png",
      gender2: process.env.VUE_APP_STATIC_URL + "img/gender2.png",
      gender3: process.env.VUE_APP_STATIC_URL + "img/gender3.png",
      gender4: process.env.VUE_APP_STATIC_URL + "img/gender4.png",
      gender5: process.env.VUE_APP_STATIC_URL + "img/gender5.png"
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
        this.$refs.userTitle.innerHTML = this.$t("user.edit");
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
        this.$refs.userTitle.innerHTML = this.$t("user.add");
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
    selectionChange(selection) {
      this.userIdList = [];
      selection.forEach(item => {
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
          id: false,
          username: false,
          avatar: false,
          nickname: false,
          gender: false,
          email: false,
          intro: false,
          website: false,
          createTime: false,
          updateTime: false
        };
        this.columnCheckedCount = 0;
      }
    },
    saveColumnConfig() {
      localStorage.setItem(
        "UserColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("UserColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("UserColumnSet")
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
        id: true,
        username: true,
        avatar: true,
        nickname: true,
        gender: true,
        email: true,
        intro: true,
        website: true,
        createTime: true,
        updateTime: true
      };
      this.columnCheckedCount = 10;
    },
    getUsers(resetCurrentPage) {
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.userList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUsers();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
      let contentType = file.type;
      if (
        contentType !== "image/jpeg" &&
        contentType !== "image/png" &&
        contentType !== "image/gif"
      ) {
        this.$message.error(this.$t("article.coverRule1"));
        return false;
      }
      if (file.size >>> 20 > 5) {
        this.$message.error(this.$t("article.coverRule2"));
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
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
    },
    addOrEditUser() {
      if (this.user.nickname.trim() === "") {
        this.$message.error(this.$t("user.nicknameRule1"));
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
            title: this.$t("success"),
            message: data.message
          });
          this.avatarUploadFlag = false;
          this.getUsers();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.userList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getUsers();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            value: 1,
            label: this.$t("option.gender1")
          },
          {
            value: 2,
            label: this.$t("option.gender2")
          },
          {
            value: 3,
            label: this.$t("option.gender3")
          },
          {
            value: 4,
            label: this.$t("option.gender4")
          }
        ];
      },
      immediate: true
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
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>

<style scoped>
/deep/ .el-upload .el-upload-dragger {
  width: 280px;
  height: 280px;
}
</style>
