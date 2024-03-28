<template>
  <div>
    <transition name="dis_list">
      <div class="list_box" v-if="listIsDis">
        <transition name="music_alert">
          <span class="music_alert" v-if="musicAlertState">
            {{ musicAlertVal }}
          </span>
        </transition>
        <div class="list_close" @click="DisList">x</div>
        <div class="music_list">
          <div class="list_l">
            <ul class="music_type">
              <li
                v-for="(item, index) in musicTypeList"
                :key="index"
                @click="_getMusicType(item.id)"
                :class="{ type_active: item.id === thisMusicType }"
              >
                {{ item.name }}
              </li>
            </ul>
            <div class="list_title">
              <span style="font-size: 14px;">歌曲列表</span>
              <img
                :src="musicStateButton"
                alt=""
                class="music_state"
                @click="MusicStateChange"
              />
              <div class="music_search_box">
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
                      @click="ListAdd(item)"
                    >
                      <span class="music_search_name">{{ item.name }}</span>
                      <span class="music_search_ar">
                        {{ item.artists[0].name }}
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
                v-for="(item, index) in thisMusicList"
                :key="index"
                @mouseover="buttonActive(index)"
                @dblclick="ListPlay((thisListPage - 1) * pageSize + index)"
              >
                <div
                  class="this_music_shelter"
                  v-if="
                    (thisListPage - 1) * pageSize + index === thisMusicIndex
                  "
                ></div>
                <span>{{ item.name }}</span
                ><span>{{ item.ar[0].name }}</span
                ><span>{{ item.al.name }}</span>
                <transition name="list_button">
                  <div
                    class="music_button"
                    v-if="listButtonActiveIndex === index"
                  >
                    <div
                      class="list_play"
                      title="播放这首歌"
                      :style="{ backgroundImage: 'url(' + listPlay + ')' }"
                      @click="ListPlay((thisListPage - 1) * pageSize + index)"
                    ></div>
                    <div
                      class="list_play"
                      title="添加到 My Songs"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="ListAdd(item)"
                      v-if="thisMusicType !== 0"
                    ></div>
                  </div>
                </transition>
              </li>
            </ul>
            <div class="list_page">
              <div
                class="page_last"
                v-if="thisListPage !== 1"
                @click="listChange(true)"
              >
                上一页&lt;
              </div>
              <div class="page_middle">第 {{ thisListPage }} 页</div>
              <div
                class="page_next"
                v-if="thisListPage < Math.ceil(musicList.length / pageSize)"
                @click="listChange(false)"
              >
                >下一页
              </div>
            </div>
          </div>
          <div class="list_r">
            <img class="music_list_bg" :src="musicImg" alt="" />
            <div
              class="music_list_shelter"
              :style="{ backgroundImage: 'url(' + shelter + ')' }"
            ></div>
            <ul class="music_talk_list">
              <li v-for="(item, index) in hotTalkList" :key="index">
                <div class="talk_head">
                  <img
                    :src="item.user.avatarUrl"
                    alt=""
                    class="talk_head_img"
                  />
                  <span class="talk_head_name">{{ item.user.nickname }}</span>
                </div>
                <p class="talk_content">
                  <img class="talk_icon_l" :src="talkicon1" alt="" />
                  {{ item.content.trim() }}
                  <img class="talk_icon_r" :src="talkicon2" alt="" />
                </p>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </transition>
    <div class="bbox" :class="{ bbox_active: disActive }">
      <div
        class="pan"
        :style="{ backgroundImage: 'url(' + pan + ')' }"
        :class="{ pan_active: disActive }"
        @click="DisActive"
      >
        <img :src="musicImg" alt="" class="pan_c" />
      </div>
      <div
        class="box"
        :style="{ backgroundImage: 'url(' + musicImg + ')' }"
        :class="{ box_active: disActive }"
        @dblclick="DisList"
      >
        <div
          class="music_shelter_2"
          :style="{ backgroundImage: 'url(' + musicImg + ')' }"
          :class="{ shelter_active: disActive }"
        ></div>
        <div
          class="music_shelter"
          :style="{ backgroundImage: 'url(' + musicImg + ')' }"
          :class="{ shelter_active: disActive }"
        ></div>
        <div class="music_shelter_3"></div>
        <div class="music_dis">
          <div class="dis_list" @click="DisList">···</div>
          <div class="dis_board" @click="DisActive">X</div>
          <p class="music_title">{{ musicTitle }}</p>
          <p class="music_intro">歌手: {{ musicName }}</p>
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
  getWords,
  getMusicInfo,
  getMusicUrl,
  getHotMusic,
  getSearchSuggest,
  getHotTalk,
  getLocalMusic
} from "./api/music";
import pan from "./img/pan.png";
import play from "./img/play.png";
import pause from "./img/pause.png";
import prev from "./img/prev.png";
import next from "./img/next.png";
import add from "./img/add.png";
import shelter from "./img/list_pan.png";
import listPlay from "./img/list_play_hover.png";
import state0 from "./img/state_0.png";
import state1 from "./img/state_1.png";
import state2 from "./img/state_2.png";
import talkicon1 from "./img/talkicon1.png";
import talkicon2 from "./img/talkicon2.png";
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
      shelter,
      listPlay,
      state0,
      state1,
      state2,
      talkicon1,
      talkicon2,
      playState: true,
      playIcon: play,
      musicImg: "",
      musicUrl: "",
      musicWords: [],
      musicTitle: "",
      musicName: "",
      wordsTime: [],
      wordsTop: 0,
      wordIndex: 0,
      currentProgress: "0%",
      musicList: [],
      myMusicList: [],
      thisMusicIndex: -1,
      disActive: false,
      listIsDis: false,
      listButtonActiveIndex: -1,
      thisListPage: -1,
      musicTypeList: [
        { name: "热歌榜", id: 3778678 },
        { name: "新歌榜", id: 3779629 },
        { name: "飙升榜", id: 19723756 },
        { name: "嘻哈榜", id: 991319590 },
        { name: "My Songs", id: 0 }
      ],
      thisMusicType: -1,
      notPlay: [],
      musicState: 0,
      musicStateButton: state0,
      musicSearchVal: "",
      musicSearchList: [],
      musicAlertVal: "",
      musicAlertState: false,
      musicAlertTimer: "",
      hotTalkList: [],
      switchList: false
    };
  },
  mounted() {
    this.Player();
  },
  created() {
    this._getMusicType(0);
    this.thisMusicIndex = 0;
    this._getLocalInfo();
    this.DisAuthorInfo();
  },
  computed: {
    thisMusicList() {
      return [...this.musicList].splice(
        (this.thisListPage - 1) * this.pageSize,
        this.pageSize
      );
    },
    pageSize() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return 10;
      }
      return 8;
    }
  },
  watch: {
    musicSearchVal() {
      if (this.musicSearchVal === "") {
        this.musicSearchList = [];
      } else {
        getSearchSuggest(this.musicSearchVal).then(res => {
          if (res.data.result.songs === undefined) {
            this.musicSearchList = [];
          } else {
            this.musicSearchList = res.data.result.songs;
          }
        });
      }
    }
  },
  methods: {
    DisAuthorInfo() {
      console.log(
        "%c音乐播放器PLUS---KS，博客地址: https://iksling.com",
        "background-color:rgb(225,225,225);border-radius:4px;font-size:12px;padding:4px;color:rgb(30,30,30);"
      );
    },
    MusicAlert(val) {
      this.musicAlertState = true;
      this.musicAlertVal = val;
      clearTimeout(this.musicAlertTimer);
      this.musicAlertTimer = setTimeout(() => {
        this.musicAlertState = false;
        this.musicAlertVal = "";
      }, 2000);
    },
    ListAdd(obj) {
      getMusicInfo(obj.id).then(res => {
        this.musicSearchVal = "";
        if (this.myMusicList.length === 0) {
          this.myMusicList = [res.data.songs[0]];
          this.thisMusicType = -1;
          this._getMusicType(0);
          this.thisMusicIndex = 0;
          this._getInfo();
        } else {
          this.myMusicList.push(res.data.songs[0]);
        }
        this.MusicAlert("添加成功");
      });
    },
    MusicStateChange() {
      if (this.musicState === 0) {
        this.musicState = 1;
        this.musicStateButton = this.state1;
        this.MusicAlert("已切换为单曲循环模式");
      } else if (this.musicState === 1) {
        this.musicState = 2;
        this.musicStateButton = this.state2;
        this.MusicAlert("已切换为随机播放模式");
      } else {
        this.musicState = 0;
        this.musicStateButton = this.state0;
        this.MusicAlert("已切换为列表循环模式");
      }
    },
    DisList() {
      if (!this.listIsDis) {
        this.disActive = false;
      }
      this.listIsDis = !this.listIsDis;
    },
    listChange(isLast) {
      this.listButtonActiveIndex = -1;
      if (isLast) {
        this.thisListPage--;
      } else {
        this.thisListPage++;
      }
    },
    musicChange(isPrev) {
      if (isPrev) {
        this.ListPlay(--this.thisMusicIndex);
      } else {
        this.ListPlay(++this.thisMusicIndex);
      }
    },
    ListPlay(id) {
      this.thisMusicIndex = id > this.musicList.length - 1 || id < 0 ? 0 : id;
      this._getInfo();
      this.top = 0;
      this.o = 0;
      this.wordIndex = 0;
      this.wordsTop = 0;
      this.currentProgress = "0%";
      let player = $("#music")[0];
      this.switchList = false;
      player.currentTime = 0;
      if (!this.playState) {
        $(".control_button").click();
      }
    },
    buttonActive(id) {
      this.listButtonActiveIndex = id;
    },
    DisActive() {
      this.disActive = !this.disActive;
    },
    _getMusicType(id) {
      if (this.thisMusicType !== id) {
        this.notPlay = [];
        this.switchList = true;
        if (id === 0) {
          if (this.myMusicList.length === 0) {
            this.myMusicList = getLocalMusic(id).music.splice(0, 200);
          }
          this.musicList = this.myMusicList;
          this.thisMusicType = 0;
        } else {
          getHotMusic(id).then(res => {
            this.musicList = res.data.playlist.tracks.splice(0, 200);
            this.thisMusicType = id;
          });
        }
        this.thisListPage = 1;
        this.thisMusicIndex = -1;
        this.listButtonActiveIndex = -1;
      }
    },
    _getInfo() {
      if (!this.musicList[this.thisMusicIndex].resourceState) {
        this._getLocalInfo();
      } else {
        getMusicUrl(this.musicList[this.thisMusicIndex].id).then(res => {
          if (
            res.data.data[0].url === null ||
            res.data.data[0].url === "" ||
            res.data.data[0].url === undefined
          ) {
            if (this.notPlay.length !== this.musicList.length) {
              let nextIndex = this.thisMusicIndex + 1;
              if (this.notPlay.indexOf(this.thisMusicIndex) === -1) {
                this.notPlay.push(this.thisMusicIndex);
              }
              this.MusicAlert(
                `${
                  this.musicList[this.thisMusicIndex].name
                }因为一些原因不能播放`
              );
              this.ListPlay(nextIndex);
            } else {
              this.MusicAlert("此列表所有歌都不能播放");
            }
          } else {
            this.musicUrl = res.data.data[0].url.replace("http://", "https://");
            this.musicImg =
              this.musicList[this.thisMusicIndex].al.picUrl.replace(
                "http://",
                "https://"
              ) + "?param=300y300";
            this.musicTitle = this.musicList[this.thisMusicIndex].name;
            let name_arr = [];
            this.musicList[this.thisMusicIndex].ar.forEach(i => {
              name_arr.push(i.name);
            });
            this.musicName = name_arr.join("/");
            getWords(this.musicList[this.thisMusicIndex].id).then(res => {
              if (!res.data.nolyric) {
                let info = this.Cut(res.data.lrc.lyric);
                this.musicWords = info.wordArr;
                this.wordsTime = info.timeArr;
              }
            });
            getHotTalk(this.musicList[this.thisMusicIndex].id).then(res => {
              let count = 0;
              this.hotTalkList = res.data.hotComments.splice(0, 3);
              this.hotTalkList.forEach(e => {
                count += e.content.length;
                e.user.avatarUrl = e.user.avatarUrl + "?param=50y50";
              });
              if (count >= 200) {
                this.hotTalkList = this.hotTalkList.slice(0, 2);
              }
            });
          }
        });
      }
    },
    _getLocalInfo() {
      this.hotTalkList = [];
      this.musicUrl = this.musicList[this.thisMusicIndex].musicUrl;
      this.musicImg = this.musicList[this.thisMusicIndex].musicImg;
      this.musicTitle = this.musicList[this.thisMusicIndex].musicTitle;
      this.musicName = this.musicList[this.thisMusicIndex].ar[0].name;
      let info = this.Cut(this.musicList[this.thisMusicIndex].musicWords);
      this.musicWords = info.wordArr;
      this.wordsTime = info.timeArr;
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
          if (self.musicList.length !== 1) {
            if (self.musicState === 0) {
              self.thisMusicIndex =
                self.thisMusicIndex >= self.musicList.length - 1
                  ? 0
                  : self.thisMusicIndex + 1;
              if (self.switchList) {
                self.thisMusicIndex = 0;
              }
            }
            if (self.musicState === 2) {
              self.thisMusicIndex = Math.floor(
                Math.random() * self.musicList.length
              );
            }
            if (self.musicState === 1) {
              self.thisMusicIndex = 0;
            }
            self.thisListPage = Math.ceil(
              (self.thisMusicIndex + 1) / self.pageSize
            );
            self.switchList = false;
            self._getInfo();
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
  }
};
</script>
<style scoped>
@import url("./css/player.css");
@import url("./css/playermobile.css");
</style>
