<template>
  <el-header>
   <p><span>MY</span><em>blog</em></p>
  </el-header>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style scoped>

.el-header {
  text-align: center;
  font-weight: bolder;
}
.el-header p{
  margin: 0 auto;
  position: relative;
  display: inline-block;
  padding: 0 10px 0 10px;
  height: 100%;
}
.el-header span {
  position: absolute;
  top: 50%;
  left: -30px;
  z-index: 1;
  margin: 0 auto;
  transform: translateY(-50%);
  height: 45px;
  line-height: 45px;
  border-radius: 50%;
  width: 45px;
  background: #07a1e3;
}
.el-header em {
  position: absolute;
  z-index: 2;
  top: 50%;
  transform: translateY(-50%);
  right: -24px;
}
</style>
