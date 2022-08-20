<template>
  <v-app id="tfg">
    <v-app-bar app>
      <v-btn
        v-if="showDrawer"
        color="primary"
        dark
        elevation="0"
        @click="drawer = !drawer"
      >
        <v-icon class="mr-2">mdi-menu</v-icon>
        <span v-if="drawer">Ocultar Menú</span>
        <span v-else>Mostrar Menú</span>
      </v-btn>
      <v-spacer></v-spacer>
      <v-alert
        class="mt-4"
        color="primary"
        type="info"
        icon="mdi-account-school"
        dense
      >
        <strong>Reservas de tutorías</strong>
      </v-alert>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" v-if="showDrawer" app>
      <v-sheet color="grey lighten-4" class="pa-4">
        <v-avatar class="mb-4" color="grey darken-1" size="64">
          <v-icon x-large color="grey lighten-4">mdi-account</v-icon>
        </v-avatar>

        <div class="mb-4" v-if="currentUser">
          <strong>{{ currentUser.loginName }}</strong>
          <p class="font-weight-light font-italic">{{ currentRoleDisplay }}</p>
        </div>
      </v-sheet>

      <v-divider></v-divider>

      <v-list :key="menuState">
        <v-list-item
          v-for="link in linkStates"
          :key="link.text"
          :disabled="link.disabledItem"
          v-show="link.visible"
          @click="
            goLink(
              link.icon,
              link.text,
              link.url,
              link.visible,
              link.disabledItem
            )
          "
        >
          <v-list-item-icon>
            <v-icon>{{ link.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ link.text }} </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-main>
      <v-card elevation="0" height="4" :loading="globalLoading"> </v-card>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
    <v-footer app height="50%">
      <v-card-text class="py-2 text-center" style="font-size: 1em">
        <strong
          >Proyecto de Fin de Grado — Rubén Cristóbal García — 2022</strong
        >
      </v-card-text>
    </v-footer>
  </v-app>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

import { mapState } from "vuex";

import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "App",

  mixins: [AuthMixin],

  components: {},

  data: () => ({
    drawer: false,
    showDrawer: false,

    linksDefault: {
      home: {
        icon: "mdi-home",
        text: "Home",
        url: "/home",
        visible: true,
        disabledItem: false,
        menuState: "mainMenu",
      },
      login: {
        icon: "mdi-login",
        text: "Login",
        url: "/login",
        visible: true,
        disabledItem: false,
        menuState: "login",
      },
      reservations: {
        icon: "mdi-book-information-variant",
        text: "Reservas",
        url: "/reservations",
        visible: true,
        disabledItem: false,
        menuState: "mainMenu",
      },
      calendar: {
        icon: "mdi-calendar-clock",
        text: "Calendario",
        url: "/calendar",
        visible: true,
        disabledItem: false,
        menuState: "mainMenu",
      },
      logOut: {
        icon: "mdi-delete",
        text: "Cerrar sesión",
        url: "/logout",
        visible: true,
        disabledItem: false,
        menuState: "mainMenu",
      },
    },
    linkStates: [],

    //
  }),

  computed: {
    ...mapState(["menuState", "currentUser", "globalLoading"]),
    currentRoleDisplay() {
      let result = "";
      if (this.currentUser) {
        const role = this.currentUser.authorities[0].authority;
        if (role === this.CONSTS.TIPO_USUARIO.STUDENT) {
          result = this.CONSTS.TIPO_USUARIO.LABEL[0];
        } else if (role === this.CONSTS.TIPO_USUARIO.TEACHER) {
          result = this.CONSTS.TIPO_USUARIO.LABEL[1];
        } else if (role === this.CONSTS.TIPO_USUARIO.ADMIN) {
          result = this.CONSTS.TIPO_USUARIO.LABEL[2];
        }
      }
      return result;
    },
  },

  watch: {
    $route(val, oldVal) {
      this.getMenuState(val.path);
      this.updateDrawer();
    },
    currentUser(val, oldVal) {
      if (val) {
        this.updateLinkRole();
      }
    },
  },

  created: function () {
    //this.$store.dispatch("getCurrentUserProfile");
    if (
      this.menuState === "/login" ||
      this.currentUser === undefined ||
      this.currentUser === null
    ) {
      // ir a login
      this.replaceLink(this.linksDefault.login);
    }
  },

  mounted: function () {},

  methods: {
    getMenuState(val) {
      switch (val) {
        case "/login":
          this.$store.dispatch("set_menuState", "login");
          this.linkStates = [this.linksDefault.login];
          break;
        case "/home":
        case "/reservations":
        case "/calendar":
        case "/logout":
          this.$store.dispatch("set_menuState", "mainMenu");
          this.linkStates = [
            this.linksDefault.home,
            this.linksDefault.reservations,
            this.linksDefault.calendar,
            this.linksDefault.logOut,
          ];
          break;
        default:
          this.linkStates = [this.linksDefault.login];
          break;
      }
    },
    replaceLink(link) {
      if (this.$route.path != link.url) {
        this.$router.replace({
          path: link.url,
          /*query: { name: text },*/
          params: {
            icon: link.icon,
            visible: link.visible,
            disabledItem: link.disabledItem,
          },
        });
      }
    },
    goLink(icon, text, url, visible, disabledItem) {
      if (this.$route.path != url) {
        this.$router.push({
          path: url,
          /*query: { name: text },*/
          params: { icon: icon, visible: visible, disabledItem: disabledItem },
        });
      }
    },
    updateDrawer() {
      this.showDrawer = this.menuState != this.linksDefault.login.menuState;
      this.drawer = this.menuState != this.linksDefault.login.menuState;
    },
    updateLinkRole: function () {
      this.linksDefault.calendar.visible = [
        this.CONSTS.TIPO_USUARIO.TEACHER,
      ].includes(this.currentUser.authorities[0].authority);

      this.linksDefault.home.visible = [
        this.CONSTS.TIPO_USUARIO.TEACHER,
        this.CONSTS.TIPO_USUARIO.STUDENT,
      ].includes(this.currentUser.authorities[0].authority);
    },
  },
};
</script>
