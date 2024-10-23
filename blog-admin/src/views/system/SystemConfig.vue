<template>
  <el-card class="main-card">
    <div class="title">
      {{
        this.$i18n.locale === "en_US"
          ? this.$route.meta.nameEn
          : this.$route.name
      }}
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
          style="width: 220px"
          prefix-icon="el-icon-search"
          :placeholder="$t('systemConfig.input')"
          clearable
          @keyup.enter.native="getSystemConfigs(true)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getSystemConfigs(true)"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table v-loading="loading" :data="systemConfigList" border>
      <el-table-column
        v-if="showColumnConfig.configName"
        prop="configName"
        :label="$t('systemConfig.name')"
        align="center"
        width="200"
      />
      <el-table-column
        v-if="showColumnConfig.configValue"
        prop="configValue"
        :label="$t('systemConfig.value')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.configDesc"
        prop="configDesc"
        :label="$t('systemConfig.desc')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
        prop="createTime"
        :label="$t('table.createDate')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.updateTime"
        prop="updateTime"
        :label="$t('table.updateDate')"
        align="center"
        width="120"
      >
        <template slot-scope="scope" v-if="scope.row.updateTime">
          <i class="el-icon-time" style="margin-right:5px" />
          {{ scope.row.updateTime | date }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.operate')" align="center" width="160">
        <template slot="header">
          <el-popover
            placement="bottom"
            :title="$t('table.showColumn')"
            width="160"
          >
            <div>
              <el-checkbox v-model="showColumnConfig.configName">{{
                $t("systemConfig.name")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.configValue">{{
                $t("systemConfig.value")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.configDesc">{{
                $t("systemConfig.desc")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.createTime">{{
                $t("table.createDate")
              }}</el-checkbox>
              <el-checkbox v-model="showColumnConfig.updateTime">{{
                $t("table.updateDate")
              }}</el-checkbox>
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
        </template>
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteSystemConfigs(scope.row.id)"
          >
            <el-button
              :disabled="!scope.row.deletableFlag"
              size="mini"
              type="danger"
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
      :page-sizes="[10, 20]"
      :current-page.sync="current"
      class="pagination-container"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div
        class="dialog-title-container"
        slot="title"
        ref="systemConfigTitle"
      />
      <el-form :model="systemConfig" size="medium" label-width="90px">
        <el-form-item :label="$t('systemConfig.name')">
          <el-input
            :disabled="systemConfig.id != null"
            v-model="systemConfig.configName"
            :ref="systemConfig.id ? '' : 'input'"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('systemConfig.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('systemConfig.value')">
          <el-input
            v-model="systemConfig.configValue"
            :ref="systemConfig.id ? 'input' : ''"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('systemConfig.inputValue')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('systemConfig.desc')">
          <el-input
            v-model="systemConfig.configDesc"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('systemConfig.inputDesc')"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditSystemConfig">
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
    this.getSystemConfigs();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      systemConfigList: [],
      systemConfig: {},
      showColumnConfig: {},
      systemConfigOrigin: {},
      keywords: null,
      oldKeywords: null,
      loading: true,
      addOrEditStatus: false,
      size: 10,
      count: 0,
      current: 1
    };
  },
  methods: {
    openModel(systemConfig) {
      if (systemConfig != null) {
        this.systemConfig = {
          id: systemConfig.id,
          configName: systemConfig.configName,
          configValue: systemConfig.configValue,
          configDesc: systemConfig.configDesc
        };
        this.$refs.systemConfigTitle.innerHTML = this.$t("systemConfig.edit");
      } else {
        this.systemConfig = {
          configName: "",
          configValue: "",
          configDesc: ""
        };
        this.$refs.systemConfigTitle.innerHTML = this.$t("systemConfig.add");
      }
      this.systemConfigOrigin = JSON.parse(JSON.stringify(this.systemConfig));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getSystemConfigs(true);
    },
    currentChange(current) {
      this.current = current;
      this.getSystemConfigs();
    },
    saveColumnConfig() {
      localStorage.setItem(
        "SystemConfigColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("SystemConfigColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("SystemConfigColumnSet")
        );
      } else {
        this.showColumnConfig = {
          configName: true,
          configValue: true,
          configDesc: true,
          createTime: true,
          updateTime: true
        };
      }
    },
    getSystemConfigs(resetCurrentPage) {
      if (resetCurrentPage || this.keywords !== this.oldKeywords) {
        this.current = 1;
        this.oldKeywords = this.keywords;
      }
      let params = {
        size: this.size,
        current: this.current,
        keywords: this.keywords
      };
      params = this.$commonMethod.skipEmptyValue(params);
      this.axios
        .get("/api/back/systemConfigs", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.systemConfigList = data.data.pageList;
          this.loading = false;
        });
    },
    addOrEditSystemConfig() {
      if (this.systemConfig.configName.trim() === "") {
        this.$message.error(this.$t("systemConfig.nameRule1"));
        return false;
      }
      if (this.systemConfig.configValue.trim() === "") {
        this.$message.error(this.$t("systemConfig.valueRule1"));
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.systemConfig,
        this.systemConfigOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.systemConfig.id != null) {
        param.id = this.systemConfig.id;
      }
      this.axios.post("/api/back/systemConfig", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getSystemConfigs();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    deleteSystemConfigs(id) {
      let param = { data: [id] };
      this.axios.delete("/api/back/systemConfigs", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (this.systemConfigList.length === 1) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getSystemConfigs();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
    }
  }
};
</script>
