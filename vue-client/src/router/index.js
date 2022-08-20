import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import ReservationsView from "../views/ReservationsView.vue";
import CalendarView from "../views/CalendarView.vue";
import LogoutView from "../views/LogoutView.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/home",
    name: "homeView",
    component: HomeView,
  },
  {
    path: "/login",
    name: "LoginView",
    component: LoginView,
  },
  {
    path: "/reservations",
    name: "Reservas",
    component: ReservationsView,
  },
  {
    path: "/calendar",
    name: "Calendario",
    component: CalendarView,
  },
  {
    path: "/logout",
    name: "Logout",
    component: LogoutView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
