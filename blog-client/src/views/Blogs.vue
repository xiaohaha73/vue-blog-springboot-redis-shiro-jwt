<template>
  <div class="home" style="height: 100%">
    <el-container style="flex-direction: column;height: 100%">
      <Header/>
      <el-main>
        <!--用户详情-->
        <el-card class="userInfo">
          <p class="title">欢迎来到mylog的博客</p>
          <div class="avatar">
            <img :src="isLogin? avatar:'/assets/logo.png'" alt="">
            <small style="font-weight: bolder">{{ isLogin ? username : '请登录' }}</small>
          </div>

          <ul>
            <li v-if="isLogin">
              <router-link to="/">主页</router-link>
            </li>
            <li v-else>
              <router-link to="/login">登陆</router-link>
            </li>
            <li>
              <router-link to="/" style="color: #88b77a">发表文章</router-link>
            </li>
            <li v-if="isLogin">
              <a @click="logout" style="cursor: pointer">退出</a>
            </li>
          </ul>
        </el-card>
        <!--文章列表-->
        <el-card class="blogInfo">
          <el-timeline>
            <el-timeline-item
              v-for="(item,index) in blogList"
              v-bind:key="index"
              :timestamp="item.created"
              placement="top">
              <el-card style="cursor: pointer">
                <router-link :to="{path:'/detail',query:{blogId:item.id}}">
                  <h4>{{ item.title }}</h4>
                </router-link>
                <div class="description" style="color: #409eff">{{item.description}}</div>
                <div class="content">{{item.content}}</div>
                <p>王小虎 提交于 2018/4/12 20:46</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>

          <el-pagination
            class="pagination"
            background
            layout="prev, pager, next"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            @current-change="handleCurrentChange"
          >
          </el-pagination>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Header from '../components/Header'

export default {
  name: 'Blogs',
  components: {
    Header
  },
  data () {
    return {
      username: '',
      avatar: '',
      isLogin: false, // 判断登陆状态
      blogList: [], // 博客列表
      pageSize: 3, // 每页的条数
      total: 0, // 总条数
      currentPage: 1 // 当前页
    }
  },
  mounted () {
    // 判断是否处于登陆
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
    // console.log(userInfo)
    if (userInfo !== null && userInfo !== undefined) {
      this.isLogin = true
      this.username = userInfo.username
      this.avatar = userInfo.avatar === '' || userInfo.avatar === null ? '/assets/logo.png' : userInfo.avatar
    }

    // 获取博客列表
    this.renderBlogs()
  },
  methods: {
    // 退出登陆函数
    async logout () {
      const res = await this.$axios.get('/user/logout', {
        headers: {
          Authorization: sessionStorage.getItem('token') // 需要携带token
        }
      })
      if (res.status === 200) {
        this.$message.success('退出登陆成功')
        this.$router.push('/login')
        sessionStorage.clear() // 清除缓存
      }
    },

    // 渲染博客列表
    async renderBlogs (page = 1) {
      const res = await this.$axios.get(`/blog/blogs?currentPage=${page}`)
      // console.log(res)
      if (res.status === 200) {
        this.blogList = res.data.data.records
        this.currentPage = res.data.data.current
        this.total = res.data.data.total
        this.pageSize = res.data.data.size
      }
    },

    // 选中页面函数
    handleCurrentChange (val) {
      this.renderBlogs(val)
    }

  }
}
</script>
<style scoped>
.el-main {
  display: flex;
  flex-direction: row;
  flex: 1;
  overflow-y: hidden;
}

.el-main .userInfo {
  width: 285px;
  height: 189px;
  text-align: center;
  box-sizing: border-box;
}

.el-main .userInfo .title {
  font-weight: bolder;
}

.el-main .userInfo .avatar {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.el-main .userInfo .avatar img {
  margin: 15px auto 10px auto;
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.el-main .userInfo ul {
  list-style: none;
  display: flex;
  flex-direction: row;
  justify-content: center;
  padding: 0;
  margin-top: 10px;
  /*width: 100%;*/
  /*height: 40px;*/
}

.el-main .userInfo li {
  padding: 0;
  margin: 0;
  display: block;
}

.el-main .userInfo li a {
  text-decoration: none;
  font-size: 13px;
  padding: 0 5px;
}

.el-main .userInfo li:nth-child(1) a {
  color: #738392;
  border-right: 1px solid #738392;
}

.el-main .userInfo li:last-child a {
  color: #ed8f8a;
  border-left: 1px solid #738392;
}

.el-main .blogInfo {
  margin-left: 10px;
  flex: 1;
  height: 100%;
  position: relative;
  /*overflow-y: auto;*/
}

.el-main .blogInfo .pagination {
  position: absolute;
  bottom: 15px;
  left: 50%;
  transform: translateX(-40%);
}

.description,.content {
  margin-top: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
