<template>
  <v-container fluid class="spacing-playground pa-12">
    <v-row>
      <v-col cols="12" d-flex align-center>
        <v-card elevation="0">
          <v-card-title>
            <div class="elevation-2 py-2 px-8">Calendario</div>
          </v-card-title>
          <v-divider class="mx-4 mt-n3"></v-divider>
          <v-card-text>
            <v-row>
              <v-col cols="12" d-flex align-center>
                <v-card class="pa-4">
                  <v-container fluid>
                    <v-form ref="form" v-model="valid" lazy-validation>
                      <v-row>
                        <v-col>
                          <v-checkbox
                            v-model="checkbox_1"
                            label="Lunes"
                            required
                          ></v-checkbox>
                        </v-col>
                        <v-col>
                          <v-checkbox
                            v-model="checkbox_2"
                            label="Martes"
                            required
                          ></v-checkbox>
                        </v-col>
                        <v-col>
                          <v-checkbox
                            v-model="checkbox_3"
                            label="Miércoles"
                            required
                          ></v-checkbox>
                        </v-col>
                        <v-col>
                          <v-checkbox
                            v-model="checkbox_4"
                            label="Jueves"
                            required
                          ></v-checkbox>
                        </v-col>
                        <v-col>
                          <v-checkbox
                            v-model="checkbox_5"
                            label="Viernes"
                            required
                          ></v-checkbox>
                        </v-col>
                      </v-row>
                    </v-form>
                  </v-container>
                </v-card>
              </v-col>
            </v-row>
          </v-card-text>
          <v-card-actions>
            <v-row>
              <v-col></v-col>
              <v-col class="d-flex justify-end">
                <v-btn
                  :disabled="!valid"
                  color="success"
                  class="mr-4"
                  @click="saveCalendar"
                >
                  Guardar
                </v-btn>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-col>
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
    </v-row>
  </v-container>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */
import { mapState } from "vuex";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "CalendarView",
  mixins: [AuthMixin],

  computed: {
    ...mapState(["currentUser"]),
  },

  data: () => {
    return {
      valid: false,
      calendarId: null,
      checkbox_1: false,
      checkbox_2: false,
      checkbox_3: false,
      checkbox_4: false,
      checkbox_5: false,
      isCalendarNew: true,
      snackbarShow: false,
      snackbarMessage: {
        text: "",
        color: "success", // success, info, warning, error
      },
    };
  },

  watch: {},

  mounted() {
    this.initialize();
  },

  methods: {
    initialize() {
      this.getCalendarTeacherAsync();
    },

    getCalendarTeacherAsync: async function () {
      const url =
        "http://" +
        "localhost:8092/calendar/teacher/" +
        this.currentUser.loginName;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
          this.isCalendarNew = true;
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        if (response.data !== null && response.data.id !== null) {
          this.isCalendarNew = false;
          this.responseDataSet(response.data);
        } else {
          this.isCalendarNew = true;
        }
      } else {
        console.log("Error: getCalendarTeacherAsync");
      }
    },

    saveCalendar: function () {
      if (this.isCalendarNew) {
        this.setCalendarAsync();
      } else {
        this.updateCalendarAsync();
      }
    },

    setCalendarAsync: async function () {
      const sendData = this.sendData();
      delete sendData.id;
      const url = "http://" + "localhost:8092/calendar/new";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .post(url, sendData, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.calendarId = response.data.id;
        this.isCalendarNew = false;
        this.snackbarMessage = {
          text: "Calendario actualizado",
          color: "success", // success, info, warning, error
        };
        this.snackbarShow = true;

        console.log("OK: ", response);
      } else {
        console.log("Error: setCalendarAsync");
      }
    },

    updateCalendarAsync: async function () {
      const sendData = this.sendData();
      const url = "http://" + "localhost:8092/calendar/update";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .put(url, sendData, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error); // posible snackBars en ul li
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.isCalendarNew = false;
        this.snackbarMessage = {
          text: "Calendario actualizado",
          color: "success", // success, info, warning, error
        };
        this.snackbarShow = true;

        console.log("OK: ", response);
      } else {
        console.log("Error: updateCalendarAsync");
      }
    },

    sendData() {
      const sendData = {
        id: this.calendarId,
        teacherName: this.currentUser.loginName,
        lunes: this.checkbox_1,
        martes: this.checkbox_2,
        miercoles: this.checkbox_3,
        jueves: this.checkbox_4,
        viernes: this.checkbox_5,
      };
      return sendData;
    },
    responseDataSet(responseData) {
      this.calendarId = responseData.id;
      this.checkbox_1 = responseData.lunes;
      this.checkbox_2 = responseData.martes;
      this.checkbox_3 = responseData.miercoles;
      this.checkbox_4 = responseData.jueves;
      this.checkbox_5 = responseData.viernes;
    },
  },
};
</script>
