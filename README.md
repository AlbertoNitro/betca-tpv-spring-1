# Tecnología Spring: Proyecto TPV - Back-end
#### Back-end con Tecnologías de Código Abierto (SPRING)
#### [Máster en Ingeniería Web por la U.P.M.](http://miw.etsisi.upm.es)
[![Build Status](https://travis-ci.org/miw-upm/betca-tpv-spring.svg?branch=develop)](https://travis-ci.org/miw-upm/betca-tpv-spring)
![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=es.upm.miw%3Abetca-tpv-spring&metric=alert_status)
> Proyecto Back-end completo para el uso de la tecnología Spring.  
> El Front-end se desarrolla en Angular en el proyecto [betca-tpv-angular](https://github.com/miw-upm/betca-tpv-angular).  


## Tecnologías necesarias
`Java` `Maven` `Spring` `Mongodb`
### Clonar el proyecto
 Clonar el repositorio en tu equipo, **mediante consola**:
```sh
> cd <folder path>
> git clone https://github.com/miw-upm/betca-tpv-spring
```
Importar el proyecto mediante **IntelliJ IDEA**
1. **Import Project**, y seleccionar la carpeta del proyecto
1. Marcar **Create Project from external model**, elegir **Maven**
1. **Next** … **Finish**


## Ecosistema
`Git` `GitHub` `Travis-CI` `Sonarclud` `Heroku` `mLab`
> Se utilizará un flujo de trabajo ramificado (_**Git Workflow**_).
> Una **historia** por alumno, organizada como un **proyecto** de tipo **Automated kanban**.
> Cada **historia** se dividirá en **tareas**, cada **tarea** será una **issue#** que será el nombre de la **rama**.  
> **Se recomienda aportaciones frecuentes a la rama `develop`** :sweat_smile:

### Metodología de trabajo
:one: Organización de la **historia** y **tareas** en el proyecto de GitHub mediante **notas**. Elegir la **nota** a implementar, convertirla en **issue#** y configurarla  
:two: Mirar el estado del proyecto [![Build Status](https://travis-ci.org/miw-upm/betca-tpv-spring.svg?branch=develop)](https://travis-ci.org/miw-upm/betca-tpv-spring) en [Travis-CI](https://travis-ci.org/miw-upm/betca-tpv-spring/builds)  
:three: Sincronizarse con las ramas remotas, 
```sh
> git fetch --all
```
Y si fuera necesario, actualizar la rama **develop** con la remota **origin/develop**:
```sh
> git checkout develop
> git pull origin develop
```
:four: Si se comienza la tarea, se crea la rama y se activa
```sh
> git checkout -b issue#xx
```
 Y si se continúa, y se necesitara actualizar la rama:
```sh
> git checkout issue#xx
> git merge -m "Merge develop into issue #xx" develop
```
:five: Programar la tarea o una parte de ella, lanzar **TODOS LOS TESTS** y asegurarse que no hay errores. Finalmente, sincronizarse con las ramas remotas:
 ```sh
> git fetch --all
```
Y si necesitamos actualizarnos, se repite el paso :four:  
:six: Actualizar **develop** con nuestro cambios:
```sh
> git checkout develop
> git merge --no-ff -m "Merge issue #xx into develop" issue#xx
```
:seven: Resolver los conflictos, observar el flujo de ramas, y si todo ha ido bien... subirlo 
```sh
> git push --all
 ```
:eight: Si la tarea continua, volver a activar la **rama issue#xx**:
```sh
> git checkout issue#xx
 ```

 ### Travis-CI
Integración continua con **Travis-CI**. Se despliega para pruebas con el servicio de BD de mongodb y ejecución de los test Unitarios y de Integración
```yaml
services:
  - mongodb
script:
- mvn org.jacoco:jacoco-maven-plugin:prepare-agent verify  #Test en el perfil "dev" y con covertura
```

### Sonarcloud
En el la cuenta de **Sonarcloud**, en la página `-> My Account -> Security`, se ha generado una **API Key**.   
En la cuenta de **Travis-CI**, dentro del proyecto, en `-> More options -> Settings`, se ha creado una variable de entorno llamada `SONAR` cuyo contenido es la **API key** de **Sonar**.    
Se ha incorporado al fichero de `.travis.yml` el siguiente código:
```yml
# Sonarcloud
- mvn sonar:sonar -Dsonar.organization=miw-upm-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$SONAR
```

### Swagger
Se monta un cliente swagger para atacar al API: http://localhost:8080/api/v0/swagger-ui.html.
Para ello, se ha introducido una fichero de configuración [SwaggerConfig](https://github.com/miw-upm/betca-tpv-spring/blob/master/src/main/java/es/upm/miw/config/SwaggerConfig.java)
```
@Configuration @EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
		...
    }
```

### Heroku & mLab
Se realiza un despliegue en **Heroku** con las bases de datos de MongoDB en **mLab**.  
En la cuenta de **Heroku**, en la página `-> Account settings -> API Key`, se ha obtenido la `API KEY`.  
En la cuenta de **Travis-CI**, dentro del proyecto, en `-> More options -> Settings`, se ha creado una variable de entorno llamada `HEROKU` cuyo contenido es la **API key** de **Heroku**.  
Se incorpora el siguiente código en el fichero `.travis.yml`
```yaml
# Deploy https://betca-tpv-spring.herokuapp.com/api/v0/swagger-ui.html
deploy:
  provider: heroku
  api_key:
    secure: $HEROKU
  on:
    branch: master
```
La conexión entre **Heroku** y **mLab** se realiza automáticamente al añadir el **Add-ons**

## Arquitectura
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/tpv-architecture.png)

### Responsabilidades
* `config` Clases de configuración de **Spring**
* `exceptions`tratamiento de errores, convierte las excepciones lanzadas en respuestas de error HTTP
* `rest_controllers` Clases que conforman el **API**
   * Define el path del recurso
   * Deben organizar la recepción de datos de la petición
   * Delega en los **dtos** la validación de campos
   * Delega en **exceptions** las respuestas de errores **HTTP**
   * Delega en los **bussines_controllers** la ejecución de la petición
* `bussines_controllers` Clases que procesan la petición
   * Desarrollan el proceso que conlleva la ejecución de la petición
   * Construye los **documents** a partir de los **dtos** de entrada
   * Delega en los **dtos** la construcción de los **dtos** de respuesta a partir de los **documents**
   * Delega en los **repositories** el acceso básico a las BD
   * Delega en los **data_services** procesos de acceso avanzado a las BD
   * Delega en los **business_services** procesos genéricos avanzados de la capa de negocio
* `busines_services` Clases de servicios de apoyo, como fachada de construcción de PDF, fachada de tratamiento de JWT, encriptación...
* `data_services` Clases de servicios avanzados de BD
* `repositories` Clases de acceso a BD mediante el patrón DAO
   * Operaciones CRUD sobre BD
   * Consultas a BD
* `documents` Clases con los documentos persistentes en BD y utilidades

## Autenticación
Se plantean mediante **Basic Auth** para logearse y obtener un **API Key** o **token** de tipo **JSON Web Tokens (JWT)**. Uso del **Bearer APIkEY** para el acceso a los recursos.  
Para obtener el **API Key** se accede al recurso: `POST \users\token`, enviando por **Basic auth** las credenciales, van en la cabecera de la petición
Para el acceso a los recursos, se envia el **token** mediante **Bearer auth**, tambien en la cabecera de la petición
> Authorization = Basic "user:pass"<sub>Base64</sub>  
> Authorization = Bearer "token"<sub>Base64</sub>  

Para asegurar los recursos, se plantea una filosofía distribuida, es decir, se establece sobre cada recursos (clase). Para ello, se anotará sobre cada clase los roles permitidos; modificando el rol sobre el método si éste, tuviese un rol diferente.  
```java
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class Clazz {
    //...
    @PreAuthorize("hasRole('ADMIN')")
    public void method(){...}
    //...
    public void method2(){...}
}
```
Existe un rol especial que se obtiene cuando se envía el usuario y contraseña por **Basic Auth** y es el rol de **authenticated**, sólo se utiliza para logearse

![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/authentication.png)

## Tratamiento de errores
Se realiza un tratamiento de error centralizado.  
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/exceptions.png)

## API. Descripción genérica
[Heroku deploy](https://betca-tpv-spring.herokuapp.com/api/v0/swagger-ui.html)

![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/api.png)

## DTOs
Son los objetos de transferencia del API, para recibir los datos (input) y enviar las respuestas (output).

* Los **input** se encargan de las validaciones de entrada mediante las anotaciones  
* Los **output**. Se deben poder construir a partir de los **documentos**. Añadir la anotación `@JsonInclude(Include.NON_NULL)` para que no se devuelvan null en el Json

![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/dtos.png)   

## Bases de datos
> Se dispone de un servicio para poblar la BD a partir de un fichero YML `db.yml`; se carga automáticamente al iniciar la aplicación en el perfil **dev**.  
> Existe el recurso `/admins/db` para poder borrar o poblar la BD a partir de un fichero yaml subido.  
> El servicio `DatabaseSeederService` nos permiter recargar el fichero `db.yml`.  
> Se debe intentar no abusar de la recarga del **yaml**, ya que ralentiza la ejecución de los tests

:exclamation: **Si se crea un nuevo _documento_, se debe añadir el `deleteAll()` asociado al nuevo documento, dentro del método `deleteAllAndInitialize` de la clase `DatabaseSeederService`**

Los pasos a seguir para incluir un nuevo documento en la carga de datos a través del fichero `db.yml`:
1. Rellenar el YML con los datos del nuevo documento  
1. Incluir en la clase `TpvGraph`, la **lista** del nuevo documento con **getters & setters**  
1. Incluir en la clase `DatabaseSeederService`, en el médoto `seedDatabase`, el `saveAll` del repositorio del nuevo documento

![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/database-seeder.png)

Fichero ** \*.yml** como ejemplo...
```yaml
articleList:
  - &ar1
    code: 8400000000017
    reference: ref-a1
    description: descrip-a1
    retailPrice: 20
    stock: 10
    tax: GENERAL
    discontinued: false
    provider: *pr1
#...

tagList:
  - description: tag-1
    articleList:
      - *ar1
      - *ar2
      - *ar3
  - description: tag-2
    articleList:
      - *ar1
      - *ar2
      - *ar5
```

## Persistencia del TPV
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/documents.png)
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/repositories.png)

## Generación de Pdf's
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/pdf.png)

## Perfiles
![](https://github.com/miw-upm/betca-tpv-spring/blob/develop/docs/profiles.png)  
Si una propiedad se define en diferentes ficheros, predomina la definición mas específica.  
A cualquier clase se le puede poner la anotación `@Profile()`, con ello indicamos que sólo se configura en el perfil marcado.  
En el TPV, los **test** siempre se ejecutan en el perfil `dev`, y los `ApiLog` también el el perfil `dev`.  
Por defecto el perfil es `dev`, pero se puede indicar como una propiedad en **application.properties**: `spring.profiles.active=dev`.  
Sólamente en la rama `release-xx` cambiaremos este valor a `prod`
Para ejecutar en un perfil determinado localmente:
```sh
> mvn spring-boot:run
> mvn -Dspring.profiles.active=dev spring-boot:run

> mvn -Dspring.profiles.active=prod spring-boot:run
> java –jar –Dspring.profiles.active=prod release-1.0.0.jar 

``` 

# Curso 2018-19. Metodología de trabajo

## Fase 1. Preparar el IDE
> Se debe utilizar `IntelliJ` & `Web Storm`.  
> Para ÌntelliJ`, todo el código debe estar formateado mediante: `-> Code -> Reformat Code`, con los chekbox `[x]Optimize imports` y `[x]Rearrange entries` activados.  
> Para Web Storm`, todo el código debe estar formateado mediante: `-> Code -> Reformat Code`, con los chekbox `[x]Optimize imports`, `[x]Rearrange entries` y `Cleanup code` activados.  

## Fase 2. Importar los proyectos
* BACK-END (IntelliJ): https://github.com/miw-upm/betca-tpv-spring
* FROND-END (Web Storm): https://github.com/miw-upm/betca-tpv-angular

## Fase 3. Desarrollo de la práctica
1. Determinar y limitar el alcance de la práctica 
   * Elegir un enunciado
   * Añadir los **Test** de aceptación y **Notas** aclaratorias
   * Dentro de cada proyecto en GitHub (uno para Angular y otro para Spring), crear un proyecto `Automated Kanban`, con el título de la práctica  
   * En la wiki, debera haber dos enlaces a los **project-spring** y **project-angular**  desarrollados  
1. Dibujar las páginas web y los menús donde se activan 
   * Debemos visualizar las ventanas, indicando desde que otras ventanas iniciamos la acción o desde que menú. Cualquier editor es válido, incluso en papel y subiendo la foto, aquí se ha utilizado el editor de Dibujos de Google. Es una vista aproximada
   * En este punto ya podemos tener, mas o menos cerrado, el alcance de la práctica a realizar  
1. Dividir la práctica en tareas mas pequeñas, cada tarea un **issue#**. Asignaros la **issue#xx** y asociarle la etiqueta oportuna. Como referencia podríamos tomar entre 5 y 10 tareas.  
Realizar fusiones frecuentes con develop del código estable, y subirlo al remoto.  
Como ejemplo proponemos la siguiente división:  
   * **Tarea 1 (Front-End)**. Vista en Angular. Crearemos en el proyecto de Angular el HTML y los componentes necesarios para su presentación. No debe tener nada de proceso, sólo nos concentraremos en la vista
   * **Tarea 2 (Front-End)**. Incluir los servicios de Angular. El servicio no llega a realizar las peticiones al API, sino que devuelve valores predeterminados e imprime por **console.log()** las peticiones al API
   * **Tarea 3 (Front-End)**. Se realiza los servicios de Angular realizando las peticiones. Como el servido no esta realizado, daran error de API no encontrado
   * **Tarea 4 (Back-End)**. La API devuelve valores fijos, con los dtos necesarios e imprime logs con las peticiones
   * **Tarea 5 (Back-End)**. Se realizan los controladores del API con sus correspondientes Test
   * **Tarea 6 (Back-End)**. Se programa las querys necesarias, se crean los documentos necesarios con sus repositorios...
   * **Tarea 7**. Se realizan las pruebas de aceptación

## Recomendaciones transversales
* Planificar antes los cambios a realizar, y cuando se tiene claro, actualizar la rama **issue#xx** con **develop** justo antes de empezar. Realizar una **estimación temporal** y **anotarlo en la tarea**
* Cuando nos sentamos a trabajar, comprobar que la rama **issue#xx** está actualizada respecto a **develop** y hacer un **commit de inicio de tarea**
* No es recomendable dejar de trabajar sin aportar a develop las mejoras, **sin romper develop**
* Realizar aportaciones frecuentes a la rama **develop**, del código estable, aunque este a medias. **Ojo** con los ficheros muy susceptibles de colisionar, como por ejemplo **app.module.ts**, **app-routing.module.ts**, **home.component.ts**..., en este caso, modificarlos y subirlos a **develop** con rapidez.

# Curso 2018-19. Enunciado de prácticas

## 1. Article
> **Autor: xxx**   
>* Permitir la creación, lectura, modificación de artículos. Debe permitir la creación de artículos sin `code`, se asignará uno automáticamente a partir del `840000000000x` y hasta el `840000099999x` (EAN13).
**OJO** habrá que consultar la BD
>* Realizar una búsqueda de aquellos artículos que no estén totalmente definidos    

[Más información...](../../wiki/Article)

## 2. Articles Family
> **Autor: xxx**  
>* Permitir la búsqueda de productos mediante agrupaciones jerárquicas. Un artículo puede pertenecer a varias familias. Aquí se utiliza el **patrón Composite**  
>* Esta parte sólo hace consulta. Incluir en el fichero `db.yml` una población de pruebas
>* El primer nivel muestra el contenido de `root`. Si se pulsa sobre uno de tipo `ARTICLE` se añade al carro de la compra. 
Si se pulsa sobre un `SIZES` se muestra dialogo con todas las tallas con su stock que son de tipo `ARTICLE`, al elegir uno se cierra el dialog y se añade al carro de la compra. Si se pulsa sobre un `ARTICLES`, se cambia el contenido dela familia

[Más información...](../../wiki/Articles-Family)

## 3. Articles Family Creator
> **Autor: xxx**  
>* Permitir realizar una creación rápida de familias de artículos, incluyendo la creación de artículos con asignación automática de código. Ver práctica `Article`  
>* Las tallas podrían ser 0..50, o "XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL", "Especial"

[Más información...](../../wiki/Articles-Family-Creator)

## 4. Articles Family CRUD
> **Autor: xxx**  
>* Permitir realizar un CRUD de las familias

[Más información...](../../wiki/Articles-Family-CRUD)

## 5. Articles Query
> **Autor: xxx**  
>* Crear un componente avanzado para la la búsqueda de productos, mediante filtros, para localizar un producto con rapidez y pueda ser utilizado en muchos puntos de la aplicación

[Más información...](../../wiki/Articles-Query)

## 6. Articles Tracking 
> **Autor: xxx**  
>* Facilitar el seguimiento de artículos por falta de stock de los clientes. Cuando se cree un ticket con una compra no entregada, 
se habilitará la referencia del ticket para que el cliente pueda acceder al ticket vía Internet para comprobar su estado
>* Añadir el envío de un email al cliente, cuando el ticket alcance el estado de `IN_STOCK` en alguna compra, indicando el estado de lo llegado
>* Cuando todo el ticket tenga el estado de `COMMIT`, ya no se podrá consultar más
 
[Más información...](../../wiki/Articles-tracking)

## 7. Budget
> **Autor: xxx** 
>* Gestión de presupuestos. Será un carro de la compra realizando un **budge** 
>* Se genera un PDF de presupuesto
 
[Más información...](../../wiki/Budget)

## 8. Cash Movements
> **Autor: xxx** 
>* Registrar movimientos de caja: ingresos o gastos de proveedores
>* Integrado con el proceso de cierre de caja

[Más información...](../../wiki/Cash-Movements)

## 9. Invoice
> **Autor: xxx**  
>* Implementar la creación de facturas a partir de un ticket, en el proceso de creación del ticket o posteriormente. Se deberá asegurar que el usuario tiene los datos necesarios (DNI, nombre de usuario y dirección completa)  
>* Ampliar el servicios de Pdf para crear la factura
>* Se debe sincronizar con la práctica de `Ticket` ya que si un Ticket con factura existe devolución, se debe crear una segunda factura negativa del mismo `Ticket`  

[Más información...](../../wiki/Invoice)

## 10. Invoice Update
> **Autor: xxx**  
>* Permitir la búsqueda de facturas por móvil, o entre un rango de fechas
>* Permitir la retificación de facturas
>* Permitir la reimpresión de facturas

[Más información...](../../wiki/Invoice-Update)

## 11. Offers
>* **Autor: xxx**
>* Crear y consultar ofertas, sobre un conjunto de artículos. La oferta tendrá un código no predecible para su comprobación
>* Permite consultar de un artículo si se le aplica alguna oferta

[Más información...](../../wiki/Offers)

## 12. Orders 
> **Autor: xxx** 
>* Gestionar la tramitación de pedidos. Se organizarán por proveedor. 
>* Permitir crear un pedido a partir de algún pedido anterior

[Más información...](../../wiki/Orders)

## 13. Operator Manager 
> **Autor: xxx** 
>* Gestionar a los vendedores (operator & Manager)
>* Gestiona de ficha de entrada y salida por día
>* Da un informe mensual de días y horas

[Más información...](../../wiki/Operator-Manager)

## 14. Orders Entry
> **Autor: xxx**  
>* Gestionar la entrada de pedidos. Se deberá ayudar para la comprobación de entrada y la inclusión en los diferentes stocks
>* También deberá ayudar en la gestión de reservas, es decir, artículos reservados que acaban de entrar
>* Relación alta con la práctica de `Orders`

[Más información...](../../wiki/Orders-Entry)

## 15. Provider
> **Autor: xxx**  
>* Mostrar una lista de proveedores (**id-company**). Solo se mostraran los proveedores activos  
>* Implementar un componente para la busqueda rápida de proveedores  
>* Permitir la creación, lectura, modificación de proveedores. No se permite borrar, solo dejar inactivo al proveedor  

[Más información...](../../wiki/Provider)

## 16. Statistics
> **Autor: xxx**
>* Ofrecer un conjunto de estudios estadísticos, mostrando en gráficas, de diferentes estudios sobre las Bases de Datos. También se permitirá obtener los datos de forma numérica.
>* Los estudios estadísticos serán abiertos, como ejemplo, se podría estudiar las ventas de un producto a lo largo de un periodo de tiempo, la evolución de ventas en total a lo largo de un periodo...
>* Importante aplicar el patrón `Facade` para mostrar los datos

[Más información...](../../wiki/Statistics)

## 17. Stock Alarm
> **Autor: xxx**  
>* Ampliar con la creación, lectura, modificación y borrado de alarmas por stock  
>* Una alarma podría ser establecer unos valores mínimos (con nivel WARNING y CRITICAL) de un conjunto de artículos, de tal manera, 
que cuando se alcanza dicho valor, aparece en una lista de artículos críticos y se avise de alguna manera

[Más información...](../../wiki/Stock-Alarm)

## 18. Stock Manager
> **Autor: xxx**  
>* Ayuda a gestionar el stok  
>* Permite realizar busquedas variadas. Productos por debajo de un stock. El stock puede ser negativo, indicando que existen reservas del mismo
>* Productos vendidos hoy

[Más información...](../../wiki/Stock-Manager)

## 19. Stock Prediction
> **Autor: xxx**  
>* Ayuda a predecir el stock de futuro 
>* Dado un producto, te indica que stock va haber a lo largo del tiempo

[Más información...](../../wiki/Stock-Prediction)

## 20. Ticket
> **Autor: xxx**
>* Se realizará una busqueda por `id` de ticket
>* Facilitar lectura y modificación de tickets. La modificación de tickets se permitirá reducir la cantidad de compras de artículos, pudiéndolo dejar a 0,
 y además, hacer evolucionar el estado de una compra. El resto de aspectos del ticket deben ser invariantes.
>* Cuando se tenga que devolver dinero se realizará mediante un `Voucher`. Ver práctica de `Voucher`

[Más información...](../../wiki/Ticket)

## 21. Ticket Query
> **Autor: xxx**
>* Facilitar la busqueda avanzada de tickets
>* Por el móvil  
>* Por los tickets pendientes de entregar de algún artículo  
>* Por los tickets pendientes de entregar por algun artículo de una lista

[Más información...](../../wiki/Ticket-Query)

## 22. User
> **Autor: xxx**  
>* Añadir la creación, lectura, modificación de usuarios. No se pueden borrar, pero si dejarlos en modo inactivo  
>* Implementar un componente para la creación rápida y actualización de usuarios  
>* En el proceso de realizar el `check out`, permitir asociar el ticket a un usuario, realizando la creación rápida si no existe 

[Más información...](../../wiki/User)

## 23. User Advanced
> **Autor: xxx**  
>* Crear un componente de busqueda avanzada de usuarios
>* Permitir a un usuario cambiar su contraseña a traves de su perfil
>* Permitir en la página de gestion de usuarios, permitir cambiar el rol de un usuario, cumpliendo una logica de autorizaciones  
>* Un `admin` puede todo. Un `manager` puede modificar `manager`, `operator` y `customer`. Un `operator` no puede alterar roles

[Más información...](../../wiki/User-Advanced)

## 24. Voucher
> **Autor: xxx**  
>* Realizar la creación y lectura. No se debe permitir el borrado ni cambiar su contenido   
>* Permitir el consumo de un vale. Se debe asegurar previamente que el vale no ha sido ya consumido  
>* Realizar una consulta de vales pendientes generados entre dos fechas  

[Más información...](../../wiki/Voucher)

## Ampliaciones

### Gestion de cuenta para pago al final de mes

### Gestion de Ticket Regalo

### Descuentos a clientes especiales

### Gestión de IVA trimestral











