import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

// 路由连续点击报错解决方案
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

const Home = () => import("views/home/home.vue");
const Buying = () => import("views/buying/Buying.vue");
const Presell = () => import("views/presell/Presell.vue");
const Login = () => import("views/login/Login.vue");
const Register = () => import("views/register/Register.vue");
const Wantbuy = () => import("views/buying/Wantbuy.vue");
const Wantsell = () => import("views/presell/Wantsell.vue");
const Detail = () => import("views/detail/Detail.vue");
const User = () => import("views/user/User.vue");
const UserMessage = () => import("views/user/childcomps/UserMessage.vue");
const UserChangeMes = () => import("views/user/childcomps/UserChangeMes.vue");
const UserPlace = () => import("views/user/childcomps/UserPlace.vue");
const UserSafety = () => import("views/user/childcomps/UserSafety.vue");
const purchase = () => import("views/purchase/purchase.vue");
const PetOrder = () => import("views/petorder/PetOrder.vue");
const Pet = () => import("views/pet/Pet.vue");
const Usercenters = () => import("views/usercenters/Usercenters.vue");
const backstage = () => import("views/backstage/backstage.vue");
const petmanage = () => import("views/backstage/petmanage/petmanage.vue");
const petkindmanage = () =>
  import("views/backstage/petmanage/petkindmanage.vue");
const usermanage = () => import("views/backstage/usermanage/usermanage.vue");
const addressmanage = () =>
  import("views/backstage/usermanage/addressmanage.vue");
const petordermanage = () =>
  import("views/backstage/petordermanage/petordermanage.vue");
const mymanage = () => import("views/backstage/mymanage/mymanage.vue");
const about = () => import("views/about/about.vue");

const routes = [
  {
    path: "",
    redirect: "/home",
  },
  {
    path: "/home",
    component: Home,
    meta: {
      title: "ペットの売買",
    },
  },
  {
    path: "/buying",
    component: Buying,
    meta: {
      title: "ペット購入希望",
    },
  },
  {
    path: "/presell",
    component: Presell,
    meta: {
      title: "ペットの予約販売",
    },
  },
  {
    path: "/login",
    component: Login,
    meta: {
      title: "ペット取引・登録",
    },
  },
  {
    path: "/register",
    component: Register,
    meta: {
      title: "ペット取引-サインアップ",
    },
  },
  {
    path: "/about",
    component: about,
  },
  {
    path: "/wantbuy",
    name: "wantbuy",
    component: Wantbuy,
    meta: {
      title: "ペット取引-購入希望",
    },
  },
  {
    path: "/wantsell",
    component: Wantsell,
    meta: {
      title: "ペット取引-販売",
    },
  },
  {
    path: "/detail",
    component: Detail,
    meta: {
      title: "ペット取引-ペット詳細情報",
    },
  },
  {
    path: "/purchase",
    component: purchase,
    meta: {
      title: "購入",
    },
  },
  {
    path: "/usercenters",
    component: Usercenters,
    meta: {
      title: "ユーザー管理",
    },
    children: [
      {
        path: "",
        redirect: "user",
      },
      {
        path: "user",
        component: User,
        meta: {
          title: "ペット取引 - マイページ",
        },
        children: [
          {
            path: "",
            redirect: "message",
          },
          {
            path: "message",
            component: UserMessage,
            meta: {
              title: "個人情報",
            },
          },
          {
            path: "changemessage",
            component: UserChangeMes,
            meta: {
              title: "情報を更新する",
            },
          },
          {
            path: "userplace",
            component: UserPlace,
            meta: {
              title: "住所管理",
            },
          },
          {
            path: "safety",
            component: UserSafety,
            meta: {
              title: "セキュリティセンター",
            },
          },
        ],
      },
      {
        path: "petorder",
        component: PetOrder,
        meta: {
          title: "注文履歴",
        },
      },
      {
        path: "pet",
        component: Pet,
        meta: {
          title: "マイペット",
        },
      },
    ],
  },
  {
    path: "/backstage",
    component: backstage,
    meta: {
      title: "管理画面",
    },
    children: [
      {
        path: "",
        redirect: "mymanage",
      },
      {
        path: "mymanage",
        component: mymanage,
        meta: {
          title: "個人管理",
        },
      },
      {
        path: "usermanage",
        component: usermanage,
        meta: {
          title: "ユーザー管理",
        },
      },
      {
        path: "addressmanage",
        component: addressmanage,
        meta: {
          title: "住所管理",
        },
      },
      {
        path: "petmanage",
        component: petmanage,
        meta: {
          title: "ペット管理",
        },
      },
      {
        path: "petkindmanage",
        component: petkindmanage,
        meta: {
          title: "ペット種類管理",
        },
      },
      {
        path: "petordermanage",
        component: petordermanage,
        meta: {
          title: "注文管理",
        },
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach((to, from, next) => {
  document.title = to.matched[0].meta.title;
  next();
});

// 使页面跳转后回到顶部
router.afterEach((to, from, next) => {
  window.scrollTo(0, 0);
});

export default router;
