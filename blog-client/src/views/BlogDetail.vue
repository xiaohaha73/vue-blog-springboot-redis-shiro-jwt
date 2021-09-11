<template>
  <el-container style="flex-direction: column;height: 100%">
    <Header/>
    <el-main>
      <el-card>
        <p style="font-weight: bolder;text-align: center;position: relative">
          {{title}}
          <el-button
            type="primary" style="position: absolute;right: 15px;top: -17px"
            @click="jumpHandle"
            v-show="isShow"
          >
            编辑
          </el-button>
        </p>
        <div v-html="content" class="content markdown-body"></div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import Header from '../components/Header'
import 'github-markdown-css'
import MarkdownIt from 'markdown-it'

export default {
  name: 'BlogDetail',
  components: { Header },
  comments: {
    Header
  },
  mounted () {
    // 获取详情
    var blogId = this.$route.query.blogId
    this.blogId = blogId
    // console.log(blogId)
    this.renderData(blogId)
  },
  data () {
    return {
      blogId: '',
      title: '',
      desc: '',
      content: '',
      isShow: false // 编辑按钮的显示和隐藏
    }
  },
  methods: {
    async renderData (blogId) {
      // 调用vuex中封装好的获取文章详情的函数
      const blogInfo = await this.$store.dispatch('asyncGetBlogInfo', blogId)
      // console.log(blogInfo)
      this.title = blogInfo.title
      this.desc = blogInfo.description
      // this.content = blogInfo.content
      var Markdown = new MarkdownIt()
      var result = Markdown.render(blogInfo.content)
      this.content = result

      // 判断编辑按钮的显示和隐藏 博客userId和当前登陆用户相同
      if (this.$store.state.userInfo && this.$store.state.userInfo.id === blogId.userId) {
        this.isShow = true
      }
    },

    // 页面跳转
    jumpHandle () {
      this.$router.push(`/edit/${this.blogId}`)
    }
  }
}
</script>

<style scoped>

</style>
