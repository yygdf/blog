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
        <el-select
          v-if="checkWeight(200)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
          clearable
          filterable
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :value="item.userId"
            :label="item.username"
          />
        </el-select>
        <el-input
          v-model="keywords"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入友链名"
          @keyup.enter.native="listLinks"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listLinks"
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
        prop="linkLogo"
        label="友链图标"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <img :src="scope.row.linkLogo" width="40" height="40" />
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
        label="创建时间"
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
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            v-if="deletedFlag"
            title="确定彻底删除吗？"
            @confirm="deleteFriendLinks(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定删除吗？"
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
      <div class="dialog-title-container" slot="title" ref="linkTitle" />
      <el-form :model="friendLink" size="medium" label-width="80">
        <el-form-item label="友链名称">
          <el-input
            v-model="friendLink.linkName"
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
        <el-button type="primary" @click="addOrEditCategory">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listLinks();
  },
  data: function() {
    return {
      options: [
        {
          value: false,
          label: "已发送"
        },
        {
          value: true,
          label: "已删除"
        }
      ],
      friendLink: {
        id: null,
        linkUrl: "",
        linkDesc: "",
        linkLogo: "",
        linkName: ""
      },
      usernameList: [],
      friendLinkList: [],
      friendLinkIdList: [],
      userId: null,
      keywords: null,
      loading: true,
      editStatus: false,
      removeStatus: false,
      addOrEditStatus: false,
      deletedFlag: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(link) {
      if (link != null) {
        this.friendLink = JSON.parse(JSON.stringify(link));
        this.$refs.linkTitle.innerHTML = "修改友链";
      } else {
        this.friendLink.id = null;
        this.friendLink.linkUrl = "";
        this.friendLink.linkDesc = "";
        this.friendLink.linkLogo = "";
        this.friendLink.linkName = "";
        this.$refs.linkTitle.innerHTML = "添加友链";
      }
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listLinks();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listLinks();
    },
    selectionChange(friendLinkList) {
      this.friendLinkIdList = [];
      friendLinkList.forEach(item => {
        this.friendLinkIdList.push(item.id);
      });
    },
    listLinks() {
      this.axios
        .get("/api/back/links", {
          params: {
            size: this.size,
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
    addOrEditCategory() {
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
        .post("/api/back/friendLinks", this.friendLink)
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listLinks();
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
          this.listLinks();
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
          this.listArticles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.editStatus = false;
      });
    }
  }
};
</script>
