<template>
  <el-container style="flex-direction: column">
    <!--头部组件-->
    <Header/>
    <!--主要区域-->
    <el-main>
      <el-card>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="账号:" prop="username">
            <el-input v-model="ruleForm.username"></el-input>
          </el-form-item>
          <el-form-item label="密码:" prop="password">
            <el-input type="password" v-model="ruleForm.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="submitBtn" type="primary" @click="submitForm('ruleForm')">登陆</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import Header from '../components/Header'

export default {
  name: 'Login',
  components: { Header },
  comments: {
    Header
  },
  data () {
    return {
      ruleForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          {
            required: true,
            message: '请输入登陆账号',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 12,
            message: '长度在 3 到 12 个字符',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入登陆密码',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 12,
            message: '长度在 3 到 12 个字符',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (!valid) {
          console.log('error submit!!')
          return false
        }

        // 发送登陆请求
        const ret = await this.$axios.post('/user/login', {
          username: this.ruleForm.username,
          password: this.ruleForm.password
        })
        // console.log(ret)
        if (ret.status === 200) {
          this.$store.commit('setToken', ret.headers.authorization) // 设置token
          this.$store.commit('setUserInfo', ret.data.data) // 设置用户的登陆信息
          this.$router.push('/')
        }
      })
    }
  }
}
</script>

<style scoped>

.el-card {
  width: 600px;
  margin: 50px auto 0 auto;
  padding-top: 50px;
  padding-right: 60px;
  padding-bottom: 35px;
}

.submitBtn {
  position: absolute;
  left: 47%;
  transform: translateX(-50%);
}
</style>
