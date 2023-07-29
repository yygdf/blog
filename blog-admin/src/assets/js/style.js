export function flexWidth(data, prop, title, finalMaxWidth = 0) {
  let canvas = document.createElement("canvas");
  let context = canvas.getContext("2d");
  let maxWidth;
  context.font = "20px sans-serif";
  const titleWidth = context.measureText(title).width;
  if (data == null || data.length === 0 || prop == null || prop.length === 0) {
    maxWidth = titleWidth;
  } else {
    maxWidth = Math.max.apply(
      Math,
      data.map(function(e) {
        return context.measureText(e[prop]).width;
      })
    );
    maxWidth = Math.max(maxWidth, titleWidth);
  }
  maxWidth += 10;
  maxWidth = finalMaxWidth === 0 ? maxWidth : Math.min(maxWidth, finalMaxWidth);
  return maxWidth;
}
