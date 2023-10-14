function skipEmptyValue(obj) {
  if (obj == null || typeof obj !== "object") {
    return obj;
  }
  let param = {};
  Object.keys(obj).forEach(key => {
    if (obj[key] != null && obj[key] !== "") {
      param[key] = obj[key];
    }
  });
  return param;
}

export default {
  skipEmptyValue
};
