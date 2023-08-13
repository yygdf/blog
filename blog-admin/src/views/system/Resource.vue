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
        新增模块
      </el-button>
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入资源名"
          @keyup.enter.native="listResources"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listResources"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="resourceList"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      row-key="id"
    >
      <el-table-column prop="resourceName" label="资源名称" />
      <el-table-column prop="resourceUri" label="资源路径" />
      <el-table-column
        prop="resourceRequestMethod"
        label="请求方式"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.resourceRequestMethod">
          <el-tag :type="tagType(scope.row.resourceRequestMethod)">
            {{ scope.row.resourceRequestMethod }}
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
            :disabled="!checkWeight(100) && !scope.row.deletableFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeResourceStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="anonymousFlag"
        label="匿名"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.anonymousFlag"
            :disabled="!checkWeight(100) && !scope.row.deletableFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeResourceStatus(scope.row)"
          />
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
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button
            :disabled="scope.row.parentId !== -1"
            type="primary"
            size="mini"
            class="smallerBtn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button
            :disabled="!checkWeight(100) && scope.row.deletableFlag"
            type="warning"
            size="mini"
            class="smallerBtn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 修改
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteResource(scope.row.id)"
          >
            <el-button
              :disabled="
                !scope.row.deletableFlag ||
                  (scope.row.children != null &&
                    scope.row.children.length !== 0)
              "
              type="danger"
              size="mini"
              slot="reference"
              class="smallerBtn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="resourceTitle" />
      <el-form :model="resource" size="medium" label-width="80">
        <el-form-item v-if="resource.parentId" label="模块名称">
          <el-select
            v-model="resource.parentId"
            size="small"
            style="margin-right:1rem;width: 200px"
            placeholder="请选择"
          >
            <el-option
              v-for="item in resourceList"
              :key="item.id"
              :value="item.id"
              :label="item.resourceName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="资源名称">
          <el-input v-model="resource.resourceName" ref="input" style="width:200px" />
        </el-form-item>
        <el-form-item v-if="resource.parentId" label="资源路径">
          <el-input v-model="resource.resourceUri" style="width:200px" />
        </el-form-item>
        <el-form-item v-if="resource.parentId" label="请求方式">
          <el-radio-group v-model="resource.resourceRequestMethod">
            <el-radio :label="'GET'">GET</el-radio>
            <el-radio :label="'POST'">POST</el-radio>
            <el-radio :label="'PUT'">PUT</el-radio>
            <el-radio :label="'DELETE'">DELETE</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-form :model="resource" :inline="true" size="medium" label-width="80">
        <el-form-item label="禁用">
          <el-switch
            v-model="resource.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
        <el-form-item label="匿名">
          <el-switch
            v-model="resource.anonymousFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditResource">
          确 定
        </el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listResources();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      resource: {},
      resourceList: [],
      keywords: null,
      loading: true,
      addOrEditStatus: false
    };
  },
  methods: {
    openModel(resource, flag = false) {
      if (resource == null) {
        this.resource = {
          parentId: null,
          resourceName: ""
        };
        this.$refs.resourceTitle.innerHTML = "添加模块";
      } else {
        if (resource.parentId !== -1) {
          this.resource = JSON.parse(JSON.stringify(resource));
          this.$refs.resourceTitle.innerHTML = "修改资源";
        } else {
          if (flag) {
            this.resource = {
              parentId: resource.id,
              resourceUri: "",
              resourceName: "",
              resourceRequestMethod: "GET"
            };
            this.$refs.resourceTitle.innerHTML = "添加资源";
          } else {
            this.resource = JSON.parse(JSON.stringify(resource));
            this.resource.parentId = null;
            this.$refs.resourceTitle.innerHTML = "修改模块";
          }
        }
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    checkWeight(weight = 200) {
      return this.$store.state.weight <= weight;
    },
    listResources() {
      this.axios
        .get("/api/back/resources", {
          params: {
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.resourceList = data.data;
          this.loading = false;
        });
    },
    deleteResource(id) {
      let param = { data: id };
      this.axios.delete("/api/back/resource", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listResources();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    addOrEditResource() {
      let flag = this.resource.parentId == null;
      if (this.resource.resourceName.trim() === "") {
        if (flag) {
          this.$message.error("模块名称不能为空");
          return false;
        }
        this.$message.error("资源名称不能为空");
        return false;
      }
      if (!flag && this.resource.resourceUri.trim() === "") {
        this.$message.error("资源路径不能为空");
        return false;
      }
      if (!flag && this.resource.resourceRequestMethod.trim() === "") {
        this.$message.error("请求方式不能为空");
        return false;
      }
      const {
        id,
        parentId,
        resourceUri,
        resourceName,
        resourceRequestMethod,
        disabledFlag,
        anonymousFlag
      } = this.resource;
      this.axios
        .post("/api/back/resource", {
          id,
          parentId,
          resourceUri,
          resourceName,
          resourceRequestMethod,
          disabledFlag,
          anonymousFlag
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listResources();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.addOrEditStatus = false;
        });
    },
    changeResourceStatus(resource) {
      let param = {
        id: resource.id,
        hiddenFlag: resource.disabledFlag,
        publicFlag: resource.anonymousFlag
      };
      this.axios.put("/api/back/resource/status", param);
    }
  },
  computed: {
    tagType() {
      return function(type) {
        switch (type) {
          case "GET":
            return "success";
          case "POST":
            return "";
          case "PUT":
            return "warning";
          case "DELETE":
            return "danger";
        }
      };
    }
  }
};
</script>

<style scoped>
.smallerBtn {
  padding: 5px;
}
</style>
