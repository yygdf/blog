function skipEmptyValue(o) {
  let param = {};
  Object.keys(o).forEach(key => {
    if (o[key] != null && o[key].length !== 0) {
      param[key] = o[key];
    }
  });
  return param;
}

function skipIdenticalValue(o1, o2) {
  let param = {};
  Object.keys(o1).forEach(key => {
    if (typeof o1[key] === "object") {
      if (JSON.stringify(o1[key]) !== JSON.stringify(o2[key])) {
        param[key] = o1[key];
      }
    } else if (typeof o1[key] === "string") {
      if (o1[key].trim() !== o2[key].trim()) {
        param[key] = o1[key].trim();
      }
    } else if (o1[key] !== o2[key]) {
      param[key] = o1[key];
    }
  });
  return param;
}

export default {
  skipEmptyValue,
  skipIdenticalValue
};
