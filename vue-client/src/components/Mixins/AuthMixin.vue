<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

import { mapState } from "vuex";
import ModelsMixin from "@/components/Mixins/ModelsMixin";

export default {
  mixins: [ModelsMixin],
  data: () => ({
    axiosHeaders: undefined,
    base_url: "",
  }),

  computed: {
    ...mapState(["menuState", "currentUser", "globalLoading"]),
  },

  watch: {
    currentUser: {
      immediate: true,
      deep: true,
      handler: function (val, oldVal) {
        if (val !== undefined) {
          this.setAxiosHeaders();
        }
      },
    },
  },

  mounted: function () {},

  methods: {
    setGlobalLoadingStateOn: function () {
      this.$store.dispatch("setGlobalLoadingState", true);
    },
    setGlobalLoadingStateOf: function () {
      this.$store.dispatch("setGlobalLoadingState", false);
    },
    setAxiosHeaders: function () {
      if (this.currentUser) {
        this.axiosHeaders = {
          "Access-Control-Allow-Origin": "*",
          Authorization: `Bearer ${this.currentUser.token}`,
        };
      }
    },

    ejemploDePeticionCopiarPegarAsync: async function () {
      const url = "http://" + "localhost:8092/bookings/all";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
      } else {
        console.log("Error: ejemploDePeticionCopiarPegarAsync");
      }
    },

    validateFormPostLoginUserAsync: async function (loginUser) {
      //valida y crea currentUser en vuex
      const url = "http://" + "localhost:8091/auth/login";
      const loginHeaders = {
        "Access-Control-Allow-Origin": "*",
      };
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .post(url, loginUser, { headers: loginHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());

      if (response) {
        const userLogin = new this.MODEL.jwtDto(
          response.data.token,
          response.data.userName /*loginName*/,
          response.data.authorities
        );
        this.$store.dispatch("setCurrentUserAsync", userLogin);
        const pathGo =
          userLogin["authorities"][0]["authority"] ===
          this.CONSTS.TIPO_USUARIO.ADMIN
            ? "/reservations"
            : "/home";
        this.$router.replace({
          path: pathGo,
        });
      } else {
        console.log("Error: setGlobalLoadingStateOn");
        return false;
      }
    },
    validateFormPostRegisterUserAsync: async function (userDataRegister) {
      const url = "http://" + "localhost:8091/auth/new";
      this.setGlobalLoadingStateOn();
      const response = await this.axios
        .post(url, userDataRegister)
        .catch((error) => {
          this.snackbarMessage = {
            text: error.response.data.message,
            color: "error",
          };
          this.snackbarShow = true;
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.snackbarMessage = {
          text: "Usuario registrado",
          color: "success",
        };
        this.snackbarShow = true;

        return true;
      } else {
        console.log("something went wrong");
        return false;
      }
    },
  },
};
</script>
