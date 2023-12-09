function setCookie(obj, day) {
  if (day == null) {
    for (let o in obj) {
      document.cookie = o + "=" + obj[o];
    }
  } else {
    let expires = new Date(
      new Date().getTime() + day * 24 * 60 * 60 * 1000
    ).toUTCString();
    for (let o in obj) {
      document.cookie = o + "=" + obj[o] + ";expires=" + expires;
    }
  }
}

function getCookie(key) {
  let arr = document.cookie.match(new RegExp("(^| )" + key + "=([^;]*)(;|$)"));
  return arr == null ? null : unescape(arr[2]);
}

function delCookie(key) {
  let obj = {};
  obj[key] = "";
  this.setCookie(obj, -1);
}

export default {
  setCookie,
  getCookie,
  delCookie
};
