
# Billetera Digital

## Caso 1: Recarga y Movimientos — Servlet + JSP

Aplicación web académica desarrollada para la evaluación T1 del curso **Soluciones Web y Aplicaciones Distribuidas** de la Universidad Privada del Norte (UPN).

El proyecto implementa una billetera digital utilizando Java, Jakarta Servlets, JSP, JSTL y Bootstrap. Permite consultar el saldo disponible, registrar recargas y gastos, y visualizar el historial de movimientos.

Los datos se almacenan temporalmente en `HttpSession`, sin utilizar base de datos ni servicios externos.

## 1. Tecnologías y versiones utilizadas

- Java JDK instalado: 25.0.1.
- Versión objetivo de compilación: Java 17.
- Apache Maven: 3.10.1.
- Apache Tomcat: 10.1.61.
- Jakarta Servlet API: 6.0.0.
- Jakarta Server Pages (JSP): 3.1.
- Jakarta JSTL API: 3.0.0.
- Implementación Jakarta JSTL: 3.0.1.
- Maven Compiler Plugin: 3.13.0.
- Maven WAR Plugin: 3.4.0.
- Bootstrap: 5.3.3.
- Editor de desarrollo: Visual Studio Code.
- Sistema operativo: Windows 11.

Bootstrap se carga desde CDN, por lo que requiere conexión a Internet para aplicar sus estilos.

## 2. Requisitos previos

Para compilar y ejecutar el proyecto se requiere:

1. Tener instalado JDK 17 o superior y configurar `JAVA_HOME`.
2. Tener instalado Apache Maven y configurado en `PATH`.
3. Tener instalado Apache Tomcat 10.1.x.
4. Contar con un navegador web.

Verificar las instalaciones mediante:

```bash
java -version
mvn -version
```

## 3. Estructura del proyecto

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

## 4. Arquitectura de la aplicación

El proyecto utiliza una organización basada en el patrón Modelo-Vista-Controlador (MVC).

### Modelo: Movimiento.java

Representa cada operación realizada en la billetera.

Almacena:
- Tipo de movimiento: Recarga o Gasto.
- Monto de la operación.
- Fecha y hora del movimiento.

### Controlador: BilleteraServlet.java

Procesa las solicitudes HTTP de la aplicación.

- `doGet()`: inicializa la sesión y muestra el saldo e historial.
- `doPost()`: recibe, valida y procesa las operaciones.
- `inicializarSesion()`: establece el saldo inicial y crea el historial vacío.

### Vista: billetera.jsp

Muestra la interfaz gráfica utilizando JSP, JSTL y Bootstrap.

Incluye:
- Saldo disponible.
- Historial de movimientos.
- Formulario de recargas y gastos.
- Mensajes de error.

## 5. Compilación del proyecto

Abrir una terminal en la carpeta que contiene el archivo `pom.xml`.

Ejecutar:

```bash
mvn clean package
```

Si la compilación termina correctamente, Maven mostrará:

```text
BUILD SUCCESS
```

Se generará el archivo:

```text
target/BilleteraDigital.war
```

## 6. Despliegue en Apache Tomcat

1. Ubicar el archivo `BilleteraDigital.war` dentro de `target`.
2. Copiarlo a la carpeta `webapps` de Apache Tomcat.
3. Iniciar Tomcat ejecutando `startup.bat` desde su carpeta `bin`, o ejecutar `catalina.bat run` desde una terminal.
4. Esperar a que Tomcat despliegue la aplicación.
5. Abrir en el navegador:

```text
http://localhost:8080/BilleteraDigital/billetera
```

En el entorno utilizado, Tomcat se encuentra instalado en:

```text
C:\Herramientas\apache-tomcat-10.1.61
```

Para comprobar que Tomcat está funcionando, abrir:

```text
http://localhost:8080
```

## 7. Funcionalidades

### Saldo inicial

Al iniciar una nueva sesión, la billetera muestra:

```text
S/ 250.00
```

El historial comienza vacío.

### Recarga

Permite agregar un monto positivo al saldo.

Ejemplo:

- Saldo anterior: S/ 250.00.
- Recarga: S/ 100.00.
- Saldo resultante: S/ 350.00.

### Gasto

Permite descontar un monto positivo siempre que exista saldo suficiente.

Ejemplo:

- Saldo anterior: S/ 350.00.
- Gasto: S/ 50.00.
- Saldo resultante: S/ 300.00.

### Historial

Los movimientos válidos se registran con tipo, monto y fecha.

El historial muestra primero el movimiento más reciente.

## 8. Validaciones

La aplicación rechaza:

- Montos vacíos.
- Valores no numéricos.
- Montos iguales a cero.
- Montos negativos.
- Montos con más de dos decimales.
- Gastos superiores al saldo disponible.
- Tipos de movimiento distintos de `Recarga` o `Gasto`.

Cuando una operación es inválida, el saldo y el historial permanecen sin cambios.

Las validaciones se realizan en el Servlet. El formulario HTML también incorpora restricciones de entrada.

## 9. Pruebas funcionales

Las pruebas se realizan partiendo de una sesión nueva, con saldo inicial de S/ 250.00.

| N.º | Prueba | Resultado esperado |
|---|---|---|
| 1 | Abrir la aplicación | Saldo S/ 250.00 e historial vacío |
| 2 | Recargar S/ 100.00 | Saldo S/ 350.00 y recarga registrada |
| 3 | Gastar S/ 50.00 | Saldo S/ 300.00 y gasto registrado antes que la recarga |
| 4 | Intentar gastar S/ 400.00 | Mensaje de saldo insuficiente; saldo S/ 300.00 |
| 5 | Ingresar cero, negativo o monto vacío | Operación rechazada; saldo e historial sin cambios |

También pueden probarse montos no numéricos y con más de dos decimales.

## 10. Gestión de sesión HTTP

El saldo y el historial se almacenan mediante `HttpSession`.

El Servlet utiliza los atributos:

- `saldo`: importe disponible representado mediante `BigDecimal`.
- `movimientos`: lista de objetos `Movimiento`.

Los datos se conservan durante la sesión HTTP activa y no se almacenan permanentemente.

## 11. Publicación en GitHub

Crear un repositorio para el Caso 1 y subir el código fuente, el archivo `pom.xml` y el README.

Desde la carpeta del proyecto:

```bash
git init
git add .
git commit -m "Entrega T1 - Caso 1 Billetera Digital"
git branch -M main
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main
```

Reemplazar `URL_DEL_REPOSITORIO` por la dirección real del repositorio de GitHub.

El archivo WAR generado debe entregarse adicionalmente, según las instrucciones de la evaluación.

## 12. Limitaciones

- No utiliza base de datos.
- No implementa autenticación.
- No consume APIs externas.
- No utiliza Spring ni otros frameworks de aplicación.
- Los datos se conservan únicamente durante la sesión HTTP.
- Se utiliza Bootstrap para la presentación.

## 13. Conclusión

La aplicación implementa las operaciones básicas de una billetera digital mediante Java, Servlets, JSP y JSTL.

Se utiliza el patrón MVC para separar la lógica de negocio, la representación de movimientos y la interfaz de usuario.

El proyecto permite aplicar los fundamentos del desarrollo web del lado del servidor, la gestión de sesiones HTTP, el procesamiento de formularios y la validación de operaciones financieras simuladas.
