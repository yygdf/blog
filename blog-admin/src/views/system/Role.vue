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
      <div style="margin-left:auto">
        <el-input
          v-model="keywords"
          ref="input"
          size="small"
          style="width: 200px"
          prefix-icon="el-icon-search"
          :placeholder="$t('role.inputName')"
          clearable
          @keyup.enter.native="getRoles"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getRoles"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="roleList" height="720" border>
      <el-table-column
        prop="roleName"
        :label="$t('role.name')"
        align="center"
      />
      <el-table-column
        prop="roleDesc"
        :label="$t('role.desc')"
        align="center"
      />
      <el-table-column
        prop="roleWeight"
        :label="$t('role.weight')"
        align="center"
        width="120"
      />
      <el-table-column
        prop="userCount"
        :label="$t('role.userCount')"
        align="center"
        width="120"
      />
      <el-table-column
        prop="disabledFlag"
        :label="$t('switch.disabled')"
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
            @change="updateRoleStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        :label="$t('table.createDate')"
        align="center"
        width="160"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="320">
        <template slot-scope="scope">
          <el-button
            type="info"
            size="mini"
            class="smaller-btn"
            @click="openMenuModel(scope.row)"
          >
            <i class="el-icon-folder-opened" /> {{ $t("role.menuPermission") }}
          </el-button>
          <el-button
            type="info"
            size="mini"
            class="smaller-btn"
            @click="openResourceModel(scope.row)"
          >
            <i class="el-icon-folder-opened" />
            {{ $t("role.resourcePermission") }}
          </el-button>
          <el-button
            type="warning"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteRoles(scope.row.id)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag || scope.row.userCount !== 0"
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
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="roleTitle" />
      <el-form :model="role" size="medium" label-width="90px">
        <el-form-item :label="$t('role.name')">
          <el-input
            v-model="role.roleName"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('role.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('role.desc')">
          <el-input
            v-model="role.roleDesc"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('role.inputDesc')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('role.weight')">
          <el-input-number
            v-model="role.roleWeight"
            class="form-input-width"
            :min="1"
            :max="1000"
            value="1"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item :label="$t('switch.disabled')">
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
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditRole">
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editRoleMenuStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        {{ $t("role.edit2") }}
      </div>
      <el-form :model="role" size="medium" label-width="80px">
        <el-form-item :label="$t('role.name')">
          <el-input v-model="role.roleName" style="width: 200px" disabled />
        </el-form-item>
        <el-form-item :label="$t('role.menuPermission')">
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
        <el-button @click="editRoleMenuStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="editRoleMenu">
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editRoleResourceStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        {{ $t("role.edit3") }}
      </div>
      <el-form :model="role" size="medium" label-width="80px">
        <el-form-item :label="$t('role.name')">
          <el-input v-model="role.roleName" style="width: 200px" disabled />
        </el-form-item>
        <el-form-item :label="$t('role.resourcePermission')">
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
        <el-button @click="editRoleResourceStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="editRoleResource">
          {{ $t("button.save") }}
        </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.getRoles();
    this.getRolePermission();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      roleList: [],
      menuList: [],
      resourceList: [],
      role: {},
      roleOrigin: {},
      keywords: "",
      rootRoleId: null,
      loading: true,
      addOrEditStatus: false,
      editRoleMenuStatus: false,
      editRoleResourceStatus: false,
      menuList2: [],
      menuList3: [],
      resourceList2: [],
      resourceList3: []
    };
  },
  methods: {
    openModel(role) {
      if (role == null) {
        this.role = {
          roleName: "",
          roleDesc: "",
          roleWeight: 1000,
          disabledFlag: false
        };
        this.$refs.roleTitle.innerHTML = this.$t("role.add");
      } else {
        this.role = {
          id: role.id,
          roleName: role.roleName,
          roleDesc: role.roleDesc,
          roleWeight: role.roleWeight,
          disabledFlag: role.disabledFlag
        };
        this.$refs.roleTitle.innerHTML = this.$t("role.edit1");
      }
      this.roleOrigin = JSON.parse(JSON.stringify(this.role));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    checkedKeys(dataList, idArray, idList) {
      dataList.forEach(item => {
        if (item.children == null) {
          if (idArray.some(id => Number(id) === item.id)) {
            idList.push(item.id);
          }
        } else {
          let count = 0;
          item.children.forEach(item => {
            if (idArray.some(id => Number(id) === item.id)) {
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
    getRoles() {
      let params = {};
      if (this.keywords.trim() !== "") {
        params.keywords = this.keywords;
      }
      this.axios
        .get("/api/back/roles", {
          params
        })
        .then(({ data }) => {
          this.rootRoleId = data.data.rootRoleId;
          this.roleList = data.data.dataList;
          this.loading = false;
        });
    },
    getRolePermission() {
      this.axios.get("/api/back/role/permission").then(({ data }) => {
        this.menuList2 = data.data.menusRoleDTOList.map(e => {
          return { id: e.id, label: e.label, children: e.children };
        });
        this.menuList3 = data.data.menusRoleDTOList.map(e => {
          return {
            id: e.id,
            label: e.label2,
            children:
              e.children == null
                ? null
                : e.children.map(item => {
                    return {
                      id: item.id,
                      label: item.label2,
                      children: item.children
                    };
                  })
          };
        });
        this.resourceList2 = data.data.resourcesRoleDTOList.map(e => {
          return { id: e.id, label: e.label, children: e.children };
        });
        this.resourceList3 = data.data.resourcesRoleDTOList.map(e => {
          return {
            id: e.id,
            label: e.label2,
            children:
              e.children == null
                ? null
                : e.children.map(item => {
                    return {
                      id: item.id,
                      label: item.label2,
                      children: item.children
                    };
                  })
          };
        });
        if (this.isEn) {
          this.menuList = this.menuList3;
          this.resourceList = this.resourceList3;
        } else {
          this.menuList = this.menuList2;
          this.resourceList = this.resourceList2;
        }
      });
    },
    deleteRoles(id) {
      let param = { data: [id] };
      this.axios.delete("/api/back/roles", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getRoles();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
        .put("/api/back/role/permission", {
          id,
          menuIdList
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: this.$t("success"),
              message: data.message
            });
            this.getRoles();
          } else {
            this.$notify.error({
              title: this.$t("failure"),
              message: data.message
            });
          }
        });
      this.editRoleMenuStatus = false;
    },
    addOrEditRole() {
      if (this.role.roleName.trim() === "") {
        this.$message.error(this.$t("role.nameRule1"));
        return false;
      }
      if (this.role.roleDesc.trim() === "") {
        this.$message.error(this.$t("role.descRule1"));
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.role,
        this.roleOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.role.id != null) {
        param.id = this.role.id;
      }
      this.axios.post("/api/back/role", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getRoles();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    updateRoleStatus(role) {
      let disabledFlag = role.disabledFlag;
      this.$confirm(
        disabledFlag
          ? this.$t("confirm.content7")
          : this.$t("confirm.content8"),
        this.$t("confirm.tip"),
        {
          confirmButtonText: this.$t("confirm.yes"),
          cancelButtonText: this.$t("confirm.no"),
          type: "warning"
        }
      )
        .then(() => {
          let param = {
            idList: [role.id]
          };
          if (!disabledFlag) {
            param.status = true;
          }
          this.axios.put("/api/back/role/status", param).then(({ data }) => {
            if (data.flag) {
              role.disabledFlag = !disabledFlag;
            } else {
              this.$notify.error({
                title: this.$t("failure"),
                message: data.message
              });
            }
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
        .put("/api/back/role/permission", {
          id,
          resourceIdList
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: this.$t("success"),
              message: data.message
            });
            this.getRoles();
          } else {
            this.$notify.error({
              title: this.$t("failure"),
              message: data.message
            });
          }
        });
      this.editRoleResourceStatus = false;
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  },
  watch: {
    isEn(newVal) {
      if (newVal) {
        this.menuList = this.menuList3;
        this.resourceList = this.resourceList3;
      } else {
        this.menuList = this.menuList2;
        this.resourceList = this.resourceList2;
      }
    }
  }
};
</script>
