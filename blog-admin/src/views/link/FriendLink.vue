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
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
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
          placeholder="请输入友链名"
          clearable
          @keyup.enter.native="getFriendLinks(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getFriendLinks(false)"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="friendLinkList"
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
        prop="linkLogo"
        label="友链图标"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.linkLogo"
            style="width: 40px;height: 40px;"
            :preview-src-list="[scope.row.linkLogo]"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="linkName"
        label="友链名称"
        align="center"
        width="120"
      />
      <el-table-column prop="linkUrl" label="友链地址" align="center" />
      <el-table-column prop="linkDesc" label="友链描述" align="center" />
      <el-table-column
        prop="createTime"
        label="创建日期"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            type="primary"
            size="mini"
            @click="openModel(scope.row)"
          >
            编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateFriendLinksStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateFriendLinksStatus(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteFriendLinks(scope.row.id)"
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
      :page-sizes="[10, 20]"
      :current-page.sync="current"
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
        <el-button type="primary" @click="updateFriendLinksStatus(null)">
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
        <el-button type="primary" @click="deleteFriendLinks(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="friendLinkTitle" />
      <el-form :model="friendLink" size="medium" label-width="80">
        <el-form-item label="所属用户">
          <el-select
            :disabled="friendLink.id != null"
            v-model="friendLink.userId"
            :ref="friendLink.id ? '' : 'input'"
            style="width: 200px"
            placeholder="请选择用户"
            remote
            clearable
            filterable
            :remote-method="query => getUsernames(query, false)"
          >
            <el-option
              v-for="item in usernameListAdd"
              :key="item.id"
              :value="item.id"
              :label="item.label"
            />
          </el-select>
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="友链名称">
          <el-input
            v-model="friendLink.linkName"
            :ref="friendLink.id ? 'input' : ''"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入友链名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="友链描述">
          <el-input
            v-model="friendLink.linkDesc"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入友链描述"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="友链图标">
          <el-input
            v-model="friendLink.linkLogo"
            class="word-limit-input2"
            style="width: 200px"
            maxlength="255"
            placeholder="请输入友链图标"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="友链地址">
          <el-input
            v-model="friendLink.linkUrl"
            class="word-limit-input2"
            style="width: 200px"
            maxlength="255"
            placeholder="请输入友链地址"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditFriendLink">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getFriendLinks();
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
      usernameList: [],
      friendLinkList: [],
      usernameListAdd: [],
      friendLinkIdList: [],
      friendLink: {},
      friendLinkOrigin: {},
      type: null,
      userId: null,
      keywords: null,
      oldKeywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(friendLink) {
      if (friendLink != null) {
        this.friendLink = {
          id: friendLink.id,
          userId: friendLink.username,
          linkUrl: friendLink.linkUrl,
          linkDesc: friendLink.linkDesc,
          linkLogo: friendLink.linkLogo,
          linkName: friendLink.linkName
        };
        this.$refs.friendLinkTitle.innerHTML = "修改友链";
      } else {
        this.friendLink = {
          userId: null,
          linkUrl: "",
          linkDesc: "",
          linkLogo: "",
          linkName: ""
        };
        this.$refs.friendLinkTitle.innerHTML = "添加友链";
      }
      this.friendLinkOrigin = JSON.parse(JSON.stringify(this.friendLink));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getFriendLinks(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getFriendLinks();
    },
    selectionChange(friendLinkList) {
      this.friendLinkIdList = [];
      friendLinkList.forEach(item => {
        this.friendLinkIdList.push(item.id);
      });
    },
    getFriendLinks(resetCurrentPage = false) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        type: this.type,
        userId: this.userId,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/friendLinks", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.friendLinkList = data.data.pageList;
          this.loading = false;
        });
    },
    getUsernames(keywords, flag = true) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          if (flag) {
            this.usernameList = data.data;
          } else {
            this.usernameListAdd = data.data;
          }
        });
    },
    addOrEditFriendLink() {
      if (!this.friendLink.userId) {
        this.$message.error("所属用户不能为空");
        return false;
      }
      if (this.friendLink.linkName.trim() === "") {
        this.$message.error("友链名称不能为空");
        return false;
      }
      if (this.friendLink.linkDesc.trim() === "") {
        this.$message.error("友链描述不能为空");
        return false;
      }
      if (this.friendLink.linkLogo.trim() === "") {
        this.$message.error("友链图标不能为空");
        return false;
      }
      if (this.friendLink.linkUrl.trim() === "") {
        this.$message.error("友链地址不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.friendLink,
        this.friendLinkOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.friendLink.id != null) {
        param.id = this.friendLink.id;
      }
      this.axios.post("/api/back/friendLink", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getFriendLinks();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    deleteFriendLinks(id) {
      let param = {};
      if (id == null) {
        param = { data: this.friendLinkIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/friendLinks", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.data.length === this.friendLinkList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getFriendLinks();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    updateFriendLinksStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.friendLinkIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/friendLinks/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.friendLinkList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getFriendLinks();
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
      this.getFriendLinks(true);
    },
    userId() {
      this.getFriendLinks(true);
    }
  }
};
</script>

<style scoped>
.word-limit-input {
  padding-right: 50px;
}
.word-limit-input2 {
  padding-right: 60px;
}
</style>
