import Vue from 'vue'
import VueRouter from 'vue-router'
import ChatView from "@/views/ChatView";

Vue.use(VueRouter)

const routes = [
  {
    path: '/chat',
    name: 'chat',
    component: ChatView
  },
  {
    path: '/',
    redirect: '/chat',
  },
]

const router = new VueRouter({
  routes
})

export default router
