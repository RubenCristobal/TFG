<template>
  <v-container fluid class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <v-card elevation="0">
          <v-card-title>
            <div class="elevation-2 py-2 px-8">Próxima tutoría</div>
          </v-card-title>
          <v-divider class="mx-4 mt-n3"></v-divider>
          <v-card-text>
            <v-card class="mb-8" elevation="0">
              <v-card-title>
                Tienes
                <v-sheet rounded="xl" class="mx-2 px-2 primary white--text">{{
                  count
                }}</v-sheet
                >citas programadas
              </v-card-title>
            </v-card>
            <v-card class="mx-auto" max-width="600" v-if="count">
              <v-card-text>
                <div>
                  <p class="text-h6">Próxima cita el:</p>
                  <v-row class="text-h4 text--primary">
                    <v-col>{{ nextBooking.date }}</v-col>
                    <v-col>{{ timeHour }}</v-col>
                  </v-row>
                </div>
                <div>
                  <p class="text-h6 mt-8">Formato:</p>
                  <p class="text-h6 text--primary">{{ nextBooking.format }}</p>
                </div>
                <v-divider class="pb-4"></v-divider>
                <v-row>
                  <v-col cols="2" class="mt-2">
                    <v-avatar class="mb-4" color="grey darken-1" size="64">
                      <v-icon x-large color="grey lighten-4"
                        >mdi-account</v-icon
                      >
                    </v-avatar>
                  </v-col>
                  <v-col cols="10">
                    <div>
                      <p class="text-h6">{{ typeRole }}:</p>
                      <p class="text-h6 text--primary">{{ userFullName }}</p>
                    </div>
                  </v-col>
                </v-row>
              </v-card-text>
            </v-card>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

import { mapState } from "vuex";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "HomeView",

  mixins: [AuthMixin],

  data: () => ({
    teachers: [],
    students: [],
    userFullName: "",
    timeHour: "",
    count: 0,
    userRole: "",
    nextBooking: {
      id: -1, // 1,
      studentName: "",
      teacherName: "",
      date: "",
      time: "",
      format: "",
      description: "",
    },
  }),

  computed: {
    ...mapState(["menuState", "currentUser", "globalLoading"]),
    typeRole() {
      let result = "";
      switch (this.currentUser.authorities[0].authority) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          result = this.CONSTS.TIPO_USUARIO.LABEL[0]; //student
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          result = this.CONSTS.TIPO_USUARIO.LABEL[1]; //teacher
          break;
      }
      return result;
    },
  },

  mounted: function () {
    this.initialize();
  },
  methods: {
    initialize: async function () {
      this.userRole = this.currentUser.authorities[0].authority;

      const countUrl = this.getCountUrl(this.userRole);
      const nextUrl = this.getNextUrl(this.userRole);
      await this.getTeachersAsync();
      await this.getStudentsAsync();
      await this.getCountAsync(countUrl);
      await this.getNextAsync(nextUrl);
      this.userFullName = this.setUserFullName();
      this.timeHour = this.setTimeHour();
    },

    getCountUrl: function (userRole) {
      let result = "";

      switch (userRole) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          result = "localhost:8092/bookings/teacher/count/";
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          result = "localhost:8092/bookings/student/count/";
          break;

        case this.CONSTS.TIPO_USUARIO.ADMIN:
          console.log("error: getCountUrl");
          break;

        default:
          console.log("error: getCountUrl");
          break;
      }
      return result;
    },
    getNextUrl: function (userRole) {
      let result = "";

      switch (userRole) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          result = "localhost:8092/bookings/teacher/next/";
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          result = "localhost:8092/bookings/student/next/";
          break;

        case this.CONSTS.TIPO_USUARIO.ADMIN:
          console.log("error: getCountUrl");
          break;

        default:
          console.log("error: getCountUrl");
          break;
      }
      return result;
    },

    getCountAsync: async function (countUrl) {
      const url = "http://" + countUrl + this.currentUser.loginName;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        if (response.data !== null) {
          this.count = response.data;
        }
      } else {
        console.log("Error: getCountAsync");
      }
    },

    getNextAsync: async function (nextUrl) {
      const url = "http://" + nextUrl + this.currentUser.loginName;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        if (response.data !== null) {
          this.nextBooking = response.data;
        }
      } else {
        console.log("Error: getNextAsync");
      }
    },

    getTeachersAsync: async function () {
      const url = "http://" + "localhost:8091/user/teachers";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.teachers = response.data;
        this.teachers.map(
          (teacher) =>
            (teacher.fullName = `${teacher.firstName} ${teacher.lastName}`)
        );
      } else {
        console.log("Error: getTeachersAsync");
      }
    },

    getStudentsAsync: async function () {
      const url = "http://" + "localhost:8091/user/students";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.students = response.data;
        this.students.map(
          (student) =>
            (student.fullName = `${student.firstName} ${student.lastName}`)
        );
      } else {
        console.log("Error: getStudentsAsync");
      }
    },

    setUserFullName() {
      let result = "";
      let users = [];
      let user = "";
      switch (this.userRole) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          users = this.students;
          user = this.nextBooking.studentName;
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          users = this.teachers;
          user = this.nextBooking.teacherName;
          break;
      }
      const userFullName = users.filter(
        (itemUser) => itemUser.loginName === user
      );
      result = userFullName.length > 0 ? userFullName[0].fullName : "";
      return result;
    },

    setTimeHour() {
      let result =
        this.nextBooking.time !== null
          ? this.nextBooking.time.substring(0, 5)
          : "";
      return result;
    },
  },
};
</script>
