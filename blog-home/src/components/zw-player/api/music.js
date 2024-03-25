import axios from "axios";
export const getWords = id => {
  return axios.post(
    `https://n.xlz122.cn/api/lyric?id=${id}&timestamp` + Date.now
  );
};
export const getMusicInfo = id => {
  return axios.post(
    `https://n.xlz122.cn/api/song/detail?ids=${id}&timestamp` + Date.now
  );
};
export const getMusicUrl = id => {
  return axios.post(
    `https://n.xlz122.cn/api/song/url?id=${id}&timestamp` + Date.now
  );
};
export const getHotMusic = id => {
  return axios.post(
    `https://n.xlz122.cn/api/playlist/detail?id=${id}&timestamp=` + Date.now
  );
};
export const getSearchSuggest = key => {
  return axios.post(
    `https://n.xlz122.cn/api/search/suggest?keywords=${key}&timestamp` +
      Date.now
  );
};
export const getHotTalk = id => {
  return axios.post(
    `https://n.xlz122.cn/api/comment/music?id=${id}&limit=3&timestamp` +
      Date.now
  );
};
export const getLocalMusic = () => {
  return {
    music: [
      {
        id: "1",
        musicImg: "https://ksling.cn/static/mp3/rxts.jpg",
        musicUrl: "https://ksling.cn/static/mp3/rxts.mp3",
        ar: [{ name: "马旭东" }],
        al: { name: "苍白" },
        name: "入戏太深",
        resourceState: false,
        musicWords:
          "[00:00.00][00:02.70]入戏太深 - 马旭东[00:04.15]作词：马旭东[00:06.69]作曲：马旭东[00:24.50]One Two Three Go![00:27.26]你的笑总是装作很天真[00:31.59]说我们永远都不会离分[00:35.82]空气中弥漫浪漫的气氛[00:39.40]随着心跳在升温[00:43.90][00:44.55]粉红色长发迷人的嘴唇[00:48.73]浅蓝色眼影迷离的眼神[00:53.11]现实的你和他路边拥吻[00:56.74]看街道上的落英缤纷[01:00.47]是我入戏太深[01:02.41]结局却一个人[01:04.50]原地傻傻的等[01:06.69]换不回那温存[01:08.93]怪我入戏太深[01:11.12]已变心的灵魂[01:13.26]这首歌越唱越觉得残忍[01:17.79]是我入戏太深[01:19.63]结局却一个人[01:21.92]原地傻傻的等[01:24.06]换不回那温存[01:26.30]怪我入戏太深[01:28.29]已变心的灵魂[01:30.63]谁能懂那些誓言多伤人[01:35.11][01:51.81]One Two Three Go![01:53.97]粉红色长发迷人的嘴唇[01:58.30]浅蓝色眼影迷离的眼神[02:02.58]现实的你和他路边拥吻[02:06.26]看街道上的落英缤纷[02:09.94]是我入戏太深[02:11.69]结局却一个人[02:13.93]原地傻傻的等[02:16.12]换不回那温存[02:18.31]怪我入戏太深[02:20.40]已变心的灵魂[02:22.73]这首歌越唱越觉得残忍[02:27.26]是我入戏太深[02:29.15]结局却一个人[02:31.34]原地傻傻的等[02:33.58]换不回那温存[02:35.87]怪我入戏太深[02:37.91]已变心的灵魂[02:40.10]谁能懂那些誓言多伤人[02:44.63][03:02.00]是我入戏太深[03:04.04]结局却一个人[03:06.18]原地傻傻的等[03:08.22]换不回那温存[03:10.46]怪我入戏太深[03:12.50]已变心的灵魂[03:14.84]这首歌越唱越觉得残忍[03:19.32]是我入戏太深[03:21.36]结局却一个人[03:23.50]原地傻傻的等[03:25.54]换不回那温存[03:27.83]怪我入戏太深[03:29.97]已变心的灵魂[03:32.31]谁能懂那些誓言多伤人[03:37.98][03:43.38]有一个地方,只有你知道"
      }
    ]
  };
};
