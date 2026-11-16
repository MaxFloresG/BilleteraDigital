# Billetera Digital

**Evaluación T1 — Caso 1: Recarga y Movimientos con Servlet + JSP**

**Universidad:** Universidad Privada del Norte (UPN)  
**Curso:** Soluciones Web y Aplicaciones Distribuidas  
**Tecnologías principales:** Java, Jakarta Servlet, JSP, JSTL y Bootstrap 5

## 1. Descripción del proyecto

Billetera Digital es una aplicación web académica desarrollada en Java que permite administrar un saldo virtual mediante operaciones de recarga y gasto.

La aplicación utiliza una arquitectura basada en el patrón Modelo-Vista-Controlador (MVC), con un Servlet encargado de procesar las solicitudes HTTP y una vista JSP responsable de presentar la información.

El saldo y el historial de movimientos se almacenan temporalmente mediante `HttpSession`, sin utilizar una base de datos ni servicios externos.

## 2. Tecnologías y versiones utilizadas

| Tecnología | Versión | Función |
|---|---|---|
| Java JDK instalado | 25.0.1 | Entorno de desarrollo |
| Java | 17 | Versión objetivo de compilación |
| Apache Maven | 3.10.1 | Gestión de dependencias y compilación |
| Apache Tomcat | 10.1.61 | Servidor de aplicaciones |
| Jakarta Servlet API | 6.0.0 | Procesamiento de solicitudes HTTP |
| Jakarta Server Pages | 3.1 | Renderizado de la interfaz |
| Jakarta JSTL API | 3.0.0 | Biblioteca de etiquetas JSP |
| Jakarta JSTL Implementation | 3.0.1 | Implementación de JSTL |
| Maven Compiler Plugin | 3.13.0 | Compilación del proyecto |
| Maven WAR Plugin | 3.4.0 | Generación del archivo WAR |
| Bootstrap | 5.3.3 | Diseño responsivo |
| Visual Studio Code | — | Editor de desarrollo |
| Windows 11 | — | Sistema operativo |

Bootstrap se carga desde CDN, por lo que se requiere conexión a Internet para visualizar correctamente sus estilos.

## 3. Funcionalidades implementadas

La aplicación permite:

- Consultar un saldo inicial de S/ 250.00.
- Registrar recargas que incrementan el saldo disponible.
- Registrar gastos que disminuyen el saldo disponible.
- Consultar el historial de movimientos.
- Visualizar el tipo, monto y fecha de cada operación.
- Mostrar primero los movimientos más recientes.
- Validar los datos ingresados antes de modificar el saldo.
- Mostrar mensajes de error cuando una operación no es válida.

## 4. Arquitectura del proyecto

La aplicación sigue una organización basada en el patrón MVC.

### 4.1. Modelo — Movimiento.java

Representa cada movimiento registrado en la billetera.

Contiene los siguientes datos:

- Tipo de movimiento: Recarga o Gasto.
- Monto de la operación.
- Fecha y hora del registro.

### 4.2. Controlador — BilleteraServlet.java

Es el componente encargado de recibir y procesar las solicitudes HTTP.

Sus principales métodos son:

- `doGet()`: inicializa la sesión cuando corresponde y muestra la billetera.
- `doPost()`: recibe y valida los datos del formulario, registra movimientos y actualiza el saldo.
- `inicializarSesion()`: establece el saldo inicial y crea el historial vacío.

El controlador utiliza `BigDecimal` para realizar operaciones monetarias con precisión decimal.

Después de registrar correctamente un movimiento, utiliza el patrón Post-Redirect-Get (PRG), que evita repetir la operación al actualizar la página.

### 4.3. Vista — billetera.jsp

Presenta la interfaz de usuario mediante JSP, JSTL y Bootstrap.

Incluye:

- Saldo disponible.
- Formulario para registrar recargas y gastos.
- Historial de movimientos.
- Mensajes de validación.

JSTL permite recorrer y presentar los datos sin utilizar scriptlets Java dentro de la vista.

## 5. Estructura del proyecto

```text
BilleteraDigital/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── billetera/
        │           ├── model/
        │           │   └── Movimiento.java
        │           └── servlet/
        │               └── BilleteraServlet.java
        └── webapp/
            ├── index.jsp
            └── WEB-INF/
                └── vistas/
                    └── billetera.jsp
```

## 6. Requisitos previos

Para compilar y ejecutar el proyecto se necesita:

1. JDK 17 o superior, con `JAVA_HOME` configurado.
2. Apache Maven disponible en la variable `PATH`.
3. Apache Tomcat 10.1.x.
4. Git para clonar el repositorio, o descargar el proyecto como ZIP.
5. Un navegador web.

Verificar las instalaciones de Java y Maven:

```bash
java -version
mvn -version
```

## 7. Instalación y compilación

### Paso 1. Clonar el repositorio

```bash
git clone https://github.com/MaxFloresG/BilleteraDigital.git
```

### Paso 2. Ingresar a la carpeta del proyecto

```bash
cd BilleteraDigital
```

### Paso 3. Compilar y empaquetar

Desde la carpeta que contiene `pom.xml`, ejecutar:

```bash
mvn clean package
```

Si el proceso termina correctamente, Maven mostrará:

```text
BUILD SUCCESS
```

Se generará el siguiente archivo:

```text
target/BilleteraDigital.war
```

## 8. Despliegue en Apache Tomcat

### Paso 1. Ubicar el archivo WAR

Después de la compilación, ingresar a la carpeta `target` y localizar:

```text
BilleteraDigital.war
```

### Paso 2. Copiar el WAR a Tomcat

Copiar el archivo a la carpeta `webapps` de Apache Tomcat.

En el entorno de desarrollo utilizado, Tomcat se encuentra en:

```text
C:\Herramientas\apache-tomcat-10.1.61
```

Por tanto, la carpeta de despliegue es:

```text
C:\Herramientas\apache-tomcat-10.1.61\webapps
```

### Paso 3. Iniciar Tomcat

Abrir PowerShell y ejecutar:

```powershell
cd "C:\Herramientas\apache-tomcat-10.1.61\bin"
.\startup.bat
```

También puede ejecutarse Tomcat desde una terminal con:

```powershell
.\catalina.bat run
```

Se debe utilizar uno de los dos métodos de inicio, no ambos simultáneamente.

### Paso 4. Comprobar el servidor

Abrir:

http://localhost:8080

### Paso 5. Ejecutar la aplicación

Abrir:

http://localhost:8080/BilleteraDigital/billetera

La aplicación debe mostrar el saldo inicial y el formulario de movimientos.

### Paso 6. Detener Tomcat

Desde la carpeta `bin`, ejecutar:

```powershell
.\shutdown.bat
```

**Nota:** Si el puerto 8080 está ocupado o Tomcat utiliza otro puerto, debe ajustarse la dirección de acceso.

## 9. Reglas de negocio y validaciones

### Saldo inicial

Al iniciar una nueva sesión HTTP, el saldo es:

**S/ 250.00**

El historial comienza vacío.

### Recargas

Una recarga válida incrementa el saldo y se registra en el historial.

Ejemplo:

- Saldo anterior: S/ 250.00.
- Recarga: S/ 100.00.
- Saldo resultante: S/ 350.00.

### Gastos

Un gasto válido disminuye el saldo, siempre que exista dinero suficiente.

Ejemplo:

- Saldo anterior: S/ 350.00.
- Gasto: S/ 50.00.
- Saldo resultante: S/ 300.00.

### Operaciones inválidas

La aplicación rechaza:

- Montos vacíos.
- Valores no numéricos.
- Montos iguales a cero.
- Montos negativos.
- Montos con más de dos decimales.
- Gastos superiores al saldo disponible.
- Tipos de movimiento diferentes de Recarga y Gasto.

Las validaciones se realizan en el Servlet.

Cuando una operación no cumple las reglas, se muestra un mensaje de error y no se modifica el saldo ni el historial.

## 10. Pruebas funcionales

Las pruebas parten de una sesión nueva con saldo S/ 250.00.

| N.º | Prueba | Resultado esperado |
|---|---|---|
| 1 | Abrir la aplicación | Saldo S/ 250.00 e historial vacío |
| 2 | Recargar S/ 100.00 | Saldo S/ 350.00 y recarga registrada |
| 3 | Gastar S/ 50.00 | Saldo S/ 300.00 y gasto registrado |
| 4 | Intentar gastar S/ 400.00 | Operación rechazada por saldo insuficiente |
| 5 | Ingresar monto cero | Operación rechazada |
| 6 | Ingresar monto negativo | Operación rechazada |
| 7 | Enviar monto vacío | Operación rechazada |

Las pruebas 2 y 3 se realizan consecutivamente para comprobar la actualización del saldo y el orden del historial.

En las operaciones inválidas, el saldo y los movimientos registrados deben permanecer sin cambios.

## 11. Gestión de la sesión HTTP

La aplicación utiliza `HttpSession` para conservar temporalmente los datos.

Los principales atributos son:

- `saldo`: importe disponible, representado mediante `BigDecimal`.
- `movimientos`: lista de objetos `Movimiento`.

Cada sesión HTTP mantiene sus propios datos.

No se utiliza persistencia permanente. La información puede perderse cuando la sesión expira, se invalida o se reinicia el servidor.

## 12. Repositorio GitHub y entregables

El proyecto se encuentra publicado en:

**https://github.com/MaxFloresG/BilleteraDigital**

El repositorio contiene el código fuente de la aplicación y los archivos de configuración necesarios para compilarla.

### Entregables del Caso 1

- Código fuente Java.
- Vistas JSP.
- Archivo `pom.xml`.
- Archivo `README.md`.
- Archivo ejecutable `BilleteraDigital.war`.

El archivo WAR se obtiene ejecutando:

```bash
mvn clean package
```

Y se genera en:

```text
target/BilleteraDigital.war
```

**Importante:** El WAR debe adjuntarse a la entrega o publicarse en una ubicación accesible para el docente. La carpeta `target` generada localmente no garantiza que el archivo esté disponible en GitHub.

## 13. Limitaciones

Este proyecto corresponde a una implementación académica.

- No utiliza base de datos.
- No implementa autenticación de usuarios.
- No consume APIs externas.
- No utiliza Spring.
- Los movimientos se almacenan temporalmente en la sesión HTTP.
- No representa una billetera financiera real.

## 14. Conclusiones

El proyecto demuestra la implementación de una aplicación web utilizando Java, Jakarta Servlet, JSP y JSTL.

Se aplicó el patrón MVC para separar la representación de los movimientos, el procesamiento de solicitudes y la presentación de la interfaz.

La solución permite registrar recargas y gastos, controlar el saldo disponible, gestionar sesiones HTTP y validar las operaciones antes de modificar la información.

Finalmente, Maven permite generar un archivo WAR para su despliegue en Apache Tomcat, facilitando la reproducción y evaluación del proyecto.
