<template>
  <v-container fluid fill-height class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <login-user></login-user>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import LoginUser from "@/components/login/LoginUser";

export default {
  name: "LoginView",

  components: {
    LoginUser,
  },
};
</script>
