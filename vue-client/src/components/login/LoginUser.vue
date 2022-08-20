<template>
  <v-container fluid fill-height class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <template>
          <v-card class="mx-auto pa-4" max-width="800" elevation="12">
            <v-toolbar-title class="primary white--text">
              <v-card-title>
                <v-icon x-large dark class="mr-2">mdi-login </v-icon>
                Inicio de sesión
              </v-card-title>
            </v-toolbar-title>

            <v-card-text class="text--primary">
              <template>
                <v-form ref="form" v-model="validLogin">
                  <v-text-field
                    prepend-icon="mdi-account"
                    v-model="loginName"
                    :rules="[
                      ...rules.minChars1Rules,
                      ...rules.maxChars100Rules,
                    ]"
                    label="Nombre Usuario"
                    required
                  ></v-text-field>

                  <v-text-field
                    ref="passwordValid"
                    v-model="password"
                    :prepend-icon="
                      ispasswordIconValid ? 'mdi-lock' : 'mdi-lock-question'
                    "
                    :append-icon="passwordShow ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="passwordShow ? 'text' : 'password'"
                    :counter="100"
                    :rules="[
                      ...rules.minChars1Rules,
                      ...rules.maxChars100Rules,
                      ...rules.passwordRules,
                    ]"
                    label="Contraseña"
                    required
                    @click:append="passwordShow = !passwordShow"
                  ></v-text-field>
                </v-form>
              </template>
            </v-card-text>

            <v-card-actions>
              <v-spacer> </v-spacer>

              <v-btn
                :disabled="!validLogin"
                color="success"
                class="mr-4"
                @click="validateForm"
              >
                Iniciar sesión
              </v-btn>
            </v-card-actions>
          </v-card>
          <div class="mx-auto pa-4 mt-4 text-center">
            <span class="text-h6">¿No tienes una cuenta?</span>
            <v-btn
              color="blue-grey"
              class="mx-4 mt-n2 white--text"
              @click="openDialogRegisterUser"
            >
              Registrarme
            </v-btn>
          </div>
        </template>
      </v-col>
    </v-row>
    <v-dialog
      v-model="dialogRegisterUser"
      persistent
      scrollable
      max-width="800px"
    >
      <v-card>
        <v-card-title>
          <v-toolbar color="primary" dark>
            <v-icon x-large dark class="mr-2">mdi-login </v-icon>
            Nuevo usuario
          </v-toolbar>
        </v-card-title>
        <v-card-text>
          <!-- componente RegisterUser-->
          <RegisterUser
            v-if="dialogRegisterUser"
            :loginName__="loginName"
            @closeDialogRegisterUser="closeDialogRegisterUser"
          ></RegisterUser>
        </v-card-text>
        <v-divider />
      </v-card>
    </v-dialog>
    <template>
      <div class="text-center">
        <v-snackbar
          centered
          v-model="snackbar"
          timeout="3000"
          color="red"
          bottom
        >
          <strong>Credenciales incorrectas</strong>
          <template v-slot:action="{ attrs }">
            <v-btn color="white" text v-bind="attrs" @click="snackbar = false">
              Cerrar
            </v-btn>
          </template>
        </v-snackbar>
      </div>
    </template>
  </v-container>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */
import RegisterUser from "./RegisterUser";
import UtilitiesMixin from "@/components/Mixins/UtilitiesMixin";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "LoginUser",
  mixins: [UtilitiesMixin, AuthMixin],

  components: {
    RegisterUser,
  },

  data: () => ({
    validLogin: true,
    loginName: "",
    password: "",
    passwordShow: false,
    itemsRol: [],
    //
    dialogRegisterUser: false,
    snackbar: false,
    //
  }),

  computed: {
    //...mapState(["projects"]),
    ispasswordIconValid(refs) {
      let valid = false;
      if (this.password && this.userRePassword) {
        valid =
          refs.$refs.passwordValid.valid &&
          refs.$refs.userRePasswordValid.valid;
      }
      return valid;
    },
  },
  created: function () {
    this.itemsRol = this.CONSTS.TIPO_USUARIO.LABEL;
  },
  mounted: function () {},
  methods: {
    closeDialogRegisterUser: function () {
      this.dialogRegisterUser = false;
    },
    openDialogRegisterUser: function () {
      this.$refs.passwordValid.reset();
      this.dialogRegisterUser = true;
    },
    validateForm: async function () {
      const loginUser = new this.MODEL.loginUser(this.loginName, this.password);
      const isValid = await this.validateFormPostLoginUserAsync(loginUser);
      if (!isValid) {
        this.snackbar = true;
      }
    },
  },
};
</script>
