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
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
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
          :remote-method="listAllUsername"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :value="item.userId"
            :label="item.label"
          />
        </el-select>
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
          placeholder="请输入友链名"
          clearable
          @keyup.enter.native="listFriendLinks"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listFriendLinks"
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
          <el-popconfirm
            v-if="deletedFlag"
            title="确定恢复吗？"
            style="margin-left:10px"
            @confirm="updateFriendLinksStatus(scope.row.id)"
          >
            <el-button type="success" size="mini" slot="reference">
              恢复
            </el-button>
          </el-popconfirm>
          <el-button
            v-else
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
            @confirm="deleteFriendLinks(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateFriendLinksStatus(scope.row.id)"
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
      :current-page="current"
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
        <el-form-item label="所属用户" v-if="!friendLink.id">
          <el-select
            v-model="friendLink.userId"
            ref="input"
            style="width:250px"
            placeholder="请选择用户"
            remote
            clearable
            filterable
            :remote-method="query => listAllUsername(query, false)"
          >
            <el-option
              v-for="item in usernameListAdd"
              :key="item.userId"
              :value="item.userId"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="友链名称">
          <el-input
            v-model="friendLink.linkName"
            :ref="friendLink.id ? 'input' : ''"
            style="width:250px"
            :maxLength="50"
          />
        </el-form-item>
        <el-form-item label="友链描述">
          <el-input
            v-model="friendLink.linkDesc"
            style="width:250px"
            :maxLength="50"
          />
        </el-form-item>
        <el-form-item label="友链图标">
          <el-input
            v-model="friendLink.linkLogo"
            style="width:250px"
            :maxLength="255"
          />
        </el-form-item>
        <el-form-item label="友链地址">
          <el-input
            v-model="friendLink.linkUrl"
            style="width:250px"
            :maxLength="255"
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
    this.listFriendLinks();
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
      friendLink: {},
      usernameList: [],
      friendLinkList: [],
      usernameListAdd: [],
      friendLinkIdList: [],
      userId: null,
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
    openModel(friendLink) {
      if (friendLink != null) {
        this.friendLink = {
          id: friendLink.id,
          linkUrl: friendLink.linkUrl,
          linkDesc: friendLink.linkDesc,
          linkLogo: friendLink.linkLogo,
          linkName: friendLink.linkName
        };
        this.$refs.friendLinkTitle.innerHTML = "修改友链";
      } else {
        this.friendLink = {
          linkUrl: "",
          linkDesc: "",
          linkLogo: "",
          linkName: ""
        };
        this.$refs.friendLinkTitle.innerHTML = "添加友链";
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listFriendLinks();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listFriendLinks();
    },
    selectionChange(friendLinkList) {
      this.friendLinkIdList = [];
      friendLinkList.forEach(item => {
        this.friendLinkIdList.push(item.id);
      });
    },
    listFriendLinks() {
      this.axios
        .get("/api/back/friendLinks", {
          params: {
            size: this.size,
            userId: this.userId,
            current: this.current,
            keywords: this.keywords,
            deletedFlag: this.deletedFlag
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.friendLinkList = data.data.pageList;
          this.loading = false;
        });
    },
    listAllUsername(keywords, flag = true) {
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
      if (!this.friendLink.userId && !this.friendLink.id) {
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
      this.axios
        .post("/api/back/friendLink", this.friendLink)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listFriendLinks();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.addOrEditStatus = false;
        });
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
          this.listFriendLinks();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.removeStatus = false;
      });
    },
    updateFriendLinksStatus(id) {
      let param = new URLSearchParams();
      if (id != null) {
        param.append("idList", [id]);
      } else {
        param.append("idList", this.friendLinkIdList);
      }
      param.append("deletedFlag", !this.deletedFlag);
      this.axios.put("/api/back/friendLinks", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listFriendLinks();
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
    userId() {
      this.listFriendLinks();
    },
    deletedFlag() {
      this.listFriendLinks();
    }
  }
};
</script>
