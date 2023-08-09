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
        :disabled="tagIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(300)"
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
            :label="item.username"
          />
        </el-select>
        <el-input
          v-model="keywords"
          size="small"
          style="width:200px"
          placeholder="请输入标签名"
          prefix-icon="el-icon-search"
          clearable
          @keyup.enter.native="listTags"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listTags"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      :data="tagList"
      v-loading="loading"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        v-if="checkWeight(300)"
        prop="username"
        label="用户"
        align="center"
        width="120"
      />
      <el-table-column prop="tagName" label="标签名" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.tagName }}
          </el-tag>
        </template>
      </el-table-column>
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
      <el-table-column
        prop="updateTime"
        label="更新时间"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteTags(scope.row.id)"
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
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">取 消</el-button>
        <el-button type="primary" @click="deleteTags(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="tagTitle" />
      <el-form :model="tag" size="medium" label-width="80">
        <el-form-item label="标签名">
          <el-input v-model="tag.tagName" style="width:200px" :maxLength="50" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditTag">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listTags();
  },
  data: function() {
    return {
      tag: {},
      tagList: [],
      tagIdList: [],
      usernameList: [],
      userId: null,
      keywords: null,
      loading: true,
      removeStatus: false,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(tag) {
      if (tag != null) {
        this.tag = JSON.parse(JSON.stringify(tag));
        this.$refs.tagTitle.innerHTML = "修改标签";
      } else {
        this.tag = {
          tagName: ""
        };
        this.$refs.tagTitle.innerHTML = "添加标签";
      }
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.listTags();
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.listTags();
    },
    selectionChange(tagList) {
      this.tagIdList = [];
      tagList.forEach(item => {
        this.tagIdList.push(item.id);
      });
    },
    listTags() {
      this.axios
        .get("/api/back/tags", {
          params: {
            size: this.size,
            userId: this.userId,
            current: this.current,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.tagList = data.data.pageList;
          this.loading = false;
        });
    },
    listAllUsername(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/user/username", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    },
    deleteTags(id) {
      let param = {};
      if (id == null) {
        param = { data: this.tagIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/tags", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listTags();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    addOrEditTag() {
      if (this.tag.tagName.trim() === "") {
        this.$message.error("标签名不能为空");
        return false;
      }
      this.axios.post("/api/back/tag", this.tag).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listTags();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    }
  },
  watch: {
    userId() {
      this.listTags();
    }
  }
};
</script>
