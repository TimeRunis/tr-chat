import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import LemonIMUI from 'lemon-imui';
import 'lemon-imui/dist/index.css';

Vue.use(LemonIMUI);
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
