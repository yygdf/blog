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
        :disabled="musicIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <el-button
        v-else
        :disabled="musicIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        {{ $t("button.batchDelete") }}
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
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
          :placeholder="$t('music.inputName')"
          clearable
          @keyup.enter.native="getMusics(false)"
        />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left:1rem"
          @click="getMusics(false)"
        >
          {{ $t("button.search") }}
        </el-button>
      </div>
    </div>
    <el-table
      v-loading="loading"
      :data="musicList"
      border
      @selection-change="selectionChange"
    >
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column
        v-if="checkWeight(200) && showColumnConfig.username"
        prop="username"
        :label="$t('table.user')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.musicCover"
        prop="musicCover"
        :label="$t('music.cover')"
        align="center"
        width="80"
      >
        <template slot-scope="scope">
          <el-image
            :src="scope.row.musicCover"
            style="width: 40px;height: 40px;"
            :preview-src-list="[scope.row.musicCover]"
          />
        </template>
      </el-table-column>
      <el-table-column
        v-if="showColumnConfig.musicName"
        prop="musicName"
        :label="$t('music.name')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.musicUrl"
        prop="musicUrl"
        :label="$t('music.url')"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.author"
        prop="author"
        :label="$t('music.author')"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.album"
        prop="album"
        :label="$t('music.album')"
        align="center"
        min-width="120"
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
                v-if="checkWeight(200)"
                v-model="showColumnConfig.username"
                @change="handleColumnCheckedChange"
                >{{ $t("table.user") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.musicCover"
                @change="handleColumnCheckedChange"
                >{{ $t("music.cover") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.musicName"
                @change="handleColumnCheckedChange"
                >{{ $t("music.name") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.musicUrl"
                @change="handleColumnCheckedChange"
                >{{ $t("music.url") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.author"
                @change="handleColumnCheckedChange"
                >{{ $t("music.author") }}</el-checkbox
              >
              <el-checkbox
                v-model="showColumnConfig.album"
                @change="handleColumnCheckedChange"
                >{{ $t("music.album") }}</el-checkbox
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
              @click="getMusics(false)"
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
            @confirm="updateMusicsStatus(scope.row.id)"
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
            @confirm="updateMusicsStatus(scope.row.id)"
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
            @confirm="deleteMusics(scope.row.id)"
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
        <el-button type="primary" @click="updateMusicsStatus(null)">
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
        <el-button type="primary" @click="deleteMusics(null)">
          {{ $t("confirm.yes") }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="musicTitle" />
      <el-form :model="music" size="medium" label-width="80px">
        <el-form-item :label="$t('music.name')">
          <el-input
            v-model="music.musicName"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('music.inputName')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('music.author')">
          <el-input
            v-model="music.author"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('music.inputAuthor')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('music.album')">
          <el-input
            v-model="music.album"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            :placeholder="$t('music.inputAlbum')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('music.url')">
          <el-input
            v-model="music.musicUrl"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('music.inputUrl')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('music.cover')">
          <el-input
            v-model="music.musicCover"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            :placeholder="$t('music.inputCover')"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('music.words')">
          <el-input
            v-model="music.musicWords"
            type="textarea"
            class="form-input-width"
            :rows="10"
            :placeholder="$t('music.inputWords')"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">{{
          $t("button.cancel")
        }}</el-button>
        <el-button type="primary" @click="addOrEditMusic">
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
    this.getMusics();
    this.$nextTick(() => {
      this.$refs.input.focus();
    });
  },
  data: function() {
    return {
      options: [],
      usernameList: [],
      musicList: [],
      musicIdList: [],
      music: {},
      musicOrigin: {},
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
      columnCount: 7,
      columnCheckedCount: 0
    };
  },
  methods: {
    openModel(music) {
      if (music != null) {
        this.music = {
          id: music.id,
          author: music.author,
          album: music.album,
          musicUrl: music.musicUrl,
          musicName: music.musicName,
          musicCover: music.musicCover,
          musicWords: music.musicWords
        };
        this.$refs.musicTitle.innerHTML = this.$t("music.edit");
      } else {
        this.music = {
          author: "",
          album: "",
          musicUrl: "",
          musicName: "",
          musicCover: "",
          musicWords: ""
        };
        this.$refs.musicTitle.innerHTML = this.$t("music.add");
      }
      this.musicOrigin = JSON.parse(JSON.stringify(this.music));
      this.$nextTick(() => {
        this.$refs.input.focus();
      });
      this.addOrEditStatus = true;
    },
    sizeChange(size) {
      this.size = size;
      this.getMusics(true);
    },
    checkWeight(weight) {
      return this.$store.state.weight <= weight;
    },
    currentChange(current) {
      this.current = current;
      this.getMusics();
    },
    selectionChange(selection) {
      this.musicIdList = [];
      selection.forEach(item => {
        this.musicIdList.push(item.id);
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
          author: false,
          album: false,
          musicUrl: false,
          musicName: false,
          musicCover: false,
          createTime: false
        };
        this.columnCheckedCount = 0;
      }
    },
    saveColumnConfig() {
      localStorage.setItem(
        "MusicColumnSet",
        JSON.stringify(this.showColumnConfig)
      );
      document.body.click();
    },
    loadColumnConfig() {
      if (localStorage.getItem("MusicColumnSet")) {
        this.showColumnConfig = JSON.parse(
          localStorage.getItem("MusicColumnSet")
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
        author: true,
        album: true,
        musicUrl: true,
        musicName: true,
        musicCover: true,
        createTime: true
      };
      this.columnCheckedCount = 7;
    },
    getMusics(resetCurrentPage) {
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
        .get("/api/back/musics", {
          params
        })
        .then(({ data }) => {
          this.count = data.data.count;
          this.musicList = data.data.pageList;
          this.loading = false;
        });
    },
    getUsernames(keywords) {
      if (keywords.trim() === "") {
        return;
      }
      this.axios
        .get("/api/back/userAuth/usernames", { params: { keywords } })
        .then(({ data }) => {
          this.usernameList = data.data;
        });
    },
    addOrEditMusic() {
      if (this.music.musicName.trim() === "") {
        this.$message.error(this.$t("music.nameRule1"));
        return false;
      }
      if (this.music.musicUrl.trim() === "") {
        this.$message.error(this.$t("music.urlRule1"));
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.music,
        this.musicOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      if (this.music.id != null) {
        param.id = this.music.id;
      }
      this.axios.post("/api/back/music", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          this.getMusics();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.addOrEditStatus = false;
    },
    deleteMusics(id) {
      let param = {};
      if (id == null) {
        param = { data: this.musicIdList };
      } else {
        param = { data: [id] };
      }
      this.axios.delete("/api/back/musics", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (param.data.length === this.musicList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMusics();
        } else {
          this.$notify.error({
            title: this.$t("failure"),
            message: data.message
          });
        }
      });
      this.removeStatus = false;
    },
    updateMusicsStatus(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.musicIdList;
      }
      if (this.type != null) {
        param.type = this.type;
      }
      this.axios.put("/api/back/musics/status", param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: this.$t("success"),
            message: data.message
          });
          if (param.idList.length === this.musicList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMusics();
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
      this.getMusics(true);
    },
    userId() {
      this.getMusics(true);
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
