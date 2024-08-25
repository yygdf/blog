<template>
  <div>
    <transition name="dis_list">
      <div class="list_box" v-if="musicListState">
        <transition name="music_alert">
          <span class="music_alert" v-if="musicAlertState">
            {{ musicAlertVal }}
          </span>
        </transition>
        <div class="list_close" @click="switchMusicListState">x</div>
        <div class="music_list">
          <div class="list_l">
            <ul class="music_type">
              <li
                v-for="(item, index) in musicTypeList"
                :key="index"
                @click="switchMusicType(item.type)"
                :class="{ type_active: item.type === currentMusicType }"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="list_title">
              <span style="font-size: 14px;">歌曲列表</span>
              <img
                :src="playModeButton"
                alt=""
                class="music_state"
                @click="switchPlayMode"
              />
              <div class="music_search_box" v-if="false">
                <input
                  type="text"
                  class="music_search"
                  v-model="musicSearchVal"
                  placeholder="搜索歌曲"
                />
                <transition name="music_search">
                  <ul class="search_list" v-if="musicSearchVal !== ''">
                    <li
                      v-for="(item, index) in musicSearchList"
                      :key="index"
                      @click="listAddMusic(item)"
                    >
                      <span class="music_search_name">{{
                        item.musicName
                      }}</span>
                      <span class="music_search_ar">
                        {{ item.author }}
                      </span>
                    </li>
                  </ul>
                </transition>
              </div>
            </div>
            <div class="music_ul_title">
              <span>歌曲</span><span>歌手</span><span>专辑</span>
            </div>
            <ul class="list">
              <li
                v-for="(item, index) in currentPageMusicList"
                :key="index"
                @mouseover="activeMusicIndexChange(index, item.id, false)"
                @mouseleave="activeMusicIndexChange(null, null, true)"
                @dblclick="
                  listPlayMusic((currentListPage - 1) * pageSize + index)
                "
              >
                <div
                  class="this_music_shelter"
                  v-if="
                    (currentListPage - 1) * pageSize + index ===
                      currentMusicIndex
                  "
                ></div>
                <span>{{ item.musicName }}</span
                ><span>{{ item.author }}</span
                ><span>{{ item.album }}</span>
                <transition name="list_button">
                  <div class="music_button" v-if="activeMusicIndex === index">
                    <div
                      v-if="
                        (currentListPage - 1) * pageSize + index !==
                          currentMusicIndex
                      "
                      class="list_play"
                      title="播放这首歌"
                      :style="{ backgroundImage: 'url(' + listPlay + ')' }"
                      @click="
                        listPlayMusic((currentListPage - 1) * pageSize + index)
                      "
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      :title="playState ? '暂停播放' : '继续播放'"
                      :style="{ backgroundImage: 'url(' + listPlay + ')' }"
                      @click="controlButtonClick"
                    ></div>
                    <div
                      v-if="currentMusicType !== 0"
                      class="list_play"
                      :title="musicAddStatus ? '已添加' : '添加到本地歌曲'"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="musicAddStatus ? '' : listAddMusic(item)"
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      title="移除"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="listRemoveMusic(item.id, false)"
                    ></div>
                    <div
                      v-if="currentMusicType !== 2"
                      class="list_play"
                      :title="musicCollectStatus ? '已收藏' : '收藏'"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="musicCollectStatus ? '' : listCollectMusic(item)"
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      title="移除"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="listRemoveMusic(item.id, true)"
                    ></div>
                  </div>
                </transition>
              </li>
            </ul>
            <div class="list_page">
              <div
                class="page_last"
                v-if="currentListPage !== 1"
                @click="pageChange(true)"
              >
                上一页&lt;
              </div>
              <div class="page_middle">第 {{ currentListPage }} 页</div>
              <div
                class="page_next"
                v-if="
                  currentListPage <
                    Math.ceil(currentMusicList.length / pageSize)
                "
                @click="pageChange(false)"
              >
                >下一页
              </div>
            </div>
          </div>
          <div class="list_r">
            <img class="music_list_bg" :src="musicCover" alt="" />
            <div
              class="music_list_shelter"
              :style="{ backgroundImage: 'url(' + listPan + ')' }"
            ></div>
          </div>
        </div>
      </div>
    </transition>
    <div class="bbox" :class="{ bbox_active: musicBoxState }">
      <div
        class="pan"
        :style="{ backgroundImage: 'url(' + pan + ')' }"
        :class="{ pan_active: musicBoxState }"
        @click="switchMusicBoxState"
      >
        <img :src="musicCover" alt="" class="pan_c" />
      </div>
      <div
        class="box"
        :style="{ backgroundImage: 'url(' + musicCover + ')' }"
        :class="{ box_active: musicBoxState }"
        @dblclick="switchMusicListState"
      >
        <div
          class="music_shelter_2"
          :style="{ backgroundImage: 'url(' + musicCover + ')' }"
          :class="{ shelter_active: musicBoxState }"
        ></div>
        <div
          class="music_shelter"
          :style="{ backgroundImage: 'url(' + musicCover + ')' }"
          :class="{ shelter_active: musicBoxState }"
        ></div>
        <div class="music_shelter_3"></div>
        <div class="music_dis">
          <div class="dis_list" @click="switchMusicListState">···</div>
          <div class="dis_board" @click="switchMusicBoxState">X</div>
          <p class="music_title">{{ musicName }}</p>
          <p class="music_intro">{{ author }}</p>
          <ul class="music_words">
            <div class="music_words_box" :style="{ top: wordsTop + 'px' }">
              <li
                v-for="(item, index) in musicWords"
                :key="index"
                class="music_word"
                :class="{ word_highlight: wordIndex === index }"
              >
                {{ item }}
              </li>
            </div>
          </ul>
        </div>
        <div class="control_box">
          <div class="control_button_prev" @click="musicChange(true)">
            <img :src="prev" alt="" class="control_icon" />
          </div>
          <div class="control_button">
            <img :src="playIcon" alt="" class="control_icon" />
          </div>
          <div class="control_button_next" @click="musicChange(false)">
            <img :src="next" alt="" class="control_icon" />
          </div>
          <div class="progress">
            <div class="progress_c" :style="{ width: currentProgress }">
              <div class="progress_circle">
                <div class="progress_circle_c"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <video
        id="music"
        autoplay="autoplay"
        muted
        :src="musicUrl"
        name="media"
      ></video>
    </div>
  </div>
</template>
<script>
import {
  getSiteMusic,
  getDefaultMusic,
  getCollectionMusic,
  collectMusic
} from "./api/music";
import pan from "./img/pan.png";
import play from "./img/play.png";
import pause from "./img/pause.png";
import prev from "./img/prev.png";
import next from "./img/next.png";
import add from "./img/add.png";
import listPan from "./img/list_pan.png";
import listPlay from "./img/list_play.png";
import state0 from "./img/state_0.png";
import state1 from "./img/state_1.png";
import state2 from "./img/state_2.png";
import music from "./img/music.jpg";
import $ from "jquery";

export default {
  name: "Player",
  data() {
    return {
      o: 0,
      top: 0,
      pan,
      play,
      pause,
      prev,
      next,
      add,
      listPan,
      listPlay,
      state0,
      state1,
      state2,
      musicWords: [],
      musicCover: "",
      musicName: "",
      musicUrl: "",
      author: "",
      playState: true,
      playIcon: play,
      wordsTime: [],
      wordsTop: 0,
      wordIndex: 0,
      currentProgress: "0%",
      siteMusicList: [],
      localMusicList: [],
      collectionMusicList: [],
      currentMusicList: [],
      currentMusicIndex: -1,
      activeMusicIndex: -1,
      currentListPage: -1,
      currentMusicType: -1,
      musicTypeList: [
        { name: "热歌榜", type: 3778678 },
        { name: "新歌榜", type: 3779629 },
        { name: "飙升榜", type: 19723756 },
        { name: "嘻哈榜", type: 991319590 },
        { name: "站点歌曲", type: 1 },
        { name: "我的收藏", type: 2 },
        { name: "本地歌曲", type: 0 }
      ],
      playMode: 0,
      playModeButton: state0,
      musicSearchVal: "",
      musicSearchList: [],
      musicAlertVal: "",
      musicAlertTimer: "",
      musicAlertState: false,
      musicBoxState: false,
      musicListState: false,
      switchMusicTypeFlag: false,
      musicAddStatus: false,
      musicCollectStatus: false
    };
  },
  mounted() {
    this.Player();
  },
  created() {
    if (localStorage.getItem("localMusicList") == null) {
      localStorage.setItem("localMusicList", JSON.stringify(getDefaultMusic()));
    }
    this.switchMusicType(0);
    this.currentMusicIndex = 0;
    this.getMusicInfo();
  },
  methods: {
    musicAlert(val) {
      this.musicAlertState = true;
      this.musicAlertVal = val;
      clearTimeout(this.musicAlertTimer);
      this.musicAlertTimer = setTimeout(() => {
        this.musicAlertState = false;
        this.musicAlertVal = "";
      }, 2000);
    },
    listAddMusic(item) {
      this.localMusicList.push(item);
      localStorage.setItem(
        "localMusicList",
        JSON.stringify(this.localMusicList)
      );
      this.musicAlert("添加成功!");
      this.musicAddStatus = true;
    },
    listCollectMusic(item) {
      if (this.userId == null) {
        this.musicAlert("该功能需要登录才能够使用哦!");
        return;
      }
      collectMusic(item.id).then(({ data }) => {
        if (data.flag) {
          this.musicAlert("收藏成功!");
          this.musicCollectStatus = true;
          this.collectionMusicList.push(item);
        } else {
          this.musicAlert("收藏失败!");
        }
      });
    },
    listRemoveMusic(id, flag) {
      if (flag) {
        collectMusic(id).then(({ data }) => {
          if (data.flag) {
            this.musicAlert("移除成功!");
            let index = this.collectionMusicList.findIndex(
              item => item.id === id
            );
            this.collectionMusicList.splice(index, 1);
          } else {
            this.musicAlert("移除失败!");
          }
        });
      } else {
        let index = this.localMusicList.findIndex(item => item.id === id);
        this.localMusicList.splice(index, 1);
        localStorage.setItem(
          "localMusicList",
          JSON.stringify(this.localMusicList)
        );
        this.musicAlert("移除成功!");
      }
    },
    switchPlayMode() {
      if (this.playMode === 0) {
        this.playMode = 1;
        this.playModeButton = this.state1;
        this.musicAlert("已切换为单曲循环模式");
      } else if (this.playMode === 1) {
        this.playMode = 2;
        this.playModeButton = this.state2;
        this.musicAlert("已切换为随机播放模式");
      } else {
        this.playMode = 0;
        this.playModeButton = this.state0;
        this.musicAlert("已切换为列表循环模式");
      }
    },
    switchMusicListState() {
      if (!this.musicListState) {
        this.musicBoxState = false;
      }
      this.musicListState = !this.musicListState;
    },
    pageChange(isLast) {
      this.activeMusicIndex = -1;
      if (isLast) {
        this.currentListPage--;
      } else {
        this.currentListPage++;
      }
    },
    musicChange(isPrev) {
      if (isPrev) {
        this.listPlayMusic(--this.currentMusicIndex);
      } else {
        this.listPlayMusic(++this.currentMusicIndex);
      }
    },
    listPlayMusic(index) {
      this.currentMusicIndex =
        index > this.currentMusicList.length - 1 || index < 0 ? 0 : index;
      this.getMusicInfo();
      this.top = 0;
      this.o = 0;
      this.wordIndex = 0;
      this.wordsTop = 0;
      this.currentProgress = "0%";
      let player = $("#music")[0];
      this.switchMusicTypeFlag = false;
      player.currentTime = 0;
      if (!this.playState) {
        $(".control_button").click();
      }
    },
    activeMusicIndexChange(index, id, flag) {
      if (flag) {
        this.activeMusicIndex = -1;
      } else {
        this.activeMusicIndex = index;
        if (this.currentMusicType !== 0) {
          this.musicAddStatus = this.localMusicList.some(e => e.id === id);
        }
        if (this.currentMusicType !== 2) {
          this.musicCollectStatus = this.collectionMusicList.some(
            e => e.id === id
          );
        }
      }
    },
    switchMusicBoxState() {
      this.musicBoxState = !this.musicBoxState;
    },
    async switchMusicType(type) {
      if (this.currentMusicType !== type) {
        this.switchMusicTypeFlag = true;
        if (type === 0) {
          if (this.localMusicList.length === 0) {
            this.localMusicList = JSON.parse(
              localStorage.getItem("localMusicList")
            );
          }
          this.currentMusicList = this.localMusicList;
          this.currentMusicType = 0;
        } else if (type === 1) {
          if (this.siteMusicList.length === 0) {
            await getSiteMusic().then(({ data }) => {
              this.siteMusicList = data.data;
              if (data.data.length === 0) {
                this.musicAlert("暂无歌曲提供~");
              }
            });
          }
          this.currentMusicList = this.siteMusicList;
          this.currentMusicType = 1;
        } else if (type === 2) {
          if (this.userId == null) {
            this.musicAlert("该功能需要登录才能够使用哦!");
            return;
          }
          if (this.collectionMusicList.length === 0) {
            await getCollectionMusic().then(({ data }) => {
              this.collectionMusicList = data.data;
            });
          }
          this.currentMusicList = this.collectionMusicList;
          this.currentMusicType = 2;
        } else {
          this.musicAlert("该榜单暂未开放哦~");
          return;
        }
        this.currentListPage = 1;
        this.currentMusicIndex = -1;
        this.activeMusicIndex = -1;
      }
    },
    getMusicInfo() {
      this.musicUrl = this.currentMusicList[this.currentMusicIndex].musicUrl;
      this.musicCover = this.currentMusicList[this.currentMusicIndex].musicCover
        ? this.currentMusicList[this.currentMusicIndex].musicCover
        : music;
      this.musicName = this.currentMusicList[this.currentMusicIndex].musicName;
      this.author = this.currentMusicList[this.currentMusicIndex].author;
      if (this.currentMusicList[this.currentMusicIndex].musicWords) {
        let info = this.Cut(
          this.currentMusicList[this.currentMusicIndex].musicWords
        );
        this.musicWords = info.wordArr;
        this.wordsTime = info.timeArr;
      } else {
        this.musicWords = ["暂无歌词"];
        this.wordsTime = [0];
      }
    },
    Ltrim(s) {
      return s.replace(/(^\s*)/g, "");
    },
    Rtrim(s) {
      return s.replace(/(\s*$)/g, "");
    },
    Cut(str) {
      let timeArr = [];
      let wordArr = [];
      timeArr = str.split("[");
      timeArr.splice(0, 1);
      for (let i = 0; i < timeArr.length; i++) {
        let cut = timeArr[i].split("]");
        let time = cut[0].split(".")[0].split(":");
        timeArr[i] = Number.parseInt(time[0]) * 60 + Number.parseInt(time[1]);
        timeArr[i] = isNaN(timeArr[i]) ? 0 : timeArr[i];
        wordArr[i] = this.Rtrim(this.Ltrim(cut[1]));
      }
      return { timeArr: timeArr, wordArr: wordArr };
    },
    controlButtonClick() {
      $(".control_button").click();
    },
    Player() {
      let self = this;
      let player = $("#music")[0];
      let playerTimer = setInterval(timer, 1000);
      $(".control_button").on("click", () => {
        if (this.playState) {
          player.pause();
          this.playState = false;
          this.playIcon = this.pause;
          clearInterval(playerTimer);
        } else {
          player.play();
          this.playState = true;
          this.playIcon = this.play;
          clearInterval(playerTimer);
          playerTimer = setInterval(timer, 1000);
        }
      });
      $(".control_button").click();
      $(".pan").on("click", () => {
        player.muted = false;
        $(".control_button").click();
        $(".pan").unbind("click");
      });
      function timer() {
        self.currentProgress = `${(player.currentTime / player.duration) *
          100}%`;
        if (player.currentTime >= self.wordsTime[self.o + 1]) {
          self.top += Number.parseInt(
            $(".music_word")
              .eq(self.o)
              .height() +
              Number.parseInt(
                $(".music_word")
                  .eq(self.o)
                  .css("marginTop")
              )
          );
          if (self.top >= $(".music_words").height() / 2 - 11) {
            self.wordsTop += -Number.parseInt(
              $(".music_word")
                .eq(self.o)
                .height() +
                Number.parseInt(
                  $(".music_word")
                    .eq(self.o)
                    .css("marginTop")
                )
            );
          }
          self.wordIndex = self.o + 1;
          self.o++;
        }
        if (player.currentTime >= player.duration) {
          if (self.currentMusicList.length !== 1) {
            if (self.playMode === 0) {
              self.currentMusicIndex =
                self.currentMusicIndex >= self.currentMusicList.length - 1
                  ? 0
                  : self.currentMusicIndex + 1;
              if (self.switchMusicTypeFlag) {
                self.currentMusicIndex = 0;
              }
            }
            if (self.playMode === 2) {
              self.currentMusicIndex = Math.floor(
                Math.random() * self.currentMusicList.length
              );
            }
            if (self.playMode === 1) {
              self.currentMusicIndex = 0;
            }
            self.currentListPage = Math.ceil(
              (self.currentMusicIndex + 1) / self.pageSize
            );
            self.switchMusicTypeFlag = false;
            self.getMusicInfo();
          }
          player.play();
          self.top = 0;
          self.o = 0;
          self.wordIndex = 0;
          self.wordsTop = 0;
          self.currentProgress = "0%";
        }
      }
      $(".progress").on("mousedown", ev => {
        let e = ev || event;
        let pro =
          (e.clientX - $(".progress").offset().left) / $(".progress").width();
        clearInterval(playerTimer);
        this.currentProgress = `${pro * 100}%`;
        $(document).on("mousemove", ev => {
          let e = ev || event;
          pro =
            (e.clientX - $(".progress").offset().left) / $(".progress").width();
          this.currentProgress = `${pro * 100}%`;
        });
        $(document).on("mouseup", () => {
          player.currentTime = player.duration * pro;
          let c_arr = [...this.wordsTime];
          c_arr.push(player.currentTime);
          c_arr.sort((l, r) => {
            return l - r;
          });
          let now_o = c_arr.indexOf(player.currentTime) - 1;
          let diff_h = 0;
          if (this.o < now_o) {
            for (let i = this.o; i < now_o; i++) {
              diff_h += -Number.parseInt(
                $(".music_word")
                  .eq(i)
                  .height() +
                  Number.parseInt(
                    $(".music_word")
                      .eq(i)
                      .css("marginTop")
                  )
              );
            }
          } else {
            for (let i = now_o; i < this.o; i++) {
              diff_h += Number.parseInt(
                $(".music_word")
                  .eq(i)
                  .height() +
                  Number.parseInt(
                    $(".music_word")
                      .eq(i)
                      .css("marginTop")
                  )
              );
            }
          }
          this.wordsTop += diff_h;
          self.wordIndex = this.o = now_o;
          clearInterval(playerTimer);
          playerTimer = setInterval(timer, 1000);
          this.playState = true;
          this.playIcon = this.play;
          if (player.currentTime >= player.duration) {
            this.top = 0;
            this.o = 0;
            this.wordIndex = 0;
            this.wordsTop = 0;
            this.currentProgress = "0%";
          }
          player.play();
          $(document).unbind("mousemove");
          $(document).unbind("mouseup");
        });
      });
    }
  },
  computed: {
    currentPageMusicList() {
      return [...this.currentMusicList].splice(
        (this.currentListPage - 1) * this.pageSize,
        this.pageSize
      );
    },
    pageSize() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return 10;
      }
      return 8;
    },
    userId() {
      return this.$store.state.userId;
    }
  },
  watch: {
    musicSearchVal() {
      if (this.musicSearchVal === "") {
        this.musicSearchList = [];
      } else {
        if (this.currentMusicType === 0) {
          this.musicSearchList = this.localMusicList.filter(e => {
            return (
              e.musicName.match(this.musicSearchVal) ||
              e.author.match(this.musicSearchVal)
            );
          });
        }
      }
    }
  }
};
</script>
<style scoped>
@import url("./css/player.css");
@import url("./css/playermobile.css");
</style>
