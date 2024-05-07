// =========================================================
// * Vue Material Dashboard - v1.5.2
// =========================================================
//
// * Product Page: https://www.creative-tim.com/product/vue-material-dashboard
// * Copyright 2024 Creative Tim (https://www.creative-tim.com)
// * Licensed under MIT (https://github.com/creativetimofficial/vue-material-dashboard/blob/master/LICENSE.md)
//
// * Coded by Creative Tim
//
// =========================================================
//
// * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// router setup
import routes from "./routes/routes";

// Plugins
import GlobalComponents from "./globalComponents";
import GlobalDirectives from "./globalDirectives";

// MaterialDashboard plugin
import MaterialDashboard from "./material-dashboard";

import Chartist from "chartist";
import request from "@/utils/request";

// configure router
const router = new VueRouter({
  routes, // short for routes: routes
  linkExactActiveClass: "nav-item active",
});

Vue.prototype.$Chartist = Chartist;
Vue.prototype.request = request;

Vue.use(VueRouter);
Vue.use(MaterialDashboard);
Vue.use(GlobalComponents);
Vue.use(GlobalDirectives);
Vue.use(ElementUI, {size:"mini"});

router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem("user");

  // 如果用户访问的是需要登录的页面，并且未登录，则重定向到登录页面
  if (to.matched.some(record => record.meta.requiresAuth) && !isLoggedIn) {
    next({
      path: '/login',
      query: { redirect: to.fullPath } // 将用户原本想要访问的页面作为参数传递给登录页面
    });
  } else if (to.path === '/login' && isLoggedIn) {
    // 如果用户已经登录，且试图访问登录页面，则重定向到主页或其他页面
    next('/dashboard'); // 例如跳转到主页
  } else {
    next(); // 继续路由导航
  }
});

/* eslint-disable no-new */
new Vue({
  el: "#app",
  render: (h) => h(App),
  router,
  data: {
    Chartist: Chartist,
  },
});
