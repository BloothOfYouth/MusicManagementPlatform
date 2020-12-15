import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/main.css'
import Axios from 'axios'
import VCharts from 'v-charts'
import moment from 'moment'
import Qs from 'qs'

Vue.use(ElementUI)
Vue.use(VCharts)

// http request 拦截器
Axios.interceptors.request.use(
    config => {
      if (localStorage.getItem("JWT_TOKEN")) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
        config.headers.Token = localStorage.getItem("JWT_TOKEN");
      }
      return config;
    },
    err => {
      return Promise.reject(err);
    });

Axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

Vue.config.productionTip = false

Vue.prototype.$http = Axios

Vue.prototype.$http.defaults.baseURL = "http://localhost:8888"

Vue.filter('formatDate', function (data) {
    if (data) {
        return moment(data).format('YYYY-MM-DD')
    }
})

Vue.filter('formatDetailDate', function (data) {
    if (data) {
        return moment(data).format('YYYY-MM-DD HH:mm:ss')
    }
})

// 路由权限拦截
router.beforeEach((to, from, next) => {
    let token = localStorage.getItem("JWT_TOKEN");
    if (token) {
        let stringify = Qs.stringify({token: token});
        Axios.post("admin/tokenVerity",stringify).then(res => {
            if (res.data.result == "success") {
                if (to.path === "/") {
                    next("/Home");
                }else {
                    next();
                }
            }else {
                localStorage.removeItem("JWT_TOKEN");
                localStorage.removeItem("username");
                next("/");
            }
        }).catch(() => {
            localStorage.removeItem("JWT_TOKEN");
            localStorage.removeItem("username");
            next("/");
        })
    }else {
        if (to.path === "/") {
            next();
        }else {
            next("/")
        }
    }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
