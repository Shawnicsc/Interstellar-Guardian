import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";


Vue.use(VueRouter)

let routes = [
	{
		// will match everything
		path: '*',
		component: () => import('../views/404.vue'),
	},
	{
		path: '/',
		name: 'Home',
		redirect: '/sign-in',
	},
	{
		path: '/dashboard',
		name: '首页',
		layout: "dashboard",
		meta: {
			requiresAuth: true
		},
		// route level code-splitting
		// this generates a separate chunk (about.[hash].js) for this route
		// which is lazy-loaded when the route is visited.
		component: () => import(/* webpackChunkName: "dashboard" */ '../views/Dashboard.vue'),
	},

	{
		path: '/ipfsService',
		name: 'IPFS文件业务',
		layout: "dashboard",
		meta: {
			requiresAuth: true
		},
		component: () => import('../views/ipfsService.vue'),
	},
	{
		path: '/crypto',
		name: '字符串加密',
		layout: "dashboard",
		meta: {
			requiresAuth: true
		},
		component: () => import('../views/DownLoad.vue'),
	},
	{
		path: '/layout',
		name: 'Layout',
		layout: "dashboard",
		component: () => import('../views/Layout.vue'),
	},
	{
		path: '/Profile',
		name: '个人中心',
		layout: "dashboard",
		meta: {
			layoutClass: 'layout-profile',
			requiresAuth: true
		},
		component: () => import('../views/Profile.vue'),
	},
	{
		path: '/share',
		name: '分享',
		layout: "dashboard",
		component: () => import('../views/share.vue'),
	},
	{
		path: '/sign-in',
		name: 'Sign-In',
		component: () => import('../views/Sign-In.vue'),
	},
	{
		path: '/sign-up',
		name: 'Sign-Up',
		meta: {
			layoutClass: 'layout-sign-up',
		},
		component: () => import('../views/Sign-Up.vue'),
	},
]


function addLayoutToRoute( route, parentLayout = "default" )
{
	route.meta = route.meta || {} ;
	route.meta.layout = route.layout || parentLayout ;

	if( route.children )
	{
		route.children = route.children.map( ( childRoute ) => addLayoutToRoute( childRoute, route.meta.layout ) ) ;
	}
	return route ;
}

routes = routes.map( ( route ) => addLayoutToRoute( route ) ) ;

const router = new VueRouter({
	mode: 'hash',
	base: process.env.BASE_URL,
	routes,
	scrollBehavior (to, from, savedPosition) {
		if ( to.hash ) {
			return {
				selector: to.hash,
				behavior: 'smooth',
			}
		}
		return {
			x: 0,
			y: 0,
			behavior: 'smooth',
		}
	}
})

let EXPIRESTIME = 86400000;
router.beforeEach((to, from, next) => {
	const isLoggedIn = localStorage.getItem("user");

	// 如果用户访问的是需要登录的页面，并且未登录，则重定向到登录页面
	if (to.matched.some(record => record.meta.requiresAuth) && !isLoggedIn) {
		next({
			path: '/sign-in',
		});
	}
	else if(to.path === '/sign-in' && isLoggedIn){
		next({
			path: '/dashboard'
		});
	}
	else if (isLoggedIn) {
		let date = new Date().getTime();
		if(date - isLoggedIn.startTime > EXPIRESTIME){
			localStorage.removeItem('user');
			next({
				path: '/sign-in'
			});
		}
		localStorage.setItem("currentPathName",to.name); //设置当前路由名称
		store.commit("setPath");
		next();
	} else {
		next(); // 继续路由导航
	}
})

export default router
