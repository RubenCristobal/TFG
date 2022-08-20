<template>
  <v-container fluid fill-height class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <template>
          <v-card class="mx-auto pa-4" max-width="800" elevation="12">
            <v-toolbar-title class="primary white--text">
              <v-card-title>
                <v-icon x-large dark class="mr-2">mdi-logout </v-icon>
                Cerrar sesión
              </v-card-title>
            </v-toolbar-title>

            <v-card-text class="text--primary py-16">
              <h2 class="text-center">¿Desea cerrar la sesión?</h2>
            </v-card-text>

            <v-card-actions>
              <v-spacer> </v-spacer>
              <v-btn color="error" class="mr-4" @click="logOut">
                Cerrar sesión
              </v-btn>
              <v-btn color="grey" class="mr-4 white--text" @click="returnHome">
                Cancelar
              </v-btn>
            </v-card-actions>
          </v-card>
        </template>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */
import RegisterUser from "./RegisterUser";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "LogOut",
  mixins: [AuthMixin],

  data: () => ({
    validLogin: true,
    loginName: "",
    userPassword: "",
    userPasswordShow: false,

    itemsRol: [],
    //
    dialogRegisterUser: false,
    //
  }),

  computed: {
    //...mapState(["projects"]),
    isUserPasswordIconValid(refs) {
      let valid = false;
      if (this.userPassword && this.userRePassword) {
        valid =
          refs.$refs.userPasswordValid.valid &&
          refs.$refs.userRePasswordValid.valid;
      }
      return valid;
    },
  },
  created: function () {
    this.itemsRol = this.CONSTS.TIPO_USUARIO.LABEL;
  },
  // mounted: function () {},
  methods: {
    logOut: function () {
      this.$router.replace({
        path: "/login",
      });
    },
    returnHome: function () {
      this.$router.replace({
        path: this.getPathCloseSessionFromRole(),
      });
    },

    getPathCloseSessionFromRole: function () {
      let result = "";
      const userRole = this.currentUser.authorities[0].authority;

      switch (userRole) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          result = "/home";
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          result = "/home";
          break;

        case this.CONSTS.TIPO_USUARIO.ADMIN:
          result = "/reservations";
          break;

        default:
          console.log("error: getPathCloseSessionFromRole");
          break;
      }
      return result;
    },
  },
};
</script>
