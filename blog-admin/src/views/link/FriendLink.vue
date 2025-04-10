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
      <el-button
        v-if="type !== 7"
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="friendLinkIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          :placeholder="$t('input.selectUser')"
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
          :placeholder="$t('input.select')"
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
          :placeholder="$t('friendLink.inputName')"
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
          {{ $t("button.search") }}
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
        v-if="showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.linkLogo"
        prop="linkLogo"
        :label="$t('friendLink.logo')"
        align="center"
        width="80"
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
        v-if="showColumnConfig.linkName"
        prop="linkName"
        :label="$t('friendLink.name')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.linkUrl"
        prop="linkUrl"
        :label="$t('friendLink.url')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.linkDesc"
        prop="linkDesc"
        :label="$t('friendLink.desc')"
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
      <el-table-column :label="$t('table.operate')" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" width="160">
            <div>
              <el-checkbox
                :indeterminate="
                  columnCheckedCount > 0 && columnCheckedCount < columnCount
                "
                :value="columnCheckedCount === columnCount"
                @change="handleColumnCheckedAllChange"
                >{{ $t("table.showColumn") }}</el-checkbox
              >
              <el-divider></el-divider>
              <el-checkbox
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.linkLogo"
                @change="handleColumnCheckedChange"
                >{{ $t("friendLink.logo") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.linkName"
                @change="handleColumnCheckedChange"
                >{{ $t("friendLink.name") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.linkUrl"
                @change="handleColumnCheckedChange"
                >{{ $t("friendLink.url") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.linkDesc"
                @change="handleColumnCheckedChange"
                >{{ $t("friendLink.desc") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.createTime"
                @change="handleColumnCheckedChange"
                >{{ $t("table.createDate") }}</el-checkbox
              >
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
          <el-tooltip
            class="item"
            effect="dark"
            :content="$t('table.refresh')"
            placement="top"
          >
            <i
              class="el-icon-refresh table-refresh-icon"
              @click="getFriendLinks(false)"
            ></i>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> {{ $t("button.edit") }}
          </el-button>
          <el-popconfirm
            v-else
            :title="$t('confirm.content2')"
            @confirm="updateFriendLinksStatus(scope.row.id)"
          >
            <el-button
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> {{ $t("button.restore") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            :title="$t('confirm.content3')"
            style="margin-left:10px"
            @confirm="updateFriendLinksStatus(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> {{ $t("button.delete") }}
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            :title="$t('confirm.content4')"
            style="margin-left:10px"
            @confirm="deleteFriendLinks(scope.row.id)"
          >
            <el-button
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
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content5") }}</div>
      <div slot="footer">
        <el-button @click="editStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="updateFriendLinksStatus(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="removeStatus" width="30%">
      <div class="dialog-title-container" slot="title">
        <i class="el-icon-warning" style="color:#ff9900" />{{
          $t("confirm.tip")
        }}
      </div>
      <div style="font-size:1rem">{{ $t("confirm.content6") }}</div>
      <div slot="footer">
        <el-button @click="removeStatus = false">{{
          $t("confirm.no")
        }}</el-button>
        <el-button type="primary" @click="deleteFriendLinks(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="friendLinkTitle" />
      <el-form :model="friendLink" size="medium" label-width="80px">
        <el-form-item :label="$t('friendLink.user')">
          <el-select
            :disabled="friendLink.id != null"
            v-model="friendLink.userId"
            :ref="friendLink.id ? '' : 'input'"
            class="form-input-width"
            :placeholder="$t('input.selectUser')"
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
        <el-form-item :label="$t('friendLink.name')">
          <el-input
            v-model="friendLink.linkName"
            :ref="friendLink.id ? 'input' : ''"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('friendLink.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('friendLink.desc')">
          <el-input
            v-model="friendLink.linkDesc"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('friendLink.inputDesc')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('friendLink.logo')">
          <el-input
            v-model="friendLink.linkLogo"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('friendLink.inputLogo')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('friendLink.url')">
          <el-input
            v-model="friendLink.linkUrl"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('friendLink.inputUrl')"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditFriendLink">
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
    this.getFriendLinks();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
      usernameList: [],
      friendLinkList: [],
      usernameListAdd: [],
      friendLinkIdList: [],
      friendLink: {},
      friendLinkOrigin: {},
      showColumnConfig: {},
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
      current: 1,
      columnCount: 6,
      columnCheckedCount: 0
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
        this.$refs.friendLinkTitle.innerHTML = this.$t("friendLink.edit");
      } else {
        this.friendLink = {
          userId: null,
          linkUrl: "",
          linkDesc: "",
          linkLogo: "",
          linkName: ""
        };
        this.$refs.friendLinkTitle.innerHTML = this.$t("friendLink.add");
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
    selectionChange(selection) {
      this.friendLinkIdList = [];
      selection.forEach(item => {
        this.friendLinkIdList.push(item.id);
      });
    },
    handleColumnCheckedChange(value) {
      if (value) {
        this.columnCheckedCount++;
      } else {
        this.columnCheckedCount--;
      }
    },
    handleColumnCheckedAllChange(value) {
      if (value) {
        this.initColumnConfig();
      } else {
        this.showColumnConfig = {
          username: false,
          linkLogo: false,
          linkName: false,
          linkUrl: false,
          linkDesc: false,
          createTime: false
        };
        this.columnCheckedCount = 0;
      }
    },
    saveColumnConfig() {
      localStorage.setItem(
        "FriendLinkColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("FriendLinkColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("FriendLinkColumnSet")
        );
        this.columnCheckedCount = Object.values(this.showColumnConfig).reduce(
          (count, value) => {
            return value ? ++count : count;
          },
          0
        );
      } else {
        this.initColumnConfig();
      }
    },
    initColumnConfig() {
      this.showColumnConfig = {
        username: true,
        linkLogo: true,
        linkName: true,
        linkUrl: true,
        linkDesc: true,
        createTime: true
      };
      this.columnCheckedCount = 6;
    },
    getFriendLinks(resetCurrentPage) {
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
        this.$message.error(this.$t("friendLink.userRule1"));
        return false;
      }
      if (this.friendLink.linkName.trim() === "") {
        this.$message.error(this.$t("friendLink.nameRule1"));
        return false;
      }
      if (this.friendLink.linkDesc.trim() === "") {
        this.$message.error(this.$t("friendLink.descRule1"));
        return false;
      }
      if (this.friendLink.linkLogo.trim() === "") {
        this.$message.error(this.$t("friendLink.logoRule1"));
        return false;
      }
      if (this.friendLink.linkUrl.trim() === "") {
        this.$message.error(this.$t("friendLink.urlRule1"));
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
            title: this.$t("success"),
            message: data.message
          });
          this.getFriendLinks();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.friendLinkList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getFriendLinks();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.friendLinkList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getFriendLinks();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  computed: {
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  },
  watch: {
    type() {
      this.getFriendLinks(true);
    },
    userId() {
      this.getFriendLinks(true);
    },
    isEn: {
      handler() {
        this.options = [
          {
            value: null,
            label: this.$t("option.available")
          },
          {
            value: 7,
            label: this.$t("option.deleted")
          }
        ];
      },
      immediate: true
    }
  }
};
</script>
