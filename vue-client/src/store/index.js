/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    menuState: "/login",
    currentUser: undefined,
    globalLoading: false,
  },
  getters: {
    menuState(state) {
      return state.menuState;
    },
    currentUser(state) {
      return state.currentUser;
    },
    globalLoading(state) {
      return state.globalLoading;
    },
  },
  mutations: {
    set_menuState(state, menuState) {
      //debugger;
      state.menuState = menuState;
    },
    set_currentUser(state, currentUser) {
      state.currentUser = currentUser;
    },
    set_globalLoading(state, isLoading) {
      state.globalLoading = isLoading;
    },
  },
  actions: {
    set_menuState: function (context, menuState) {
      context.commit("set_menuState", menuState);
    },
    getCurrentUser: async function (context, id, user) {
      // user    enviar new this.MODEL.user()
      //return new Promise(async function (resolve, reject) {
      // const url = "user/current/" + id;
      const url = "https://jsonplaceholder.typicode.com/users/" + id;
      const response = await this.axios
        .get(url)
        .catch((error) => {
          console.log(error);
        })
        .finally(console.log("getCurrentUserProfile"));
      if (response) {
        console.log(response.data);
        user.userUserName = response.data.userUserName;
        user.userName = response.data.userName;
        user.userSurname = response.data.userSurname;
        user.userEmail = response.data.userEmail;
        user.userPassword = response.data.userPassword;
        user.userRol = response.data.userRol;
        user.userTerms = response.data.userTerms;
        //resolve(response.data);
        context.commit("set_currentUser", user);
      } else {
        console.log("something went wrong");
        //resolve(); // reject()
      }
    },
    setCurrentUserAsync: async function (context, currentUser) {
      if (currentUser) {
        context.commit("set_currentUser", currentUser);
      } else {
        console.log("something went wrong");
        //resolve(); // reject()
      }
    },
    //getCurrentUser: async function (context, id, user) =>
    setGlobalLoadingState: function (context, isLoading) {
      context.commit("set_globalLoading", isLoading);
    },
  },
  modules: {},
});
