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
        type="danger"
        size="small"
        icon="el-icon-minus"
        :disabled="tagIdList.length === 0"
        @click="remove = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="userId"
          placeholder="请选择用户"
          size="small"
          style="margin-right:1rem"
          clearable
          filterable
          v-if="checkWeight()"
        >
          <el-option
            v-for="item in usernameList"
            :key="item.userId"
            :label="item.username"
            :value="item.userId"
          />
        </el-select>
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入标签名"
          style="width:200px"
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
      border
      :data="tagList"
      v-loading="loading"
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        prop="username"
        label="用户"
        align="center"
        v-if="checkWeight()"
      />
      <el-table-column prop="tagName" label="标签名" align="center">
        <template slot-scope="scope">
          <el-tag>
            {{ scope.row.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间" align="center">
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
            style="margin-left:1rem"
            @confirm="deleteTag(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference">
              删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination-container"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper"
    />
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteTag(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEdit" width="30%">
      <div class="dialog-title-container" slot="title" ref="tagTitle" />
      <el-form label-width="80px" size="medium" :model="tag">
        <el-form-item label="标签名">
          <el-input style="width:220px" v-model="tag.tagName" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
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
    this.listAllUsername();
  },
  data: function() {
    return {
      tag: {
        id: null,
        tagName: ""
      },
      tagList: [],
      tagIdList: [],
      usernameList: [],
      keywords: null,
      userId: null,
      loading: true,
      remove: false,
      addOrEdit: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    listAllUsername() {
      if (this.checkWeight()) {
        this.axios.get("/api/back/user/username").then(({ data }) => {
          this.usernameList = data.data;
        });
      }
    },
    selectionChange(tagList) {
      this.tagIdList = [];
      tagList.forEach(item => {
        this.tagIdList.push(item.id);
      });
    },
    sizeChange(size) {
      this.size = size;
      this.listTags();
    },
    currentChange(current) {
      this.current = current;
      this.listTags();
    },
    deleteTag(id) {
      var param = {};
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
      this.remove = false;
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
          this.tagList = data.data.pageList;
          this.count = data.data.count;
          this.loading = false;
        });
    },
    openModel(tag) {
      if (tag != null) {
        this.tag = JSON.parse(JSON.stringify(tag));
        this.$refs.tagTitle.innerHTML = "修改标签";
      } else {
        this.tag.id = null;
        this.tag.tagName = "";
        this.$refs.tagTitle.innerHTML = "添加标签";
      }
      this.addOrEdit = true;
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
      this.addOrEdit = false;
    },
    checkWeight() {
      return this.$store.state.weight <= 300;
    }
  },
  watch: {
    userId() {
      this.listTags();
    }
  }
};
</script>
