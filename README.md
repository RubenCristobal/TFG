## Trabajo Final de Grado en Ingeniería del software
## Aplicación con arquitectura de microservicios para gestionar tutorias entre alumnos y profesores.

Cada directorio corresponde a una parte de la aplicación, los microservicios están desarrollados usando el framework **Spring Boot.**

### Microservicio Api Gateway
Encargado de redirigir las peticiones del cliente externo a cada uno de los microservicios.

### Microservicio Web Client
Interactúa directamente con el cliente a través de una interfaz web elaborado en **Vue.js.**

### Microservicio Booking Service
Microservicio para realizar reservas de tutorías entre alumnos y profesores, y publicar calendario de disponibilidad.

### Microservicio Users service
Se encarga de crear nuevos usuarios, inicio de sesión con seguridad **JWT (Json Web Token).**

### Microservicio Notification service
Notifica por correo electrónico las actualizaciones en las reservas de los usuarios (profesores y alumnos).

### Open API
Descripción de la API REST de la aplicación según la definición de **[Open API](https://www.openapis.org/)** en formato yaml. Además se incluye la colección de Postman de las respectivas peticiones.

### Deployment
Ficheros de despliegue en un clúster de contenedores **Docker** usando docker compose. Incluye:
- Servidor de integración y entrega continua Jenkins (un controlador máster y un agente)
- Microservicios de la aplicación.

