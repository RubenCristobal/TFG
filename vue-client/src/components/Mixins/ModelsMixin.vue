<script>
/* eslint-disable no-unused-vars */
/* eslint-disable no-debugger */

export default {
  data: () => ({
    CONSTS: {
      TIPO_USUARIO: {
        LABEL: ["Alumno", "Profesor", "Administrador"],
        STUDENT: "STUDENT",
        TEACHER: "TEACHER",
        ADMIN: "ADMIN",
        getList: function () {
          return this.LABEL.map((item, indice) => {
            return { id: indice, label: item };
          });
        },
        getRoles: function () {
          return [
            { text: "Alumno", value: "student" },
            { text: "Profesor", value: "teacher" },
            { text: "Administrador", value: "admin" },
          ];
        },
      },
      FORMATS: {
        LABEL: ["ONLINE", "PRESENCIAL"],
        ONLINE: "ONLINE",
        PRESENCIAL: "PRESENCIAL",
        getList: function () {
          return [
            { id: "ONLINE", label: "ONLINE" },
            { id: "PRESENCIAL", label: "PRESENCIAL" },
          ];
        },
      },
    },
    MODEL: {
      //lo que envia al login
      loginUser: function (loginName, password) {
        this.loginName = loginName;
        this.password = password;
      },
      //lo que se devuelve una vez logado
      jwtDto: function (token, loginName, authorities) {
        this.token = token;
        this.loginName = loginName;
        this.authorities = authorities;
      },
      //Usuario
      user: function (id, loginName, name, surname, email, rol) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.rol = rol;
      },
      //Meeting
      meeting: function (
        teacher,
        student,
        typeMeeting,
        location,
        date,
        startTime,
        endTime,
        state,
        message
      ) {
        this.teacher = teacher;
        this.student = student;
        this.meeting = typeMeeting;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.message = message;
      },
    },
  }),
  computed: {},
  methods: {},
};
</script>
