/* jshint esversion: 11 */
import DashboardLayout from "@/pages/Layout/DashboardLayout.vue";

import Dashboard from "@/pages/Dashboard.vue";
import UserProfile from "@/pages/UserProfile.vue";
import Share from "@/pages/Share";
import VueRouter from "vue-router";
const routes = [
  {
    path: "/",
    component: DashboardLayout,
    redirect:"/login",
    children: [
      {
        path: "/dashboard",
        name: "Dashboard",
        meta: { requiresAuth: true },// 添加元数据以表示需要验证
        component: Dashboard,
      },
      {
        path: "/user",
        name: "User Profile",
        meta: { requiresAuth: true }, // 添加元数据以表示需要验证
        component: UserProfile,
      },
      {
        path: "/share",
        name: "Share",
        meta: {requiresAuth: true},
        component: Share
      }
    ],
  },
  {
    path: "/login",
    name: "登录",
    component: () => import(/* webpackChunkName: "about" */ '../pages/login.vue'),
    meta: { noLogin: true }
  },
  {
    path: "/register",
    name: "注册",
    component: () => import(/* webpackChunkName: "about" */ '../pages/register.vue'),
    meta: { noLogin: true }
  }
];






export default routes;
