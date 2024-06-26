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
import store from "@/store";

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

let EXPIRESTIME = 86400000;
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem("user");

  // 如果用户访问的是需要登录的页面，并且未登录，则重定向到登录页面
  if (to.matched.some(record => record.meta.requiresAuth) && !isLoggedIn) {
    next({
      path: '/login',
    });
  }
  else if(to.path === '/login'){
    localStorage.setItem("currentPathName",to.name); //设置当前路由名称
    store.commit("setPath");
    next();
  }
  else if (isLoggedIn) {
    let date = new Date().getTime();
    if(date - isLoggedIn.startTime > EXPIRESTIME){
      localStorage.removeItem('user');
      next({
        path: '/login'
      });
    }
    localStorage.setItem("currentPathName",to.name); //设置当前路由名称
    store.commit("setPath");
    next();
  } else {
    next(); // 继续路由导航
  }
});

/* eslint-disable no-new */
new Vue({
  el: "#app",
  render: (h) => h(App),
  router,
  store,
  data: {
    Chartist: Chartist,
  },
});
