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
          style="width: 200px"
          prefix-icon="el-icon-search"
          placeholder="请输入资源名"
          clearable
          @keyup.enter.native="getResources"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getResources"
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
      height="720"
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
        <template v-if="scope.row.parentId !== -1" slot-scope="scope">
          <el-switch
            v-model="scope.row.disabledFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateResourceStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="anonymousFlag"
        label="匿名"
        align="center"
        width="80"
      >
        <template v-if="scope.row.parentId !== -1" slot-scope="scope">
          <el-switch
            v-model="scope.row.anonymousFlag"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="updateResourceStatus(scope.row, 10)"
          />
        </template>
      </el-table-column>
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
      <el-table-column
        prop="updateTime"
        label="更新日期"
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
            class="smaller-btn"
            @click="openModel(scope.row, true)"
          >
            <i class="el-icon-plus" /> 新增
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteResources(scope.row.id)"
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
              class="smaller-btn"
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
          <el-input
            v-model="resource.resourceName"
            ref="input"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入资源名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item v-if="resource.parentId" label="资源路径">
          <el-input
            v-model="resource.resourceUri"
            class="word-limit-input"
            style="width: 200px"
            maxlength="50"
            placeholder="请输入资源路径"
            show-word-limit
          />
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
      <el-form
        v-if="resource.parentId"
        :model="resource"
        :inline="true"
        size="medium"
        label-width="80"
      >
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
    this.getResources();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data() {
    return {
      resource: {},
      resourceOrigin: {},
      resourceList: [],
      keywords: "",
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
          this.resource = {
            id: resource.id,
            parentId: resource.parentId,
            resourceUri: resource.resourceUri,
            resourceName: resource.resourceName,
            resourceRequestMethod: resource.resourceRequestMethod,
            disabledFlag: resource.disabledFlag,
            anonymousFlag: resource.anonymousFlag
          };
          this.$refs.resourceTitle.innerHTML = "修改资源";
        } else {
          if (flag) {
            this.resource = {
              parentId: resource.id,
              resourceUri: "",
              resourceName: "",
              resourceRequestMethod: "GET",
              disabledFlag: resource.disabledFlag,
              anonymousFlag: resource.anonymousFlag
            };
            this.$refs.resourceTitle.innerHTML = "添加资源";
          } else {
            this.resource = {
              id: resource.id,
              parentId: null,
              resourceName: resource.resourceName
            };
            this.$refs.resourceTitle.innerHTML = "修改模块";
          }
        }
      }
      this.resourceOrigin = JSON.parse(JSON.stringify(this.resource));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    getResources() {
      let params = {};
      if (this.keywords.trim() !== "") {
        params.keywords = this.keywords;
      }
      this.axios
        .get("/api/back/resources", {
          params
        })
        .then(({ data }) => {
          this.resourceList = data.data;
          this.loading = false;
        });
    },
    deleteResources(id) {
      let param = { data: [id] };
      this.axios.delete("/api/back/resources", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getResources();
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
      let param = this.$commonMethod.skipIdenticalValue(
        this.resource,
        this.resourceOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.resource.id != null) {
        param.id = this.resource.id;
      } else {
        if (this.resource.parentId != null) {
          param.parentId = this.resource.parentId;
        }
      }
      this.axios.post("/api/back/resource", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.getResources();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    updateResourceStatus(resource, type) {
      let param = {
        idList: [resource.id]
      };
      if (type != null) {
        param.type = type;
      }
      this.axios.put("/api/back/resource/status", param).then(({ data }) => {
        if (!data.flag) {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
          if (type === 10) {
            resource.anonymousFlag = !resource.anonymousFlag;
          } else {
            resource.disabledFlag = !resource.disabledFlag;
          }
        }
      });
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
