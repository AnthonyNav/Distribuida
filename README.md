# Distribuida – guia de uso

Monorepo con tres clientes y un backend PHP que comparten la misma base de datos `distribuida`.

- `ServidorPHP/`: API CRUD sobre la tabla `users` (MySQL/MariaDB).
- `CRUD_Bootstrap/`: cliente web con Bootstrap y jQuery que consume la API.
- `Movil-App/`: app Android (Java + Volley) que llama la misma API.
- `WindowsForms/`: carpeta sin codigo aun.

## Pasos rapidos
1) Importa la base de datos de ejemplo.  
2) Arranca el servidor PHP con `php -S 0.0.0.0:8000 index.php` para que escuche en tu IP.  
3) Ajusta los clientes para que apunten a ese endpoint.

## Base de datos en XAMPP
1) Inicia Apache y MySQL desde XAMPP.  
2) Abre `http://localhost/phpmyadmin`, crea la BD `distribuida` (collation utf8mb4).  
3) En la pestaña Importar, carga `ServidorPHP/distribuida.sql`.  
   - Crea la tabla `users` con campos: `id`, `nombre`, `apellidos`, `email`, `telefono`, `clave`.  
   - Inserta datos de ejemplo listos para probar.

## Servidor PHP (`ServidorPHP/index.php`)
- Requiere PHP con extensiones mysqli y acceso a la BD `distribuida`.
- CORS abierto: `Access-Control-Allow-Origin: *` ya esta habilitado.
- Como arrancarlo:
  - Opcion XAMPP: copia `ServidorPHP` dentro de `htdocs` y accede a `http://localhost/ServidorPHP/index.php?tipo=1`.
  - Opcion CLI: desde `ServidorPHP/` ejecuta `php -S 0.0.0.0:8000 index.php` para usar el mismo puerto y habilitar acceso desde otras maquinas (app movil y Windows Forms).
- Endpoints (metodo GET, parametro `tipo`):
  - `tipo=1` → lista todos los usuarios.
  - `tipo=2` → crea usuario. Requiere `nombre`, `apellidos`, `telefono`, `email` (o `gmail`), `clave`.
  - `tipo=3` → modifica. Requiere `id`, `nombre`, `apellidos`, `telefono`, `email`/`gmail`, `clave`.
  - `tipo=4` → elimina. Requiere `id`.
  - `tipo=5` → consulta por `id`.
- Prueba rapida desde la consola:  
  `curl "http://localhost:8000/index.php?tipo=1"`

## Cliente web (`CRUD_Bootstrap/index.php`)
- Agenda CRUD en Bootstrap con modales para alta, edicion y borrado.
- La URL del API esta en la constante `API_URL` al final del archivo; por defecto `http://localhost:8000/index.php`.
- Para ejecutarlo:
  - Sirvelo desde un servidor estatico escuchando en todas las interfaces:  
    ```bash
    cd CRUD_Bootstrap
    php -S 0.0.0.0:8081
    ```  
    (para entrar desde otra maquina usa `http://<IP-de-tu-PC>:8081`).  
  - O colocalo en `htdocs/CRUD_Bootstrap`.
  - Abre `http://127.0.0.1:8081` o `http://<IP-de-tu-PC>:8081` (segun donde lo sirvas) y opera la tabla.

## App Android (`Movil-App/`)
- Codigo Java con Volley, activities para crear, mostrar (RecyclerView), modificar y eliminar registros.
- La URL del backend se configura en `Movil-App/app/src/main/java/com/example/myapplication/Config.java` en `URL_API`.
  - Emulador: usa `http://10.0.2.2:8000/index.php`.
  - Dispositivo fisico: usa `http://<IP-de-tu-PC>:8000/index.php` con ambos en la misma red.
- Como ejecutar:
  1) Abre la carpeta `Movil-App` en Android Studio y sincroniza Gradle.
  2) Ajusta `URL_API` al endpoint real.
  3) Compila y corre en emulador o dispositivo.

## Conexion entre proyectos
- Todos los clientes llaman al mismo endpoint PHP. Asegura que `API_URL` (web) y `URL_API` (Android) apunten al dominio/puerto donde correra `ServidorPHP/index.php`.
- El servidor usa la BD `distribuida`, asi que con solo importar `distribuida.sql` ya tienen datos iniciales que veras tanto en la web como en la app movil.

## Estado de WindowsForms
- La carpeta `WindowsForms/` esta vacia en este repositorio; agregala aqui cuando tengas el proyecto.
