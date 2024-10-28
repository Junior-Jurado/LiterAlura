# 📚 Proyecto Alura Literatura

Este proyecto, **Alura Literatura**, es una aplicación de línea de comandos desarrollada en **Java** utilizando **Spring Boot**. Su objetivo es gestionar y consultar información sobre libros y autores mediante el uso de una API externa. Permite realizar búsquedas de libros por título, listar autores, consultar estadísticas, y mucho más. La aplicación proporciona una interfaz interactiva para que el usuario navegue por varias funcionalidades que permiten la gestión de libros y autores en una base de datos.

## 🛠️ Tecnologías Utilizadas

- **Java**: Lenguaje de programación utilizado para la implementación del proyecto.
- **Spring Boot**: Framework que facilita el desarrollo de aplicaciones de backend en Java, proporcionando dependencias clave para la creación de REST APIs y el manejo de inyección de dependencias.
- **Maven**: Herramienta de gestión de proyectos utilizada para manejar las dependencias y construir el proyecto.
- **API Gutendex**: Utilizada para obtener información de libros en línea mediante solicitudes HTTP.
- **MySQL** o **H2 Database**: La aplicación soporta múltiples bases de datos, configurables en el archivo `application.properties` de Spring Boot.
  
## 📂 Estructura del Proyecto

El proyecto sigue una arquitectura modular que permite un fácil mantenimiento y escalabilidad. Los principales componentes son:

- **Principal**: Contiene el menú de navegación y las opciones interactivas para el usuario.
- **Modelo**: Define las entidades como `Autor`, `Libro`, etc.
- **Repository**: Interfaz para acceder a la base de datos y realizar operaciones CRUD.
- **Service**: Contiene la lógica de negocio, incluyendo la integración con la API externa.
- **Utilities**: Métodos auxiliares, como el convertidor de datos JSON a objetos Java.

## 🚀 Funcionalidades Principales

1. **Buscar libro por título**: Realiza una búsqueda en la API Gutendex y guarda la información en la base de datos.
2. **Listar libros y autores registrados**: Muestra todos los libros y autores almacenados en la base de datos.
3. **Listar autores vivos en un año específico**: Filtra autores por su año de nacimiento y fallecimiento.
4. **Listar libros por idioma**: Permite filtrar los libros según el idioma.
5. **Estadísticas**: Calcula estadísticas como el promedio de descargas de libros.
6. **Top 10 libros más descargados**: Muestra los libros con más descargas.
7. **Buscar autor por nombre**: Filtra autores por su nombre.
8. **Ordenar autores por año de nacimiento**: Lista a los autores en orden cronológico.

## 📦 Instalación y Ejecución

Sigue los pasos a continuación para configurar y ejecutar el proyecto en tu entorno local.

```bash
# Clona el repositorio en tu máquina local
git clone https://github.com/usuario/alura-literatura.git

# Navega al directorio del proyecto
cd alura-literatura

# Compila el proyecto
mvn clean install

# Ejecuta la aplicación
mvn spring-boot:run
```

¡Gracias por explorar Alura Literatura! 📖✨
