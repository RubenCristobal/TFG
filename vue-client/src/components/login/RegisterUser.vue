<template>
  <v-form ref="form" v-model="validRegister">
    <v-text-field
      prepend-icon="mdi-note-edit"
      v-model="name"
      :counter="100"
      :rules="[...rules.minChars1Rules, ...rules.maxChars100Rules]"
      label="Nombre"
      required
    ></v-text-field>
    <v-text-field
      prepend-icon="mdi- "
      v-model="surname"
      :counter="100"
      :rules="[...rules.minChars1Rules, ...rules.maxChars100Rules]"
      label="Apellidos"
      required
    ></v-text-field>
    <v-text-field
      prepend-icon="mdi-account"
      v-model="loginName"
      :counter="100"
      :rules="[...rules.minChars1Rules, ...rules.maxChars100Rules]"
      label="Nombre usuario"
      required
    ></v-text-field>
    <v-text-field
      prepend-icon="mdi-at "
      v-model="email"
      :rules="[
        ...rules.minChars1Rules,
        ...rules.maxChars100Rules,
        ...rules.emailRules,
      ]"
      label="E-mail"
      required
      :error-messages="errorMessageEmail"
    ></v-text-field>

    <v-text-field
      ref="userPasswordValid"
      v-model="password"
      :prepend-icon="isUserPasswordIconValid ? 'mdi-lock' : 'mdi-lock-question'"
      :append-icon="userPasswordShow ? 'mdi-eye' : 'mdi-eye-off'"
      :type="userPasswordShow ? 'text' : 'password'"
      :counter="100"
      :rules="[
        ...rules.minChars1Rules,
        ...rules.maxChars100Rules,
        ...rules.passwordRules,
      ]"
      label="Contraseña"
      required
      @click:append="userPasswordShow = !userPasswordShow"
    ></v-text-field>

    <v-text-field
      ref="userRePasswordValid"
      v-model="userRePassword"
      prepend-icon="mdi- "
      :append-icon="userRePasswordShow ? 'mdi-eye' : 'mdi-eye-off'"
      :type="userRePasswordShow ? 'text' : 'password'"
      :counter="100"
      :rules="[
        ...rules.minChars1Rules,
        ...rules.maxChars100Rules,
        ...rePasswordRules(userRePassword, password),
      ]"
      label="Repetir contraseña"
      required
      @click:append="userRePasswordShow = !userRePasswordShow"
    ></v-text-field>

    <v-select
      v-model="rol"
      prepend-icon="mdi-briefcase-account "
      :items="itemsRol"
      :rules="[...rules.selectRequieredRules]"
      label="Tipo de usuario"
      required
    ></v-select>

    <v-sheet class="d-flex justify-end">
      <v-btn
        color="success"
        class="mr-4"
        @click="checkVerifingForm"
        :disabled="!validRegister"
      >
        Guardar
      </v-btn>
      <v-btn class="mr-4" @click="closeDialogRegisterUser"> Cerrar </v-btn>
    </v-sheet>
    <template>
      <div class="text-center">
        <v-snackbar
          centered
          v-model="snackbarShow"
          timeout="3000"
          :color="snackbarMessage.color"
          bottom
        >
          <strong>{{ snackbarMessage.text }}</strong>
          <template v-slot:action="{ attrs }">
            <v-btn
              color="white"
              text
              v-bind="attrs"
              @click="snackbarShow = false"
            >
              Cerrar
            </v-btn>
          </template>
        </v-snackbar>
      </div>
    </template>
  </v-form>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

import UtilitiesMixin from "@/components/Mixins/UtilitiesMixin";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "RegisterUser",

  mixins: [UtilitiesMixin, AuthMixin],

  emits: ["closeDialogRegisterUser"],

  props: {
    loginName__: {
      type: String,
      required: true,
    },
  },

  data: () => ({
    validRegister: true,
    name: "",
    surname: "",
    loginName: "",
    email: "",
    errorMessageEmail: "",
    password: "",
    userPasswordShow: false,
    userRePassword: "",
    userRePasswordShow: false,
    rol: "",
    terms: "",
    itemsRol: [],
    validate: true,
    uniqueVerifying: 0,
    userNameUniqueRules: [],
    emailUniqueRules: [],
    snackbarShow: false,
    snackbarMessage: {
      text: "",
      color: "error", // success, info, warning, error
    },
  }),

  computed: {
    isUserPasswordIconValid(refs) {
      let valid = false;
      if (this.password && this.userRePassword) {
        valid =
          refs.$refs.userPasswordValid.valid &&
          refs.$refs.userRePasswordValid.valid;
      }
      return valid;
    },
  },
  created: function () {
    this.itemsRol = this.CONSTS.TIPO_USUARIO.getRoles();
  },

  watch: {},
  mounted: function () {
    this.loginName = this.loginName__;
  },
  methods: {
    closeDialogRegisterUser: function () {
      this.$emit("closeDialogRegisterUser");
    },

    checkVerifingForm: async function () {
      const userDataRegister = {
        loginName: this.loginName,
        email: this.email,
        password: this.userRePassword,
        firstName: this.name,
        lastName: this.surname,
        roles: [this.rol],
      };
      const resultValidate = await this.validateFormPostRegisterUserAsync(
        userDataRegister
      );
      if (resultValidate) {
        this.closeDialogRegisterUser();
      }
    },
  },
};
</script>
