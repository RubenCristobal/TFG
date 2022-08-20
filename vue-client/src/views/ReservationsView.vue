<template>
  <v-container fluid class="spacing-playground pa-12">
    <v-card elevation="0">
      <v-card-title>
        <div class="elevation-2 py-2 px-8">Reservas</div>
      </v-card-title>
      <v-divider class="mx-4 mt-n3"></v-divider>
      <v-card-text>
        <v-row>
          <v-col cols="12" d-flex align-center>
            <v-data-table
              :headers="tableHeaders"
              :items="bookings"
              sort-by="teacher"
              class="elevation-1"
            >
              <!--           popup reserva -->
              <template v-slot:top>
                <v-toolbar flat>
                  <v-spacer></v-spacer>
                  <v-dialog v-model="dialog" max-width="700px">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        color="success"
                        dark
                        class="mb-2"
                        v-bind="attrs"
                        v-on="on"
                        @click="newItem"
                      >
                        Nueva reserva
                      </v-btn>
                    </template>
                    <v-card>
                      <v-form ref="form" v-model="validPopupBooking">
                        <v-card-title>
                          <span class="text-h5">{{ formTitle }}</span>
                        </v-card-title>

                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-col
                                cols="12"
                                sm="6"
                                md="4"
                                v-if="ifPopupStudentSelector"
                              >
                                <v-autocomplete
                                  v-model="editedItem.studentName"
                                  :items="students"
                                  label="Alumno"
                                  item-text="fullName"
                                  item-value="loginName"
                                  :disabled="disabledEditStudent"
                                  :rules="[...rules.selectRequieredRules]"
                                  required
                                  @blur="eventStudentName(editedItem)"
                                ></v-autocomplete>
                              </v-col>
                              <v-col
                                cols="12"
                                sm="6"
                                md="4"
                                v-if="ifPopupTeacherSelector"
                              >
                                <v-autocomplete
                                  v-model="editedItem.teacherName"
                                  :items="teachers"
                                  label="Profesor"
                                  item-text="fullName"
                                  item-value="loginName"
                                  :disabled="disabledEditTeacher"
                                  :rules="[...rules.selectRequieredRules]"
                                  required
                                  @blur="eventTeacherName(editedItem)"
                                ></v-autocomplete>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-autocomplete
                                  v-model="editedItem.format"
                                  :items="bookingFormats"
                                  label="Formato"
                                  item-text="label"
                                  item-value="id"
                                  :rules="[...rules.selectRequieredRules]"
                                  required
                                ></v-autocomplete>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-menu
                                  v-model="ifPopupCalendarShow"
                                  :close-on-content-click="false"
                                  transition="scale-transition"
                                  offset-y
                                  min-width="auto"
                                >
                                  <template v-slot:activator="{ on, attrs }">
                                    <v-text-field
                                      v-model="editedItem.date"
                                      :disabled="ifPopupCalendar"
                                      label="Día"
                                      readonly
                                      v-bind="attrs"
                                      v-on="on"
                                      :rules="[...rules.selectRequieredRules]"
                                      required
                                    ></v-text-field>
                                  </template>
                                  <v-date-picker
                                    v-model="editedItem.date"
                                    no-tittle
                                    first-day-of-week="1"
                                    :min="minCalendar"
                                    :max="maxCalendar"
                                    :allowed-dates="
                                      allowedDatesWeekDaysPopupCalendar
                                    "
                                    scrollable
                                    locale="ES"
                                    color="primary"
                                    @input="eventCalendar(editedItem)"
                                  ></v-date-picker>
                                </v-menu>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-autocomplete
                                  v-model="editedItem.time"
                                  :items="hours"
                                  :disabled="ifPopupTime"
                                  label="Hora"
                                  item-text="text"
                                  item-value="value"
                                  :rules="[...rules.selectRequieredRules]"
                                  required
                                  @blur="eventHour(editedItem)"
                                ></v-autocomplete>
                              </v-col>

                              <v-col cols="12" sm="12" md="12">
                                <v-textarea
                                  v-model="editedItem.description"
                                  label="Descripción"
                                  clearable
                                  counter
                                  hint="Máximo 400 caracteres"
                                  :rules="[
                                    ...rules.minChars1Rules,
                                    ...rules.maxChars400Rules,
                                  ]"
                                  required
                                >
                                </v-textarea>
                              </v-col>
                            </v-row>
                          </v-container>
                        </v-card-text>

                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn color="red darken-1" text @click="close">
                            Cancelar
                          </v-btn>
                          <v-btn
                            color="blue darken-1"
                            text
                            :disabled="!validPopupBooking"
                            @click="saveAsync"
                          >
                            Guardar
                          </v-btn>
                        </v-card-actions>
                      </v-form>
                    </v-card>
                  </v-dialog>
                  <v-dialog v-model="dialogDelete" max-width="500px">
                    <v-card>
                      <v-card-title class="text-h5"
                        >¿Seguro que quiere borrar esta reserva?</v-card-title
                      >
                      <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="red darken-1" text @click="closeDelete"
                          >Cancelar</v-btn
                        >
                        <v-btn
                          color="blue darken-1"
                          text
                          @click="deleteItemConfirm"
                          >Aceptar</v-btn
                        >
                        <v-spacer></v-spacer>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-toolbar>
              </template>
              <!--           popup reserva -->
              <template v-slot:[`item.isoDateTime`]="{ item }">
                {{ item.isoDateTime.toLocaleDateString() }}
              </template>
              <template v-slot:[`item.timeStart`]="{ item }">
                {{ item.timeStart.toLocaleTimeString() }}
              </template>
              <template v-slot:[`item.timeEnd`]="{ item }">
                {{ item.timeEnd.toLocaleTimeString() }}
              </template>
              <template v-slot:[`item.actions`]="{ item }">
                <v-icon small class="mr-2" @click="editItem(item)">
                  mdi-pencil
                </v-icon>
                <v-icon small @click="deleteItem(item)"> mdi-delete </v-icon>
              </template>
            </v-data-table>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
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
  </v-container>
</template>

<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */
import UtilitiesMixin from "@/components/Mixins/UtilitiesMixin";
import AuthMixin from "@/components/Mixins/AuthMixin";

export default {
  name: "ReservationsView",
  mixins: [UtilitiesMixin, AuthMixin],

  data: () => ({
    dialog: false,
    dialogDelete: false,
    validPopupBooking: true,
    tableHeadersDefault: [
      {
        text: "Alumno",
        align: "start",
        value: "studentFullName",
      },
      {
        text: "Profesor",
        align: "start",
        value: "teacherFullName",
      },
      { text: "Formato", value: "format" },
      { text: "Fecha", value: "isoDateTime" },
      { text: "Hora inicio", value: "timeStart" },
      { text: "Hora fin", value: "timeEnd" },
      { text: "Descripción", value: "description" },
      { text: "Acciones", value: "actions", sortable: false },
    ],
    editedIndex: -1,
    editedItem: {
      date: null,
      description: null,
      format: null,
      id: null,
      isoDateTime: null,
      studentFullName: null,
      studentName: null,
      teacherFullName: null,
      teacherName: null,
      time: null,
      timeEnd: null,
      timeStart: null,
    },
    defaultItem: {
      date: null,
      description: "",
      format: null,
      id: null,
      isoDateTime: null,
      studentFullName: null,
      studentName: null,
      teacherFullName: null,
      teacherName: null,
      time: null,
      timeEnd: null,
      timeStart: null,
    },
    // variables para alumno, profesor, formato //
    teachers: [],
    students: [],
    bookingFormats: [],
    minutesBlockProcess: 60, // bloques 60 minutos
    allBookings: [],
    allBookingsProcess: [],
    // variables para alumno, profesor, formato ^^
    // variables para calendario //
    ifPopupCalendarShow: false,
    today: new Date(Date.now()),
    rangeDaysCalendar: +60,
    minCalendar: null,
    maxCalendar: null,
    // variables para calendario ^^
    // variables para horas //
    hours: [],
    hourFirst: 9,
    hoursLast: 21,
    hoursDefault: [],
    // variables para horas ^^
    // variables para restricciones //
    userRole: null,
    tableHeaders: [],
    bookings: [],
    disabledEditTeacher: null,
    disabledEditStudent: null,
    ifPopupStudentSelector: null,
    ifPopupTeacherSelector: null,
    ifWeekDaysCalendarSelect: {}, // para que no salte error de vuejs, clonada en window.ifWeekDaysCalendarSelect
    // ifPopupCalendar // pasada a computed
    // ifPopupTime // pasada a computed
    // variables para restricciones ^^
    // popup snackbar //
    snackbarShow: false,
    snackbarMessage: {
      text: "",
      color: "success", // success, info, warning, error
    },
    // popup snackbar ^^
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "Nueva reserva" : "Editar Reserva";
    },
    ifPopupCalendar() {
      const result =
        this.editedItem.teacherName === null ||
        this.editedItem.studentName === null;
      return result;
    },
    ifPopupTime() {
      const result = this.editedItem.date === null;
      return result;
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  mounted() {
    this.initialize();
  },

  methods: {
    initialize: async function () {
      this.userRole = this.currentUser.authorities[0].authority;

      await this.getTeachersAsync();
      await this.getStudentsAsync();
      this.getBookingFormats();
      const urlBooking = this.getUrlBookingFromRole(this.userRole);
      await this.getAllBookingsAsync(urlBooking);
      this.hoursDefault = this.getHours();
      this.restrictionCalendarMinMaxDay();
      this.setModeRole();
    },

    setModeRole: function () {
      this.setTableHeaders(this.userRole);
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

    getBookingFormats() {
      this.bookingFormats = this.CONSTS.FORMATS.getList();
    },

    getUrlBookingFromRole: function (userRole) {
      let result = "";

      switch (userRole) {
        case this.CONSTS.TIPO_USUARIO.TEACHER:
          result =
            "localhost:8092/bookings/teacher/" + this.currentUser.loginName;
          break;

        case this.CONSTS.TIPO_USUARIO.STUDENT:
          result =
            "localhost:8092/bookings/student/" + this.currentUser.loginName;
          break;

        case this.CONSTS.TIPO_USUARIO.ADMIN:
          result = "localhost:8092/bookings/all";
          break;

        default:
          console.log("error: getUrlBookingFromRole");
          break;
      }
      return result;
    },

    getAllBookingsAsync: async function (urlBooking) {
      const url = "http://" + urlBooking;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        this.allBookings = response.data;
        this.allBookingsProcess = this.processBookingsDataFromBbdd(
          this.allBookings
        );
        this.bookings = this.allBookingsProcess;
      } else {
        console.log("Error: getAllBookingsAsync");
      }
    },

    getHours: function () {
      const hours = [];
      const schedule = [];
      for (let index = this.hourFirst; index < this.hoursLast + 1; index++) {
        const schedule = {
          text: `${index.toString().padStart(2, "0")}:00`,
          value: `${index.toString().padStart(2, "0")}:00:00`,
          disabled: false,
        };
        hours.push(schedule);
      }
      return hours;
    },

    getCalendarTeacherAsync: async function (teacherName) {
      const url = "http://" + "localhost:8092/calendar/teacher/" + teacherName;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .get(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        if (response.data !== null && response.data.id !== null) {
          return response.data;
        } else {
          return {
            id: null,
            teacherName: null,
            lunes: false,
            martes: false,
            miercoles: false,
            jueves: false,
            viernes: false,
          };
        }
      } else {
        console.log("Error: getCalendarTeacherAsync");
      }
    },

    // Restricciones (por Rol, días, etc...) //
    setTableHeaders: function (userRole) {
      this.tableHeaders = [...this.tableHeadersDefault];
      if (userRole === this.CONSTS.TIPO_USUARIO.STUDENT) {
        delete this.tableHeaders[0];
      } else if (userRole === this.CONSTS.TIPO_USUARIO.TEACHER) {
        delete this.tableHeaders[1];
      }
    },

    setIfPopupTeacher: function (userRole) {
      const self = this;
      this.ifPopupTeacherSelector = ![
        this.CONSTS.TIPO_USUARIO.TEACHER,
      ].includes(userRole);
      if (!this.ifPopupTeacherSelector) {
        this.editedItem.teacherName = this.currentUser.loginName;
        this.conv_teacherFullName(this.editedItem, self.teachers);
      }
    },

    setIfPopupStudent: function (userRole) {
      const self = this;
      this.ifPopupStudentSelector = ![
        this.CONSTS.TIPO_USUARIO.STUDENT,
      ].includes(userRole);
      if (!this.ifPopupStudentSelector) {
        this.editedItem.studentName = this.currentUser.loginName;
        this.conv_studentFullName(this.editedItem, self.students);
      }
    },

    restrictionCalendarMinMaxDay: function () {
      this.today = this.getNewDate();
      this.minCalendar = this.formatDateCalendar(this.today);
      this.maxCalendar = this.formatDateCalendar(
        this.offsetValDate(this.today, {
          date: this.rangeDaysCalendar,
        })
      );
    },

    restrictionCalendarFreeAsync: async function () {
      this.editedItem.date = null;
      this.editedItem.isoDateTime = null;
      this.editedItem.time = null;
      this.editedItem.timeStart = null;
      this.editedItem.timeEnd = null;
      //
      this.hours = [...this.hoursDefault];
      //
      await this.restrictionPopupCalendarWeekDayAsync();
    },

    restrictionHaveHoursFree: function () {
      const setEditedItemTime = this.hours.find(
        (hour) => hour.disabled === false
      );
      return setEditedItemTime;
    },

    restrictionHours: function () {
      this.hours = this.restrictionHoursFree();

      const setEditedItemTime = this.restrictionHaveHoursFree();
      if (setEditedItemTime !== undefined) {
        //si hay hora libre
        this.editedItem.time = setEditedItemTime.value;
      } else {
        this.editedItem.time = null;
      }
    },

    restrictionHoursFree: function () {
      const self = this;
      let teacherHoursBusy = [];
      let studentHoursBusy = [];
      let allHoursBusy = [];
      const hoursFree = [...this.hoursDefault];

      teacherHoursBusy = this.allBookingsProcess.filter((booking) => {
        const result =
          booking.teacherName === self.editedItem.teacherName &&
          booking.date === self.editedItem.date &&
          booking.id !== self.editedItem.id;
        return result;
      });

      studentHoursBusy = this.allBookingsProcess.filter((booking) => {
        const result =
          booking.studentName === self.editedItem.studentName &&
          booking.date === self.editedItem.date &&
          booking.id !== self.editedItem.id;
        return result;
      });

      for (const teacherHourBusy of teacherHoursBusy) {
        allHoursBusy.push(teacherHourBusy.time);
      }

      for (const studentHourBusy of studentHoursBusy) {
        allHoursBusy.push(studentHourBusy.time);
      }

      for (const hourFree of hoursFree) {
        const isBusy = allHoursBusy.includes(hourFree.value);
        hourFree.disabled = isBusy;
      }

      return hoursFree;
    },

    restrictionPopupCalendarWeekDayAsync: async function () {
      if (this.editedItem.teacherName) {
        const calendar = await this.getCalendarTeacherAsync(
          this.editedItem.teacherName
        );
        this.ifWeekDaysCalendarSelect = {
          lunes: calendar.lunes,
          martes: calendar.martes,
          miercoles: calendar.miercoles,
          jueves: calendar.jueves,
          viernes: calendar.viernes,
        };
        window.ifWeekDaysCalendarSelect = this.ifWeekDaysCalendarSelect; // HACK para que no salte error de vuejs, clonado en window.ifWeekDaysCalendarSelect
      }
    },
    // Restricciones (por Rol, días, etc...) ^^

    // utilidades fechas horas //
    getNewDate: function () {
      const returnDate = this.setValDate(new Date(Date.now()), {});
      return returnDate;
    },

    setValDate: function (valDate, set) {
      /*
      set : {
        fullYear : "año completo",
        month : "mes",
        date : "día mes",
        hours : "hora",
        minutes : "minuto",
        seconds : "segundo",
        milliseconds : "milisegundo",
      }
      */
      const returnDate = new Date(
        isNaN(parseInt(set.fullYear))
          ? valDate.getFullYear()
          : parseInt(set.fullYear),
        isNaN(parseInt(set.month)) ? valDate.getMonth() : parseInt(set.month),
        isNaN(parseInt(set.date)) ? valDate.getDate() : parseInt(set.date),
        isNaN(parseInt(set.hours)) ? valDate.getHours() : parseInt(set.hours),
        isNaN(parseInt(set.minutes))
          ? valDate.getMinutes()
          : parseInt(set.minutes),
        isNaN(parseInt(set.seconds))
          ? valDate.getSeconds()
          : parseInt(set.seconds),
        isNaN(parseInt(set.milliseconds))
          ? valDate.getMilliseconds()
          : parseInt(set.milliseconds)
      );
      return returnDate;
    },

    offsetValDate: function (valDate, offset) {
      /*
      offset : {
        fullYear : "año completo",
        month : "mes",
        date : "día mes",
        hours : "hora",
        minutes : "minuto",
        seconds : "segundo",
        milliseconds : "milisegundo",
      }
      */
      const returnDate = new Date(
        isNaN(parseInt(offset.fullYear))
          ? valDate.getFullYear()
          : valDate.getFullYear() + parseInt(offset.fullYear),
        isNaN(parseInt(offset.month))
          ? valDate.getMonth()
          : valDate.getMonth() + parseInt(offset.month),
        isNaN(parseInt(offset.date))
          ? valDate.getDate()
          : valDate.getDate() + parseInt(offset.date),
        isNaN(parseInt(offset.hours))
          ? valDate.getHours()
          : valDate.getHours() + parseInt(offset.hours),
        isNaN(parseInt(offset.minutes))
          ? valDate.getMinutes()
          : valDate.getMinutes() + parseInt(offset.minutes),
        isNaN(parseInt(offset.seconds))
          ? valDate.getSeconds()
          : valDate.getSeconds() + parseInt(offset.seconds),
        isNaN(parseInt(offset.milliseconds))
          ? valDate.getMilliseconds()
          : valDate.getMilliseconds() + parseInt(offset.milliseconds)
      );
      return returnDate;
    },

    formatDateCalendar: function (inputDate) {
      let date, month, year;
      date = inputDate.getDate();
      month = inputDate.getMonth() + 1;
      year = inputDate.getFullYear();
      date = date.toString().padStart(2, "0");
      month = month.toString().padStart(2, "0");
      return `${year}-${month}-${date}`;
    },

    formatHourCalendar: function (inputDate) {
      let hours, minutes, seconds;
      hours = inputDate.getHours().toString().padStart(2, "0");
      minutes = inputDate.getMinutes().toString().padStart(2, "0");
      seconds = inputDate.getSeconds().toString().padStart(2, "0");
      return `${hours}:${minutes}:${seconds}`;
    },

    getYearMonthDateFromCalendar: function (inputDate) {
      const splitDate = inputDate.split("-");
      return {
        fullYear: splitDate[0],
        month: splitDate[1],
        date: splitDate[2],
      };
    },
    // utilidades fechas horas ^^

    // CRUD Original >>
    openPopup: async function () {
      this.setIfPopupTeacher(this.userRole);
      this.setIfPopupStudent(this.userRole);
      await this.restrictionPopupCalendarWeekDayAsync();
      this.hours = this.restrictionHoursFree();
      if (this.editedIndex === -1) {
        this.$refs.form.reset();
      }
    },

    newItem() {
      this.editedItem = Object.assign({}, this.defaultItem);
      this.editedIndex = -1;
      this.disabledEditStudent = false;
      this.disabledEditTeacher = false;
      this.openPopup();
      this.dialog = true;
    },

    editItem(item) {
      this.editedIndex = this.bookings.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.disabledEditStudent = true;
      this.disabledEditTeacher = true;
      this.openPopup();
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.bookings.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteBookingAsync: async function () {
      const url =
        "http://" + "localhost:8092/bookings/delete/" + this.editedItem.id;
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .delete(url, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
          this.snackbarMessage = {
            text: error.response.data.message,
            color: "error", // success, info, warning, error
          };
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        this.snackbarMessage = {
          text: "Reserva eliminada",
          color: "success", // success, info, warning, error
        };
        this.snackbarShow = true;

        return true;
      } else {
        console.log("Error: deleteBookingAsync");
        return false;
      }
    },

    deleteItemConfirm() {
      const result = this.deleteBookingAsync();
      if (result) {
        this.bookings.splice(this.editedIndex, 1);
      }
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    updateBookingAsync: async function () {
      const booking = this.bookingProcessToBbdd(this.editedItem);
      const url = "http://" + "localhost:8092/bookings/update";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .put(url, booking, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
          this.snackbarMessage = {
            text: error.response.data.message,
            color: "error", // success, info, warning, error
          };
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        this.snackbarMessage = {
          text: response.data,
          color: "success", // success, info, warning, error
        };
        this.snackbarShow = true;

        return true;
      } else {
        console.log("Error: updateBookingAsync");
        return false;
      }
    },

    newBookingAsync: async function () {
      const booking = this.bookingProcessToBbdd(this.editedItem);
      delete booking.id;
      const url = "http://" + "localhost:8092/bookings/new";
      this.setGlobalLoadingStateOn();
      let response = await this.axios
        .post(url, booking, { headers: this.axiosHeaders })
        .catch((error) => {
          console.log(error);
          this.snackbarMessage = {
            text: error.response.data.message,
            color: "error", // success, info, warning, error
          };
        })
        .finally(this.setGlobalLoadingStateOf());
      if (response) {
        console.log("OK: ", response);
        this.snackbarMessage = {
          text: "Reserva creada",
          color: "success", // success, info, warning, error
        };
        this.snackbarShow = true;

        return response.data;
      } else {
        console.log("Error: newBookingAsync");
        return false;
      }
    },

    saveAsync: async function () {
      let result;
      if (this.editedIndex > -1) {
        result = await this.updateBookingAsync();
        if (result) {
          Object.assign(this.bookings[this.editedIndex], this.editedItem);
        }
      } else {
        result = await this.newBookingAsync();
        this.editedItem.id = result.id;
        if (result) {
          this.bookings.push(this.editedItem);
        }
      }
      this.close();
    },
    // CRUD Original ^^

    // utilidades convertir //

    conv_isoDateTime(booking) {
      booking.isoDateTime = new Date(booking.date + " " + booking.time);
    },

    conv_timeStart(booking, minutesBlockProcess) {
      const timeEndMinutesFix =
        Math.round(booking.isoDateTime.getMinutes() / minutesBlockProcess) *
        minutesBlockProcess;
      booking.timeStart = this.setValDate(booking.isoDateTime, {
        minutes: timeEndMinutesFix,
        seconds: 0,
        milliseconds: 0,
      });
    },

    conv_timeEnd(booking) {
      booking.timeEnd = this.offsetValDate(booking.timeStart, {
        minutes: +60,
      });
    },

    conv_teacherFullName(booking, teachers) {
      const teacherData = teachers.filter((teacher) => {
        return teacher.loginName === booking.teacherName;
      });
      booking.teacherFullName = teacherData[0].fullName;
    },

    conv_studentFullName(booking, students) {
      const studentData = students.filter((student) => {
        return student.loginName === booking.studentName;
      });
      booking.studentFullName = studentData[0].fullName;
    },

    // utilidades convertir ^^

    // popup reserva //
    allowedDatesWeekDaysPopupCalendar: (date) => {
      const ifWeekDaysCalendarSelect = window.ifWeekDaysCalendarSelect; // HACK para que no salte error de vuejs, clonado en window.ifWeekDaysCalendarSelect

      let result = false;

      if (ifWeekDaysCalendarSelect !== undefined) {
        const thisDay = new Date(Date.now());
        const splitDate = date.split("-");
        const inputDate = {
          fullYear: splitDate[0],
          month: splitDate[1],
          date: splitDate[2],
        };

        thisDay.setFullYear(parseInt(inputDate.fullYear));
        thisDay.setMonth(parseInt(inputDate.month) - 1);
        thisDay.setDate(parseInt(inputDate.date));

        const weekDay = thisDay.getDay(); // 0..6 lunes..domingo

        if (weekDay === 1 && ifWeekDaysCalendarSelect.lunes === true) {
          result = true;
        } else if (weekDay === 2 && ifWeekDaysCalendarSelect.martes === true) {
          result = true;
        } else if (
          weekDay === 3 &&
          ifWeekDaysCalendarSelect.miercoles === true
        ) {
          result = true;
        } else if (weekDay === 4 && ifWeekDaysCalendarSelect.jueves === true) {
          result = true;
        } else if (weekDay === 5 && ifWeekDaysCalendarSelect.viernes === true) {
          result = true;
        }
      }
      return result;
    },

    bookingBbddToProcess: function (booking) {
      const self = this;

      // isoDateTime  // .toISOString();
      this.conv_isoDateTime(booking);

      //timeStart
      this.conv_timeStart(booking, self.minutesBlockProcess);

      // time, normalizado a bloques de tiempo
      booking.time = this.formatHourCalendar(booking.timeStart);

      // timeEnd
      this.conv_timeEnd(booking);

      // teacherFullName
      this.conv_teacherFullName(booking, self.teachers);

      // studentFullName
      this.conv_studentFullName(booking, self.students);

      //return
      return booking;
    },

    bookingProcessToBbdd: function (booking) {
      const result = {
        date: booking.date,
        description: booking.description,
        format: booking.format,
        id: booking.id,
        studentName: booking.studentName,
        teacherName: booking.teacherName,
        time: booking.time,
      };
      return result;
    },

    processBookingsDataFromBbdd: function (allBookings) {
      const result = [];
      for (const item in allBookings) {
        if (Object.hasOwnProperty.call(allBookings, item)) {
          const booking = { ...allBookings[item] };
          result[item] = this.bookingBbddToProcess(booking);
        }
      }
      return result;
    },

    processBookingsDataToBbdd: function (allBookingsProcess) {
      const result = [];
      for (const item in allBookingsProcess) {
        if (Object.hasOwnProperty.call(allBookingsProcess, item)) {
          const booking = { ...allBookingsProcess[item] };
          result[item] = this.bookingProcessToBbdd(booking);
        }
      }
      return result;
    },
    // popup reserva ^^

    // popup campos eventos //
    eventTeacherName: function (booking) {
      const self = this;
      if (booking.teacherName) {
        this.conv_teacherFullName(booking, self.teachers);
        this.restrictionCalendarFreeAsync();
      }
    },

    eventStudentName: function (booking) {
      const self = this;
      if (booking.studentName) {
        this.conv_studentFullName(booking, self.students);
        this.restrictionCalendarFreeAsync();
      }
    },

    eventCalendar: function (booking) {
      const self = this;
      booking.time = booking.time !== null ? booking.time : self.hours[0].value;
      this.conv_isoDateTime(booking);
      this.conv_timeStart(booking, self.minutesBlockProcess);
      booking.time = this.formatHourCalendar(booking.timeStart);
      this.conv_timeEnd(booking);
      this.restrictionHours();
      this.ifPopupCalendarShow = false;
    },

    eventHour: function (booking) {
      const self = this;
      if (booking.time !== null) {
        this.conv_isoDateTime(booking);
        this.conv_timeStart(booking, self.minutesBlockProcess);
        booking.time = this.formatHourCalendar(booking.timeStart);
        this.conv_timeEnd(booking);
      }
    },
    // popup campos eventos ^^
  },
};
</script>
