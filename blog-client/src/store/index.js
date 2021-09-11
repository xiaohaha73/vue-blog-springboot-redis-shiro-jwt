import Vue from 'vue'
import Vuex from 'vuex'
// 配置axios
import axios from 'axios'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: sessionStorage.getItem('token'), // 后端登陆后发给的token
    userInfo: JSON.parse(sessionStorage.getItem('userInfo')), // 登陆之后的用户信息
    blogInfo: {} // 博客详情
  },
  mutations: {
    // 设置token的值
    setToken: (state, token) => {
      state.token = token
      // 同时将数据存入缓存中
      sessionStorage.setItem('token', token)
    },
    // 设置用户userInfo数据
    setUserInfo: (state, userInfo) => {
      state.userInfo = userInfo
      sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
    },

    // 设置博客详情
    setBlogInfo: (state, blogInfo) => {
      state.blogInfo = blogInfo
    }
  },
  actions: {
    // 异步获取文章详情的函数
    asyncGetBlogInfo: async (context, blogId) => {
      // console.log(blogId)
      const res = await axios.get(`/blog/blog/${blogId}`)
      if (res.status === 200) {
        context.commit('setBlogInfo', res.data.data)
        return res.data.data
      }
    }
  },
  modules: {
  }
})
