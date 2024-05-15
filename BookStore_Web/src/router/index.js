import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Product from "@/views/Product.vue";
import Authorization from "@/views/Authorization.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    },
    {
      path: '/book/:id',
      name: 'Product',
      component: Product
    },
    {
      path: '/login',
      name: 'Authorization',
      component: Authorization
    }
  ]
})

export default router
