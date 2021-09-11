<template>
  <el-container style="flex-direction: column;height: 100%">
    <Header/>
    <el-main>
      <el-card>
        <el-form :model="blogForm" :rules="rules" ref="blogForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="文章标题" prop="title">
            <el-input v-model="blogForm.title"></el-input>
          </el-form-item>
          <el-form-item label="文章介绍" prop="desc">
            <el-input type="textarea" v-model="blogForm.desc"></el-input>
          </el-form-item>
          <el-form-item label="文章内容" prop="content">
            <mavon-editor v-model="blogForm.content"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('blogForm')">立即创建</el-button>
            <el-button @click="resetForm('blogForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import Header from '../components/Header'

export default {
  name: 'BlogEdit',
  components: { Header },
  comments: {
    Header
  },
  mounted () {
    // 接收传递过来的文章id来判断是修改文章还是新增文章
    var id = this.$route.params.id
    if (!id) {
      this.isUpdate = false
    } else { // 更新文章的情况调用函数获取文章内容
      this.renderData(id)
      this.isUpdate = true
      // console.log(id)
    }
  },
  data () {
    return {
      isUpdate: false, // 判断是否是更新文章
      blogForm: {
        id: '',
        userId: '',
        title: '',
        desc: '',
        content: ''
      },
      rules: {
        title: [
          {
            required: true,
            message: '请输入文章标题',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 50,
            message: '长度在 1 到 15 个字符',
            trigger: 'blur'
          }
        ],
        desc: [
          {
            required: true,
            message: '请输入文章描述',
            trigger: 'blur'
          },
          {
            min: 1,
            max: 100,
            message: '长度在 1 到 50 个字符',
            trigger: 'blur'
          }
        ],
        content: [
          {
            required: true,
            message: '文章内容不能为空',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    // 根据id获取文章详情
    async renderData (blogId) {
      // 调用vuex中封装好的获取文章详情的函数
      const blogInfo = await this.$store.dispatch('asyncGetBlogInfo', blogId)
      // console.log(blogInfo)
      this.blogForm.id = blogInfo.id
      this.blogForm.title = blogInfo.title
      this.blogForm.desc = blogInfo.description
      this.blogForm.content = blogInfo.content
    },
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (!valid) {
          this.$message.error('提交失败')
          return false
        }
        const {
          id,
          userId,
          desc,
          content,
          title
        } = this.blogForm
        // 根据修改和新增文章调用不同的接口
        if (this.isUpdate) {
          // 调用更新文章的接口
          const res = await this.$axios.post('/blog/update',
            {
              id: id,
              userId: userId,
              title: title,
              description: desc,
              content: content
            },
            {
              headers: {
                Authorization: sessionStorage.getItem('token') // 需要携带token
              }
            }
          )

          if (res.status === 200) {
            // 修改成功
            this.$router.push('/')
            this.$message.success('修改成功')
            return false
          }
        } else {
          // 创建新文章
          // 调用添加文章的接口
          const res = await this.$axios.post('/blog/blog',
            {
              id: id,
              title: title,
              description: desc,
              content: content
            },
            {
              headers: {
                Authorization: sessionStorage.getItem('token') // 需要携带token
              }
            }
          )

          if (res.status === 200) {
            // 修改成功
            this.$router.push('/')
            this.$message.success('添加成功')
            return false
          }
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>

</style>
