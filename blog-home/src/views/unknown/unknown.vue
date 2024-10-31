<template>
  <div>
    <div class="unknown-banner banner"></div>
    <img src="../../assets/img/unknown.png" alt="" />
  </div>
</template>

<style scoped>
.unknown-banner {
  background: linear-gradient(-45deg, #ee7752, #ce3e75, #23a6d5, #23d5ab);
  animation: Gradient 10s ease infinite;
  background-size: 400% 400%;
  z-index: -2;
}
img {
  position: absolute;
  z-index: -1;
  width: 100%;
  height: 100%;
}
@media (max-width: 759px) {
  img {
    width: 200%;
    left: -187px;
  }
}
@keyframes Gradient {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>
