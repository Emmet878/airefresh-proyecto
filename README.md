# Airefresh

Tienda en línea de aires acondicionados (Spring Boot 3.5, Java 21, MySQL, Thymeleaf).
Catálogo con filtros, carrito, pedidos, inicio de sesión y panel de administración.

## Cómo ejecutarlo

Necesitas Java 21 y un MySQL en marcha. Luego:

```
./mvnw spring-boot:run
```

La app queda en <http://localhost:8081>. Las tablas se crean solas (`ddl-auto=update`).

## Variables de entorno

| Variable | Para qué sirve | Valor por defecto |
|---|---|---|
| `DB_URL` | URL de MySQL | `jdbc:mysql://localhost:3306/airefresh?...` |
| `DB_USERNAME` | Usuario de la base de datos | `airefresh` |
| `DB_PASSWORD` | Contraseña de la base de datos | `1234` (cámbiala en producción) |
| `AIREFRESH_ADMIN_EMAIL` | Correo del administrador inicial | — |
| `AIREFRESH_ADMIN_PASSWORD` | Contraseña del administrador (mínimo 12 caracteres) | — |
| `AIREFRESH_DATOS_TRANSFERENCIA` | Texto con los datos bancarios que ve el cliente al pagar por transferencia | mensaje genérico |
| `AIREFRESH_FOTOS` | Carpeta donde se guardan las fotos | `uploads/equipos` |
| `PORT` | Puerto del servidor | `8081` |
| `SHOW_SQL` | Mostrar el SQL en consola | `false` |

**Sin `AIREFRESH_ADMIN_EMAIL` y `AIREFRESH_ADMIN_PASSWORD` no se crea ningún administrador**
y no podrás entrar al panel (`/admin`). El administrador se crea una sola vez, al arrancar.

## Estructura

```
src/main/java/com/example/airefresh
├── config/        SecurityConfig, AdminBootstrap
├── controlador/
│   ├── web/       páginas HTML: Inicio, Cuenta, Carrito, Pedido, Admin, Equipo
│   │              y ManejadorErroresWeb
│   ├── api/       API REST (/api/equipos) y ManejadorErroresApi
│   └── dto/       CarritoDTO, LineaCarritoDTO
├── modelo/        entidades JPA: Equipo, Usuario, Pedido, PedidoItem
├── repositorio/   Spring Data JPA
└── servicio/      interfaces + implementaciones (...Jpa, StorageServiceDisco)
                   y excepciones propias (NoEncontradoException, ReglaNegocioException)

src/main/resources
├── templates/     fragmentos/ tienda/ cuenta/ pedidos/ admin/ equipos/  (error.html en la raíz)
└── static/        css/  js/  img/
```

`error.html` se queda en la raíz de `templates/` porque es donde Spring Boot lo busca por defecto.

## Rutas principales

| Ruta | Quién |
|---|---|
| `/`, `/productos`, `/carrito` | Todos |
| `/login`, `/registro` | Todos |
| `/checkout`, `/pedidos` | Usuario con sesión |
| `/admin`, `/equipos` | Administrador |
| `GET /api/equipos` | Todos |
| `POST/PUT/DELETE /api/equipos` | Administrador |
