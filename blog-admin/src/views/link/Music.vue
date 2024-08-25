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
        v-if="type !== 7"
        :disabled="musicIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="editStatus = true"
      >
        批量删除
      </el-button>
      <el-button
        v-else
        :disabled="musicIdList.length === 0"
        type="danger"
        size="small"
        icon="el-icon-minus"
        @click="removeStatus = true"
      >
        批量删除
      </el-button>
      <div style="margin-left:auto">
        <el-select
          v-if="checkWeight(200)"
          v-model="userId"
          size="small"
          style="margin-right:1rem"
          placeholder="请选择用户"
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
          placeholder="请选择"
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
          placeholder="请输入音乐名"
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
          搜索
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
        label="用户"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.musicCover"
        prop="musicCover"
        label="音乐封面"
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
        label="音乐名称"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.musicUrl"
        prop="musicUrl"
        label="音乐链接"
        align="center"
        min-width="240"
      />
      <el-table-column
        v-if="showColumnConfig.author"
        prop="author"
        label="作者"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.album"
        prop="album"
        label="专辑"
        align="center"
        min-width="120"
      />
      <el-table-column
        v-if="showColumnConfig.createTime"
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
      <el-table-column label="操作" align="center" width="160">
        <template slot="header">
          <el-popover placement="bottom" title="选择显示列" width="160">
            <div>
              <el-checkbox v-model="showColumnConfig.username"
                >用户</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.musicCover"
                >音乐封面</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.musicName"
                >音乐名称</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.musicUrl"
                >音乐链接</el-checkbox
              >
              <el-checkbox v-model="showColumnConfig.author">作者</el-checkbox>
              <el-checkbox v-model="showColumnConfig.album">专辑</el-checkbox>
              <el-checkbox v-model="showColumnConfig.createTime"
                >创建日期</el-checkbox
              >
              <div>
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  plain
                  @click="saveColumnConfig"
                >
                  保存
                </el-button>
              </div>
            </div>
            <i slot="reference" class="el-icon-setting table-setting-icon"></i>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <el-button
            v-if="type !== 7"
            type="primary"
            size="mini"
            class="smaller-btn"
            @click="openModel(scope.row)"
          >
            <i class="el-icon-edit" /> 编辑
          </el-button>
          <el-popconfirm
            v-else
            title="确定恢复吗？"
            @confirm="updateMusicsStatus(scope.row.id)"
          >
            <el-button
              type="success"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-refresh-left" /> 恢复
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-if="type !== 7"
            title="确定删除吗？"
            style="margin-left:10px"
            @confirm="updateMusicsStatus(scope.row.id)"
          >
            <el-button
              type="danger"
              size="mini"
              slot="reference"
              class="smaller-btn"
            >
              <i class="el-icon-delete" /> 删除
            </el-button>
          </el-popconfirm>
          <el-popconfirm
            v-else
            title="确定彻底删除吗？"
            style="margin-left:10px"
            @confirm="deleteMusics(scope.row.id)"
          >
            <el-button
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
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="editStatus = false">取 消</el-button>
        <el-button type="primary" @click="updateMusicsStatus(null)">
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
        <el-button type="primary" @click="deleteMusics(null)">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEditStatus" width="30%">
      <div class="dialog-title-container" slot="title" ref="musicTitle" />
      <el-form :model="music" size="medium" label-width="80">
        <el-form-item label="音乐名称">
          <el-input
            v-model="music.musicName"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            placeholder="请输入音乐名称"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="音乐作者">
          <el-input
            v-model="music.author"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            placeholder="请输入音乐作者"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="音乐专辑">
          <el-input
            v-model="music.album"
            ref="input"
            class="word-limit-input form-input-width"
            maxlength="50"
            placeholder="请输入音乐专辑"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="音乐链接">
          <el-input
            v-model="music.musicUrl"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            placeholder="请输入音乐链接"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="封面链接">
          <el-input
            v-model="music.musicCover"
            class="word-limit-input2 form-input-width"
            maxlength="255"
            placeholder="请输入封面链接"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="音乐歌词">
          <el-input
            v-model="music.musicWords"
            type="textarea"
            class="form-input-width"
            :rows="10"
            :placeholder="example"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEditStatus = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditMusic">
          确 定
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
      options: [
        {
          value: null,
          label: "未删除"
        },
        {
          value: 7,
          label: "已删除"
        }
      ],
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
      example:
        "请输入音乐歌词(以下为示例)\n[00:00.00]\n[00:02.70]入戏太深 - 马旭东\n[00:04.15]作词：马旭东\n[00:06.69]作曲：马旭东\n[00:24.50]One Two Three Go!\n[00:27.26]你的笑总是装作很天真\n...\n[03:32.31]谁能懂那些誓言多伤人"
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
        this.$refs.musicTitle.innerHTML = "修改音乐";
      } else {
        this.music = {
          author: "",
          album: "",
          musicUrl: "",
          musicName: "",
          musicCover: "",
          musicWords: ""
        };
        this.$refs.musicTitle.innerHTML = "添加音乐";
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
      } else {
        this.showColumnConfig = {
          username: true,
          author: true,
          album: true,
          musicUrl: true,
          musicName: true,
          musicCover: true,
          createTime: true
        };
      }
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
        this.$message.error("音乐名称不能为空");
        return false;
      }
      if (this.music.musicUrl.trim() === "") {
        this.$message.error("音乐链接不能为空");
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
            title: "成功",
            message: data.message
          });
          this.getMusics();
        } else {
          this.$notify.error({
            title: "失败",
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
            title: "成功",
            message: data.message
          });
          if (param.data.length === this.musicList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMusics();
        } else {
          this.$notify.error({
            title: "失败",
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
            title: "成功",
            message: data.message
          });
          if (param.idList.length === this.musicList.length) {
            this.current = --this.current > 1 ? this.current : 1;
          }
          this.getMusics();
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
      this.editStatus = false;
    }
  },
  watch: {
    type() {
      this.getMusics(true);
    },
    userId() {
      this.getMusics(true);
    }
  }
};
</script>
