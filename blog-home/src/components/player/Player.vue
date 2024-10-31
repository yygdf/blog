<template>
  <div>
    <transition name="dis_list">
      <div class="list_box" id="canvas" v-show="musicListState">
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
              <span style="font-size: 14px;">{{ $t("music.list") }}</span>
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
                  :placeholder="$t('music.search')"
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
              <span>{{ $t("music.music") }}</span
              ><span>{{ $t("music.author") }}</span
              ><span>{{ $t("music.album") }}</span>
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
                      :title="$t('music.play')"
                      :style="{ backgroundImage: 'url(' + listPlay + ')' }"
                      @click="
                        listPlayMusic((currentListPage - 1) * pageSize + index)
                      "
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      :title="playState ? $t('music.pause') : $t('music.play2')"
                      :style="{ backgroundImage: 'url(' + listPlay + ')' }"
                      @click="controlButtonClick"
                    ></div>
                    <div
                      v-if="currentMusicType !== 0"
                      class="list_play"
                      :title="
                        musicAddStatus
                          ? $t('music.added')
                          : $t('music.addLocal')
                      "
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="musicAddStatus ? '' : listAddMusic(item)"
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      :title="$t('music.remove')"
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="listRemoveMusic(item.id, false)"
                    ></div>
                    <div
                      v-if="currentMusicType !== 2"
                      class="list_play"
                      :title="
                        musicCollectStatus
                          ? $t('music.collected')
                          : $t('music.collect')
                      "
                      :style="{ backgroundImage: 'url(' + add + ')' }"
                      @click="musicCollectStatus ? '' : listCollectMusic(item)"
                    ></div>
                    <div
                      v-else
                      class="list_play"
                      :title="$t('music.remove')"
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
                {{ $t("page.prev") }}&lt;
              </div>
              <div class="page_middle">
                {{
                  $t("page.no") + " " + currentListPage + " " + $t("page.page")
                }}
              </div>
              <div
                class="page_next"
                v-if="
                  currentListPage <
                    Math.ceil(currentMusicList.length / pageSize)
                "
                @click="pageChange(false)"
              >
                >{{ $t("page.next") }}
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
      musicTypeList: [],
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
    this.Visual();
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
      this.musicAlert(this.$t("music.addSuccess"));
      this.musicAddStatus = true;
    },
    listCollectMusic(item) {
      if (this.userId == null) {
        this.musicAlert(this.$t("music.tip1"));
        return;
      }
      collectMusic(item.id).then(({ data }) => {
        if (data.flag) {
          this.musicAlert(this.$t("music.collectSuccess"));
          this.musicCollectStatus = true;
          this.collectionMusicList.push(item);
        } else {
          this.musicAlert(this.$t("music.collectFailure"));
        }
      });
    },
    listRemoveMusic(id, flag) {
      if (flag) {
        collectMusic(id).then(({ data }) => {
          if (data.flag) {
            this.musicAlert(this.$t("music.removeSuccess"));
            let index = this.collectionMusicList.findIndex(
              item => item.id === id
            );
            this.collectionMusicList.splice(index, 1);
          } else {
            this.musicAlert(this.$t("music.removeFailure"));
          }
        });
      } else {
        let index = this.localMusicList.findIndex(item => item.id === id);
        this.localMusicList.splice(index, 1);
        localStorage.setItem(
          "localMusicList",
          JSON.stringify(this.localMusicList)
        );
        this.musicAlert(this.$t("music.removeSuccess"));
      }
    },
    switchPlayMode() {
      if (this.playMode === 0) {
        this.playMode = 1;
        this.playModeButton = this.state1;
        this.musicAlert(this.$t("music.mode1"));
      } else if (this.playMode === 1) {
        this.playMode = 2;
        this.playModeButton = this.state2;
        this.musicAlert(this.$t("music.mode2"));
      } else {
        this.playMode = 0;
        this.playModeButton = this.state0;
        this.musicAlert(this.$t("music.mode3"));
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
                this.musicAlert(this.$t("music.tip3"));
              }
            });
          }
          this.currentMusicList = this.siteMusicList;
          this.currentMusicType = 1;
        } else if (type === 2) {
          if (this.userId == null) {
            this.musicAlert(this.$t("music.tip1"));
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
          this.musicAlert(this.$t("music.tip2"));
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
        this.musicWords = [this.$t("music.none")];
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
      const self = this;
      const player = $("#music")[0];
      const controlButton = $(".control_button");
      let playerTimer = setInterval(timer, 1000);
      controlButton.on("click", () => {
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
      controlButton.click();
      $(".pan").on("click", () => {
        player.muted = false;
        controlButton.click();
        $(".pan").unbind("click");
      });
      const musicWord = $(".music_word");
      function timer() {
        self.currentProgress = `${(player.currentTime / player.duration) *
          100}%`;
        if (player.currentTime >= self.wordsTime[self.o + 1]) {
          self.top += Number.parseInt(
            musicWord.eq(self.o).height() +
              Number.parseInt(musicWord.eq(self.o).css("marginTop"))
          );
          if (self.top >= musicWord.height() / 2 - 11) {
            self.wordsTop += -Number.parseInt(
              musicWord.eq(self.o).height() +
                Number.parseInt(musicWord.eq(self.o).css("marginTop"))
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
      const progress = $(".progress");
      progress.on("mousedown", ev => {
        let e = ev || event;
        let pro = (e.clientX - progress.offset().left) / progress.width();
        clearInterval(playerTimer);
        this.currentProgress = `${pro * 100}%`;
        $(document).on("mousemove", ev => {
          let e = ev || event;
          pro = (e.clientX - progress.offset().left) / progress.width();
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
                musicWord.eq(i).height() +
                  Number.parseInt(musicWord.eq(i).css("marginTop"))
              );
            }
          } else {
            for (let i = now_o; i < this.o; i++) {
              diff_h += Number.parseInt(
                musicWord.eq(i).height() +
                  Number.parseInt(musicWord.eq(i).css("marginTop"))
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
    },
    Visual() {
      let audio, audioContext, audioSrc;
      let analyser;

      const MATH_PI_2 = Math.PI * 2;

      let w;
      let h;

      let canvas;
      let context;

      let imageData;
      let data;

      let mouseActive = false;
      let mouseDown = false;
      let mousePos = { x: 0, y: 0 };

      let fov = 250;

      let speed = 0.75;
      let smoothingTimeConstant = 0.65;
      let fftSize = 8192;

      let circleHolder = [];

      let time = 0;

      let colorInvertValue = 0;

      let rgb = {};
      rgb.r = Math.random() * MATH_PI_2;
      rgb.g = Math.random() * MATH_PI_2;
      rgb.b = Math.random() * MATH_PI_2;

      let rgb2 = {};
      rgb2.r = Math.random() * MATH_PI_2;
      rgb2.g = Math.random() * MATH_PI_2;
      rgb2.b = Math.random() * MATH_PI_2;

      function getRGBColor(color) {
        let r = Math.sin((color.r += 0.01)) + 1;
        let g = Math.sin((color.g += 0.007)) + 1;
        let b = Math.sin((color.b += 0.013)) + 1;
        return { r: r, g: g, b: b };
      }

      function getRGBColor2(color) {
        let r = Math.sin((color.r += 0.04)) + 1;
        let g = Math.sin((color.g += 0.028)) + 1;
        let b = Math.sin((color.b += 0.052)) + 1;
        return { r: r, g: g, b: b };
      }

      function limitRGBColor(color, percent = 0.45) {
        if (color.r < percent) {
          color.r = percent;
        }

        if (color.g < percent) {
          color.g = percent;
        }

        if (color.b < percent) {
          color.b = percent;
        }
      }

      function init() {
        canvas = document.createElement("canvas");
        canvas.addEventListener("mousedown", mouseDownHandler, false);
        canvas.addEventListener("mouseup", mouseUpHandler, false);
        canvas.addEventListener("mousemove", mouseMoveHandler, false);
        canvas.addEventListener("mouseenter", mouseEnterHandler, false);
        canvas.addEventListener("mouseleave", mouseLeaveHandler, false);
        document.getElementById("canvas").appendChild(canvas);
        context = canvas.getContext("2d");

        window.addEventListener("resize", onResize, false);

        onResize();

        addCircles();

        render();

        clearImageData();

        render();

        context.putImageData(imageData, 0, 0);
        audioSetup();
        animate();
      }

      function audioSetup() {
        audio = $("#music")[0];
        audio.crossOrigin = "anonymous";

        audioContext = new (window.AudioContext || window.webkitAudioContext)();

        analyser = audioContext.createAnalyser();
        analyser.connect(audioContext.destination);
        analyser.smoothingTimeConstant = smoothingTimeConstant;
        analyser.fftSize = fftSize;

        audioSrc = audioContext.createMediaElementSource(audio);
        audioSrc.connect(analyser);
      }

      function clearImageData() {
        for (let i = 0, l = data.length; i < l; i += 4) {
          data[i] = 0;
          data[i + 1] = 0;
          data[i + 2] = 0;
          data[i + 3] = 255;
        }
      }

      function setPixel(x, y, r, g, b, a) {
        let i = (x + y * imageData.width) * 4;

        data[i] = r;
        data[i + 1] = g;
        data[i + 2] = b;
        data[i + 3] = a;
      }

      function drawLine(x1, y1, x2, y2, r, g, b, a) {
        let dx = Math.abs(x2 - x1);
        let dy = Math.abs(y2 - y1);

        let sx = x1 < x2 ? 1 : -1;
        let sy = y1 < y2 ? 1 : -1;

        let err = dx - dy;

        let lx = x1;
        let ly = y1;

        // eslint-disable-next-line no-constant-condition
        while (true) {
          if (lx > 0 && lx < w && ly > 0 && ly < h) {
            setPixel(lx, ly, r, g, b, a);
          }

          if (lx === x2 && ly === y2) break;

          let e2 = 2 * err;

          if (e2 > -dx) {
            err -= dy;
            lx += sx;
          }

          if (e2 < dy) {
            err += dx;
            ly += sy;
          }
        }
      }

      function getCirclePosition(centerX, centerY, radius, index, segments) {
        let angle = index * (MATH_PI_2 / segments) + time;

        let x = centerX + Math.cos(angle) * radius;
        let y = centerY + Math.sin(angle) * radius;

        return { x: x, y: y };
      }

      function drawCircle(centerPosition, radius, segments) {
        let coordinates = [];

        let radiusSave;

        let diff = 0;

        for (let i = 0; i <= segments; i++) {
          let radiusRandom = radius;

          if (i === 0) {
            radiusSave = radiusRandom;
          }

          if (i === segments) {
            radiusRandom = radiusSave;
          }

          let centerX = centerPosition.x;
          let centerY = centerPosition.y;

          let position = getCirclePosition(
            centerX,
            centerY,
            radiusRandom,
            i,
            segments
          );

          coordinates.push({
            x: position.x,
            y: position.y,
            index: i + diff,
            radius: radiusRandom,
            segments: segments
          });
        }

        return coordinates;
      }

      function addCircleSegment(x, y, z, audioBufferIndex) {
        let circleSegment = {};
        circleSegment.x = x;
        circleSegment.y = y;
        circleSegment.x2d = 0;
        circleSegment.y2d = 0;
        circleSegment.audioBufferIndex = audioBufferIndex;

        return circleSegment;
      }

      function addCircles() {
        let audioBufferIndexMin = 8;
        let audioBufferIndexMax = 1024;
        let audioBufferIndex = audioBufferIndexMin;

        let centerPosition = { x: 0, y: 0 };
        let center = { x: 0, y: 0 };

        let toggle = 1;
        let index = 0;

        let mp = { x: Math.random() * w, y: Math.random() * h };

        for (let z = -fov; z < fov; z += 5) {
          let coordinates = drawCircle(centerPosition, 75, 64);

          let circleObj = {};
          circleObj.segmentsOutside = [];
          circleObj.segmentsInside = [];
          circleObj.segmentsInside2 = [];
          circleObj.segmentsCount = 0;
          circleObj.index = index;
          circleObj.z = z;
          circleObj.center = center;
          circleObj.circleCenter = { x: 0, y: 0 };
          circleObj.mp = { x: mp.x, y: mp.y };
          circleObj.radius = coordinates[0].radius;
          circleObj.color = { r: 0, g: 0, b: 0 };

          toggle = index % 2;
          index++;

          audioBufferIndex =
            Math.floor(Math.random() * audioBufferIndexMax) +
            audioBufferIndexMin;

          let circleSegmentOutside;

          for (let i = 0, l = coordinates.length; i < l; i++) {
            let coordinate = coordinates[i];

            if (i % 2 === toggle) {
              circleSegmentOutside = addCircleSegment(
                coordinate.x,
                coordinate.y,
                z,
                audioBufferIndex
              );
              circleSegmentOutside.active = true;
              circleSegmentOutside.index = coordinate.index;
              circleSegmentOutside.radius = coordinate.radius;
              circleSegmentOutside.radiusAudio = circleSegmentOutside.radius;
              circleSegmentOutside.segments = coordinate.segments;
              circleSegmentOutside.coordinates = [];

              let co;

              if (i > 0) {
                co = coordinates[i - 1];
              } else {
                co = coordinates[l - 1];
              }

              let sub1 = addCircleSegment(co.x, co.y, z, audioBufferIndex);
              let sub2 = addCircleSegment(
                coordinate.x,
                coordinate.y,
                z - 5,
                audioBufferIndex
              );
              let sub3 = addCircleSegment(co.x, co.y, z - 5, audioBufferIndex);

              let sub4 = addCircleSegment(
                coordinate.x,
                coordinate.y,
                z,
                audioBufferIndex
              );
              let sub5 = addCircleSegment(co.x, co.y, z, audioBufferIndex);
              let sub6 = addCircleSegment(
                coordinate.x,
                coordinate.y,
                z - 5,
                audioBufferIndex
              );
              let sub7 = addCircleSegment(co.x, co.y, z - 5, audioBufferIndex);

              sub1.index = co.index;
              sub2.index = coordinate.index;
              sub3.index = co.index;

              sub4.index = coordinate.index;
              sub5.index = co.index;
              sub6.index = coordinate.index;
              sub7.index = co.index;

              circleSegmentOutside.subs = [];
              circleSegmentOutside.subs.push(
                sub1,
                sub2,
                sub3,
                sub4,
                sub5,
                sub6,
                sub7
              );

              if (i < l - 1) {
                audioBufferIndex =
                  Math.floor(Math.random() * audioBufferIndexMax) +
                  audioBufferIndexMin;
              } else {
                audioBufferIndex =
                  circleObj.segmentsOutside[0].audioBufferIndex;
              }

              circleObj.segmentsOutside.push(circleSegmentOutside);
            } else {
              circleObj.segmentsOutside.push({ active: false });
            }
          }

          circleHolder.push(circleObj);
        }
      }

      function onResize() {
        w =
          window.innerWidth ||
          document.documentElement.clientWidth ||
          document.body.clientWidth;
        h =
          window.innerHeight ||
          document.documentElement.clientHeight ||
          document.body.clientHeight;

        canvas.width = w;
        canvas.height = h;

        context.fillStyle = "#000000";
        context.fillRect(0, 0, w, h);

        imageData = context.getImageData(0, 0, w, h);
        data = imageData.data;
      }

      function mouseDownHandler() {
        mouseDown = true;
      }

      function mouseUpHandler() {
        mouseDown = false;
      }

      function mouseEnterHandler() {
        mouseActive = true;
      }

      function mouseLeaveHandler() {
        mouseActive = false;
        mouseDown = false;
      }

      function mouseMoveHandler(event) {
        mousePos = getMousePos(canvas, event);
      }

      function getMousePos(canvas, event) {
        let rect = canvas.getBoundingClientRect();

        return { x: event.clientX - rect.left, y: event.clientY - rect.top };
      }

      function render() {
        let aa = false;

        if (analyser) {
          aa = true;
        }

        let frequencySource;

        if (aa === true) {
          frequencySource = new Uint8Array(analyser.frequencyBinCount);

          analyser.getByteFrequencyData(frequencySource);
        }

        let sortArray = false;

        let col = getRGBColor2(rgb);
        let col2 = getRGBColor(rgb2);

        limitRGBColor(col, 0.45);
        limitRGBColor(col2, 0.25);

        for (let i = 0, l = circleHolder.length; i < l; i++) {
          let circleObj = circleHolder[i];

          circleObj.color.r = col.r - (circleObj.z + fov) / fov;
          circleObj.color.g = col.g - (circleObj.z + fov) / fov;
          circleObj.color.b = col.b - (circleObj.z + fov) / fov;

          if (circleObj.color.r < col2.r) {
            circleObj.color.r = col2.r;
          }

          if (circleObj.color.g < col2.g) {
            circleObj.color.g = col2.g;
          }

          if (circleObj.color.b < col2.b) {
            circleObj.color.b = col2.b;
          }

          let circleObjBack;

          if (i > 0) {
            circleObjBack = circleHolder[i - 1];
          }

          if (mouseActive) {
            circleObj.mp = mousePos;
          } else {
            circleObj.mp.x += (w / 2 - circleObj.mp.x) * 0.00025;
            circleObj.mp.y += (h / 2 - circleObj.mp.y) * 0.00025;
          }

          circleObj.center.x =
            (w / 2 - circleObj.mp.x) * ((circleObj.z - fov) / 500) + w / 2;
          circleObj.center.y =
            (h / 2 - circleObj.mp.y) * ((circleObj.z - fov) / 500) + h / 2;

          for (let j = 0, k = circleObj.segmentsOutside.length; j < k; j++) {
            let circleSegmentOutside = circleObj.segmentsOutside[j];

            if (circleSegmentOutside.active === true) {
              let scale = fov / (fov + circleObj.z);
              let scaleBack;

              if (i > 0) {
                scaleBack = fov / (fov + circleObjBack.z);
              }

              let frequency, frequencyAdd;

              circleSegmentOutside.x2d =
                circleSegmentOutside.x * scale + circleObj.center.x;
              circleSegmentOutside.y2d =
                circleSegmentOutside.y * scale + circleObj.center.y;

              if (aa === true) {
                frequency =
                  frequencySource[circleSegmentOutside.audioBufferIndex];
                frequencyAdd = frequency / 20;

                circleSegmentOutside.radiusAudio =
                  circleSegmentOutside.radius - frequencyAdd;
              }

              let lineColorValue = 0;

              if (j > 0) {
                if (aa === true && audio.paused === false) {
                  lineColorValue = Math.round((i / l) * (55 + frequency));

                  if (lineColorValue > 255) {
                    lineColorValue = 255;
                  }
                } else {
                  lineColorValue = Math.round((i / l) * 200);
                }
              }

              if (i > 0 && i < l - 1) {
                let sub1 = circleSegmentOutside.subs[0];
                let sub1angle =
                  sub1.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub1.x2d = sub1.x * scale + circleObj.center.x;
                sub1.y2d = sub1.y * scale + circleObj.center.y;
                sub1.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub1angle) * circleSegmentOutside.radiusAudio;
                sub1.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub1angle) * circleSegmentOutside.radiusAudio;

                let sub2 = circleSegmentOutside.subs[1];
                let sub2angle =
                  sub2.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub2.x2d = sub2.x * scaleBack + circleObjBack.center.x;
                sub2.y2d = sub2.y * scaleBack + circleObjBack.center.y;
                sub2.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub2angle) * circleSegmentOutside.radiusAudio;
                sub2.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub2angle) * circleSegmentOutside.radiusAudio;

                let sub3 = circleSegmentOutside.subs[2];
                let sub3angle =
                  sub3.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub3.x2d = sub3.x * scaleBack + circleObjBack.center.x;
                sub3.y2d = sub3.y * scaleBack + circleObjBack.center.y;
                sub3.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub3angle) * circleSegmentOutside.radiusAudio;
                sub3.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub3angle) * circleSegmentOutside.radiusAudio;

                let sub4 = circleSegmentOutside.subs[3];
                let sub4angle =
                  sub4.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub4.x2d = sub4.x * scale + circleObj.center.x;
                sub4.y2d = sub4.y * scale + circleObj.center.y;
                sub4.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub4angle) * circleSegmentOutside.radius;
                sub4.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub4angle) * circleSegmentOutside.radius;

                let sub5 = circleSegmentOutside.subs[4];
                let sub5angle =
                  sub5.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub5.x2d = sub5.x * scale + circleObj.center.x;
                sub5.y2d = sub5.y * scale + circleObj.center.y;
                sub5.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub5angle) * circleSegmentOutside.radius;
                sub5.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub5angle) * circleSegmentOutside.radius;

                let sub6 = circleSegmentOutside.subs[5];
                let sub6angle =
                  sub6.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub6.x2d = sub6.x * scaleBack + circleObjBack.center.x;
                sub6.y2d = sub6.y * scaleBack + circleObjBack.center.y;
                sub6.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub6angle) * circleSegmentOutside.radius;
                sub6.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub6angle) * circleSegmentOutside.radius;

                let sub7 = circleSegmentOutside.subs[6];
                let sub7angle =
                  sub7.index * (MATH_PI_2 / circleSegmentOutside.segments) +
                  time;
                sub7.x2d = sub7.x * scaleBack + circleObjBack.center.x;
                sub7.y2d = sub7.y * scaleBack + circleObjBack.center.y;
                sub7.x =
                  circleObj.circleCenter.x +
                  Math.cos(sub7angle) * circleSegmentOutside.radius;
                sub7.y =
                  circleObj.circleCenter.y +
                  Math.sin(sub7angle) * circleSegmentOutside.radius;

                let p1;
                let p2;
                let p3;
                let p4;

                let p5 = circleSegmentOutside.subs[3];
                let p6 = circleSegmentOutside.subs[4];
                let p7 = circleSegmentOutside.subs[6];
                let p8 = circleSegmentOutside.subs[5];

                if (frequencyAdd > 0) {
                  p1 = circleSegmentOutside;
                  p2 = circleSegmentOutside.subs[1];
                  p3 = circleSegmentOutside.subs[2];
                  p4 = circleSegmentOutside.subs[0];
                }

                let cr = Math.round(circleObj.color.r * lineColorValue);
                let cg = Math.round(circleObj.color.g * lineColorValue);
                let cb = Math.round(circleObj.color.b * lineColorValue);

                if (frequencyAdd > 0) {
                  drawLine(
                    p1.x2d | 0,
                    p1.y2d | 0,
                    p2.x2d | 0,
                    p2.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p2.x2d | 0,
                    p2.y2d | 0,
                    p3.x2d | 0,
                    p3.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p3.x2d | 0,
                    p3.y2d | 0,
                    p4.x2d | 0,
                    p4.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p4.x2d | 0,
                    p4.y2d | 0,
                    p1.x2d | 0,
                    p1.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );

                  drawLine(
                    p5.x2d | 0,
                    p5.y2d | 0,
                    p1.x2d | 0,
                    p1.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p6.x2d | 0,
                    p6.y2d | 0,
                    p4.x2d | 0,
                    p4.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p7.x2d | 0,
                    p7.y2d | 0,
                    p3.x2d | 0,
                    p3.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p8.x2d | 0,
                    p8.y2d | 0,
                    p2.x2d | 0,
                    p2.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                }

                if (circleObj.z < fov / 2) {
                  drawLine(
                    p5.x2d | 0,
                    p5.y2d | 0,
                    p6.x2d | 0,
                    p6.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p6.x2d | 0,
                    p6.y2d | 0,
                    p7.x2d | 0,
                    p7.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p7.x2d | 0,
                    p7.y2d | 0,
                    p8.x2d | 0,
                    p8.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                  drawLine(
                    p8.x2d | 0,
                    p8.y2d | 0,
                    p5.x2d | 0,
                    p5.y2d | 0,
                    cr,
                    cg,
                    cb,
                    255
                  );
                }
              }

              let circleSegmentOutsideAngle;

              circleSegmentOutsideAngle =
                circleSegmentOutside.index *
                  (MATH_PI_2 / circleSegmentOutside.segments) +
                time;

              circleSegmentOutside.x =
                circleObj.circleCenter.x +
                Math.cos(circleSegmentOutsideAngle) *
                  circleSegmentOutside.radiusAudio;
              circleSegmentOutside.y =
                circleObj.circleCenter.y +
                Math.sin(circleSegmentOutsideAngle) *
                  circleSegmentOutside.radiusAudio;
            }
          }

          if (mouseDown) {
            circleObj.z += speed;

            if (circleObj.z > fov) {
              circleObj.z -= fov * 2;

              sortArray = true;
            }
          } else {
            circleObj.z -= speed;

            if (circleObj.z < -fov) {
              circleObj.z += fov * 2;

              sortArray = true;
            }
          }
        }

        if (sortArray) {
          circleHolder = circleHolder.sort(function(a, b) {
            return b.z - a.z;
          });
        }

        if (mouseDown) {
          time -= 0.005;
        } else {
          time += 0.005;
        }

        if (mouseDown) {
          if (colorInvertValue < 255) colorInvertValue += 5;
          else colorInvertValue = 255;

          softInvert(colorInvertValue);
        } else {
          if (colorInvertValue > 0) colorInvertValue -= 5;
          else colorInvertValue = 0;

          if (colorInvertValue > 0) softInvert(colorInvertValue);
        }
      }

      function softInvert(value) {
        for (let j = 0, n = data.length; j < n; j += 4) {
          data[j] = Math.abs(value - data[j]);
          data[j + 1] = Math.abs(value - data[j + 1]);
          data[j + 2] = Math.abs(value - data[j + 2]);
          data[j + 3] = 255;
        }
      }

      function animate() {
        clearImageData();

        render();

        context.putImageData(imageData, 0, 0);

        requestAnimationFrame(animate);
      }

      window.requestAnimFrame = (function() {
        return (
          window.requestAnimationFrame ||
          window.webkitRequestAnimationFrame ||
          window.mozRequestAnimationFrame ||
          function(callback) {
            window.setTimeout(callback, 1000 / 60);
          }
        );
      })();

      init();
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
    },
    "$i18n.locale": {
      handler() {
        this.musicTypeList = [
          { name: this.$t("music.list1"), type: 3778678 },
          { name: this.$t("music.list2"), type: 3779629 },
          { name: this.$t("music.list3"), type: 19723756 },
          { name: this.$t("music.list4"), type: 991319590 },
          { name: this.$t("music.list5"), type: 1 },
          { name: this.$t("music.list6"), type: 2 },
          { name: this.$t("music.list7"), type: 0 }
        ];
      },
      immediate: true
    }
  }
};
</script>
<style scoped>
@import url("./css/player.css");
@import url("./css/playermobile.css");
</style>
