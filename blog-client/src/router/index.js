import Vue from 'vue'
import VueRouter from 'vue-router'
import Blogs from '../views/Blogs.vue'
import Login from '../views/Login'
import BlogEdit from '../views/BlogEdit'
import BlogDetail from '../views/BlogDetail'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Blogs',
    component: Blogs
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/edit/:id', // 73422
    name: 'edit',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/add',
    name: 'add',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/detail',
    name: 'BlogDetail',
    component: BlogDetail
  }
]

const router = new VueRouter({
  routes
})

export default router
