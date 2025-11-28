import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path:'/login',meta:{name:'登录'},component:()=>import('../views/Login.vue') },
    { path:'/register',meta:{name:'注册'},component:()=>import('../views/Register.vue') },
    {
      path: '/manage', name: 'manage',component: ()=>import('../views/Manage.vue'),
      redirect: '/manage/home',  // 重定向到首页
      children:[
        { path:'home',meta:{name:'首页'},component:()=>import('../views/manage/Home.vue')},
        { path:'notice',meta:{name:'公告管理'},component:()=>import('../views/manage/Notice.vue')},
        { path:'type',meta:{name:'分类管理'},component:()=>import('../views/manage/Type.vue')},
        { path:'goods',meta:{name:'物品管理'},component:()=>import('../views/manage/Goods.vue')},
        { path:'order',meta:{name:'订单管理'},component:()=>import('../views/manage/Order.vue')},
        { path:'user',meta:{name:'用户信息'},component:()=>import('../views/manage/User.vue')},
        { path:'admin',meta:{name:'管理员信息'},component:()=>import('../views/manage/Admin.vue')},
        { path:'userInfo',meta:{name:'个人信息'},component:()=>import('../views/manage/UserInfo.vue')},
        { path:'password',meta:{name:'修改密码'},component:()=>import('../views/manage/Password.vue')},
      ]
    },
    {
      path: '/front',
      name: 'Front',
      component: () => import('../views/Front.vue'),
      children: [
        { path: 'index', name: 'Index', meta: { name: '系统首页'}, component:() => import('../views/front/Index.vue') },
        { path: 'notice', name: 'Notice', meta: { name: '修改密码'}, component:() => import('../views/front/Notice.vue')  },
        { path: 'goods', name: 'Goods', meta: { name: '物品列表'}, component:() => import('../views/front/Goods.vue')  },
        { path: 'goodsDetail', name: 'GoodsDetail', meta: { name: '物品详情'}, component:() => import('../views/front/GoodsDetail.vue')  },
        { path: 'center',name: 'Center',component:() => import('../views/front/Center.vue'),redirect: '/front/center/userInfo',
        children: [
          { path: 'collect', name: 'Collect', meta: { name: '我的收藏' }, component:() => import('../views/front/center/Collect.vue')  },
          { path: 'buy', name: 'Buy', meta: { name: '我买到的' }, component:() => import('../views/front/center/Buy.vue')  },
          { path: 'sell', name: 'Sell', meta: { name: '我卖出的' }, component:() => import('../views/front/center/Sell.vue')  },
          { path: 'myGoods', name: 'MyGoods', meta: { name: '我的发布' }, component:() => import('../views/front/center/MyGoods.vue')  },
          { path: 'password', name: 'Password', meta: { name: '修改密码' }, component:() => import('../views/front/center/Password.vue')  },
          { path: 'userInfo', name: 'UserInfo', meta: { name: '个人信息' }, component:() => import('../views/front/center/UserInfo.vue')  },
               ]
        },
              ]
     },
  ],
})

router.beforeEach((to ,from, next) => {
    document.title = '校园二手交易市场';
    if (to.path === '/') {
        return next("/login");
    }
  const token= localStorage.getItem('token')
   console.log(token)
    //front有页面 打开下方的
    if (to.path !== '/login' && to.path !== '/register' && to.path !== '/front/index') {
        if (!token){
            next("/login");}
    }
    const user =JSON.parse(localStorage.getItem('userInfo'))
    if (to.path.includes("manage")&&user.role==='user'){
        next('/front/index')
    }
 /*   if (to.path.includes("front")&&user.role!='user'){
        next('/manage/home')
    }
*/
    next();
})


export default router
