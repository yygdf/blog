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
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width:200px"
          prefix-icon="el-icon-search"
          placeholder="请输入角色名"
          clearable
          @keyup.enter.native="listRoles"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="listRoles"
        >
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="roleList" v-loading="loading">
      <el-table-column prop="roleName" label="角色名称" align="center" />
      <el-table-column prop="roleDesc" label="角色描述" align="center" />
      <el-table-column
        prop="roleWeight"
        label="角色权重"
        align="center"
        width="120"
      />
      <el-table-column
        prop="userCount"
        label="用户数"
        align="center"
        width="80"
      />
      <el-table-column
        prop="disabledFlag"
        label="禁用"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.disabledFlag"
            :disabled="scope.row.id === rootRoleId"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            @change="changeRoleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建日期"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="400">
        <template slot-scope="scope">
          <el-button type="info" size="mini" @click="openMenuModel(scope.row)">
            <i class="el-icon-folder-opened" /> 菜单权限
          </el-button>
          <el-button
            type="info"
            size="mini"
            @click="openResourceModel(scope.row)"
          >
            <i class="el-icon-folder-opened" /> 资源权限
          </el-button>
          <el-button type="primary" size="mini" @click="openModel(scope.row)">
            <i class="el-icon-edit" /> 修改
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="deleteRole(scope.row.id)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag || scope.row.userCount !== 0"
              type="danger"
              size="mini"
              slot="reference"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="roleTitle" />
      <el-form :model="role" size="medium" label-width="80">
        <el-form-item label="角色名称">
          <el-input
            v-model="role.roleName"
            :disabled="role.id != null"
            :ref="role.id ? '' : 'input'"
            :maxlength="50"
            style="width:200px"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input
            v-model="role.roleDesc"
            :ref="role.id ? 'input' : ''"
            :maxlength="50"
            style="width:200px"
          />
          <span style="color: red;"> *</span>
        </el-form-item>
        <el-form-item label="角色权重">
          <el-input-number
            v-model="role.roleWeight"
            :min="1"
            :max="1000"
            value="1"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="禁用">
          <el-switch
            v-model="role.disabledFlag"
            :disabled="role.id === rootRoleId"
            :active-value="true"
            :inactive-value="false"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditRole">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editRoleMenuStatus" width="30%">
      <div class="dialog-title-container" slot="title">修改菜单权限</div>
      <el-form :model="role" size="medium" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" style="width:200px" disabled />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            :data="menuList"
            :default-checked-keys="role.menuIdList"
            ref="menuTree"
            node-key="id"
            show-checkbox
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editRoleMenuStatus = false">取 消</el-button>
        <el-button type="primary" @click="editRoleMenu">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editRoleResourceStatus" width="30%">
      <div class="dialog-title-container" slot="title">修改资源权限</div>
      <el-form :model="role" size="medium" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" style="width:200px" disabled />
        </el-form-item>
        <el-form-item label="资源权限">
          <el-tree
            :data="resourceList"
            :default-checked-keys="role.resourceIdList"
            ref="resourceTree"
            node-key="id"
            show-checkbox
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editRoleResourceStatus = false">取 消</el-button>
        <el-button type="primary" @click="editRoleResource">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.listRoles();
    this.listRoleOptions();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      role: {},
      roleList: [],
      menuList: [],
      resourceList: [],
      keywords: null,
      rootRoleId: null,
      loading: true,
      addOrEditStatus: false,
      editRoleMenuStatus: false,
      editRoleResourceStatus: false
    };
  },
  methods: {
    openModel(role) {
      if (role == null) {
        this.role = {
          roleName: "",
          roleDesc: "",
          roleWeight: 1000
        };
        this.$refs.roleTitle.innerHTML = "新增角色";
      } else {
        this.role = {
          id: role.id,
          roleName: role.roleName,
          roleDesc: role.roleDesc,
          roleWeight: role.roleWeight,
          disabledFlag: role.disabledFlag
        };
        this.$refs.roleTitle.innerHTML = "修改角色";
      }
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    checkedKeys(dataList, idArray, idList) {
      dataList.forEach(item => {
        if (item.children == null) {
          if (idArray.some(id => id == item.id)) {
            idList.push(item.id);
          }
        } else {
          let count = 0;
          item.children.forEach(item => {
            if (idArray.some(id => id == item.id)) {
              idList.push(item.id);
              count++;
            }
          });
          if (count === item.children.length) {
            idList.push(item.id);
          }
        }
      });
    },
    openMenuModel(role) {
      this.$nextTick(function() {
        this.$refs.menuTree.setCheckedKeys([]);
      });
      let menuIdList = [];
      if (role.menuIdList != null) {
        const idArray = role.menuIdList.split(",");
        this.checkedKeys(this.menuList, idArray, menuIdList);
      }
      this.role = {
        id: role.id,
        roleName: role.roleName,
        menuIdList: menuIdList
      };
      this.editRoleMenuStatus = true;
    },
    openResourceModel(role) {
      this.$nextTick(function() {
        this.$refs.resourceTree.setCheckedKeys([]);
      });
      let resourceIdList = [];
      if (role.resourceIdList != null) {
        const idArray = role.resourceIdList.split(",");
        this.checkedKeys(this.resourceList, idArray, resourceIdList);
      }
      this.role = {
        id: role.id,
        roleName: role.roleName,
        resourceIdList: resourceIdList
      };
      this.editRoleResourceStatus = true;
    },
    listRoles() {
      this.axios
        .get("/api/back/roles", {
          params: {
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.rootRoleId = data.data.rootRoleId;
          this.roleList = data.data.dataList;
          this.loading = false;
        });
    },
    listRoleOptions() {
      this.axios.get("/api/back/role/option").then(({ data }) => {
        this.menuList = data.data.menusDTOList;
        this.resourceList = data.data.resourcesDTOList;
      });
    },
    deleteRole(id) {
      let param = { data: id };
      this.axios.delete("/api/back/role", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listRoles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    editRoleMenu() {
      let id = this.role.id;
      let menuIdList = this.$refs.menuTree
        .getCheckedKeys()
        .concat(this.$refs.menuTree.getHalfCheckedKeys());
      this.axios
        .put("/api/back/role/option", {
          id,
          menuIdList
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listRoles();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.editRoleMenuStatus = false;
        });
    },
    addOrEditRole() {
      if (this.role.roleName.trim() === "") {
        this.$message.error("角色名称不能为空");
        return false;
      }
      if (this.role.roleDesc.trim() === "") {
        this.$message.error("角色描述不能为空");
        return false;
      }
      this.axios.post("/api/back/role", this.role).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: "成功",
            message: data.message
          });
          this.listRoles();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
        this.addOrEditStatus = false;
      });
    },
    changeRoleStatus(role) {
      let disabledFlag = role.disabledFlag;
      let text = disabledFlag ? "启用" : "禁用";
      this.$confirm("是否" + text + "该角色?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          role.disabledFlag = !disabledFlag;
          this.axios.put("/api/back/role/status", {
            id: role.id,
            publicFlag: !disabledFlag
          });
        })
        .catch(() => {});
    },
    editRoleResource() {
      let id = this.role.id;
      let resourceIdList = this.$refs.resourceTree
        .getCheckedKeys()
        .concat(this.$refs.resourceTree.getHalfCheckedKeys());
      this.axios
        .put("/api/back/role/option", {
          id,
          resourceIdList
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: "成功",
              message: data.message
            });
            this.listRoles();
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
          this.editRoleResourceStatus = false;
        });
    }
  }
};
</script>
