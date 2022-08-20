<template>
  <v-container fluid fill-height class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <log-out></log-out>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import LogOut from "@/components/login/LogOut";

export default {
  name: "LoginView",

  components: {
    LogOut,
  },
};
</script>
