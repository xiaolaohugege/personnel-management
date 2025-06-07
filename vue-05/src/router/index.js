import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', name: 'home', component: import('../views/Login.vue'),},
    {path: '/about',  component: import('../views/About.vue'),},
    {path: '/notFount',  component: import('../views/404.vue'),},
    {path: '/:pathMathc(.*)',  redirect:'/notFount',},
    {path:'/manager', component: import('../views/Manager.vue'),
      children:[
        {path: 'children',  component: import('../views/Children.vue'),},
        {path: 'user',  component: import('../views/User.vue'),},
        {path: 'home',  component: import('../views/Home.vue'),},
        {path: 'apply',  component: import('../views/Apply.vue'),},
        {path: 'person',  component: import('../views/Person.vue'),},

      ]
    },

    {path:'/register', component: import('../views/Register.vue'),},

  ],
})

export default router
