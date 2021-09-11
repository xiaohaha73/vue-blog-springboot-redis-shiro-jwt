import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import Element from 'element-ui'

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

// 配置axios
import axios from 'axios'

import store from './store'

Vue.use(mavonEditor)

Vue.config.productionTip = false

Vue.prototype.$axios = axios
Vue.prototype.$message = Element.Message

// 配置公共请求头
axios.defaults.baseURL = 'http://localhost:8081'

// 前置路由拦截器
router.beforeEach((to, from, next) => {
  // 获取浏览器存储的token值进行判断是否是登录状态
  const loginToken = window.sessionStorage.getItem('token')
  // 对需要权限的路由进行拦截
  if (to.matched.some(record => record.meta.requireAuth)) { // 判断该路由是否需要登录权限
    // 判断token是否存在
    if (loginToken) {
      // 放行
      next()
    } else {
      // 不存在跳转登陆界面
      next('/login')
    }
  } else {
    // 不需要权限放行
    next()
  }
})

// 响应拦截器
axios.interceptors.response.use(function (res) {
  // 在接收响应做些什么
  // console.log(res)
  if (res.data.status !== 200) {
    return Element.Message.error(res.data.msg)
  }
  return res
}, function (err) {
  // 对响应错误做点什么
  // console.log(err.response)
  Element.Message.error(err.response.data.msg)
  return false
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
