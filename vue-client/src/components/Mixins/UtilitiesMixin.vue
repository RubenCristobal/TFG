<script>
export default {
  data: () => ({
    noRules: true,
    rules: {
      uniqueVerifying: [(v) => v < 1 || "Verificando formulario."],
      requieredRuleBoolean: [(v) => !!v || "No puede estar vacío."],
      minChars1Rules: [
        (value) => {
          if (value) {
            value = String(value).trim();
          }
          return (value && value.length >= 1) || "No puede estar vacío.";
        },
      ],
      maxChars100Rules: [
        (value) => {
          if (value) {
            value = String(value).trim();
          }
          return (value && value.length <= 100) || "Máximo 100 caracteres.";
        },
      ],
      maxChars400Rules: [
        (value) => {
          if (value) {
            value = String(value).trim();
          }
          return (value && value.length <= 400) || "Máximo 400 caracteres.";
        },
      ],
      emailRules: [
        (value) => {
          if (value) {
            value = String(value).trim();
          }
          return (
            !value ||
            /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(value) ||
            "E-mail tiene que ser válido"
          );
        },
      ],
      passwordRules: [
        (value) => {
          return (
            !value ||
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/.test(
              value
            ) ||
            "Mínimo 8 caracteres en total, incluir una mayúscula, una minúscula, un número y un símbolo !@#$%^&* "
          );
        },
      ],
      selectRequieredRules: [
        (value) => value !== null || "No puede estar vacío",
        (value) => value !== undefined || "No puede estar vacío",
        (value) => value !== "" || "No puede estar vacío",
      ],
      selectRequieredArrayRules: [
        (value) => value.length > 0 || "No puede estar vacío",
      ],
    },
  }),

  computed: {},

  methods: {
    rePasswordRules(userRePassword, userPassword) {
      if (userRePassword) {
        userRePassword = String(userRePassword).trim();
      }
      if (userPassword) {
        userPassword = String(userPassword).trim();
      }
      return [
        (userRePassword && userPassword && userRePassword === userPassword) ||
          "Debe repetir la contraseña",
      ];
    },
  },
};
</script>
