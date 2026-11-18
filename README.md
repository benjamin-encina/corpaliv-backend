# CORPALIV – Backend (microservicios Spring Boot)

> DSY1105 Desarrollo de Aplicaciones Móviles · Evaluación Parcial 4 · DuocUC

Backend de **CorpalivApp**, la app Android (Kotlin + Jetpack Compose) para visibilizar las oportunidades de
colaboración y recibir aportes de personas y empresas para CORPALIV, la Escuela Jean Van Dijk y Alba Lab.

Repositorio de la app móvil: `<URL del repositorio de CorpalivApp>`

## 1. Nombre del proyecto

**CorpalivApp** – Plataforma de Impacto Social y Vinculación Empresarial (backend de microservicios).

## 2. Integrantes

| Integrante | Responsabilidad principal |
|------------|---------------------------|
| `<Nombre Apellido>` | `<ej: auth-service y aportes-service>` |
| `<Nombre Apellido>` | `<ej: oportunidades-service y pruebas>` |

Tablero de planificación (Trello): `<URL del tablero>`

## 3. Funcionalidades

Tres microservicios independientes, cada uno con su propia base de datos:

| Microservicio | Puerto | Qué resuelve en la app |
|---------------|--------|------------------------|
| `auth-service` | 8083 | Inicio de sesión del administrador (cuenta + rol "Administrador" del perfil). Emite un token JWT. |
| `oportunidades-service` | 8081 | CRUD de las oportunidades/proyectos que muestra la pantalla Oportunidades. Lectura pública, escritura solo administrador. |
| `aportes-service` | 8082 | Registro de aportes de personas y empresas (formulario de la app) y su gestión en el Dashboard: listar, filtrar, resumen, marcar como contactado, editar y eliminar. |

**Seguridad:** `auth-service` firma un JWT (HS256) con el rol de la cuenta; los otros dos servicios lo verifican con el
mismo secreto. Son públicos solo `GET /api/oportunidades/**` y `POST /api/aportes` (el formulario lo usa cualquier
visitante). Todo lo demás exige un token de **ADMINISTRADOR**. Sin token responde `401` y con otro rol `403`.

**Validaciones** (equivalentes a las del formulario de la app): RUN/RUT solo por formato (`45678987-3` se acepta),
correo, teléfono chileno (`+569 12345678` para personas, `+56 123456789` para empresas), frecuencia obligatoria para
personas y giro, dirección, representante y su RUT obligatorios para empresas. Los errores vuelven en JSON con el
detalle por campo.

**Compatibilidad con la app:** `GET /api/oportunidades` devuelve los mismos campos que `OportunidadModel` y
`GET /api/aportes` los mismos que `RegistroGuardado`, por lo que Retrofit/Gson los lee sin cambios en los modelos.

## 4. Endpoints utilizados

### Microservicios propios

**auth-service** · `http://localhost:8083`

| Método | Ruta | Acceso | Descripción |
|--------|------|--------|-------------|
| POST | `/api/auth/login` | Público | Body `{correo, clave}` → token y datos de la sesión |
| GET | `/api/auth/me` | Token | Datos de la cuenta autenticada |

**oportunidades-service** · `http://localhost:8081`

| Método | Ruta | Acceso | Descripción |
|--------|------|--------|-------------|
| GET | `/api/oportunidades` | Público | Lista (filtro opcional `?categoria=`) |
| GET | `/api/oportunidades/{id}` | Público | Detalle |
| POST | `/api/oportunidades` | Admin | Crear (`201`) |
| PUT | `/api/oportunidades/{id}` | Admin | Actualizar |
| DELETE | `/api/oportunidades/{id}` | Admin | Eliminar (`204`) |

**aportes-service** · `http://localhost:8082`

| Método | Ruta | Acceso | Descripción |
|--------|------|--------|-------------|
| POST | `/api/aportes` | Público | Registrar aporte de persona o empresa (`201`) |
| GET | `/api/aportes` | Admin | Lista (filtro opcional `?tipo=PERSONA` o `?tipo=EMPRESA`) |
| GET | `/api/aportes/resumen` | Admin | Totales: total, personas, empresas, pendientes |
| GET | `/api/aportes/{id}` | Admin | Detalle |
| PUT | `/api/aportes/{id}` | Admin | Actualizar |
| PATCH | `/api/aportes/{id}/estado` | Admin | Body `{estado: "CONTACTADO" \| "PENDIENTE"}` |
| DELETE | `/api/aportes/{id}` | Admin | Eliminar (`204`) |

Ejemplos listos para ejecutar: [`docs/endpoints.http`](docs/endpoints.http).

### API externa (consumida por la app vía Retrofit)

`<Completar al integrarla en la app: nombre, URL base y endpoint usado, y en qué pantalla se muestra>`

## 5. Pasos para ejecutar

Requisitos: **JDK 21** y **Maven 3.9+** (o el soporte Maven de IntelliJ / VS Code).

```bash
git clone <URL-de-este-repositorio>
cd corpaliv-backend

# En tres terminales (o tres configuraciones de ejecución del IDE):
cd auth-service          && mvn spring-boot:run
cd oportunidades-service && mvn spring-boot:run
cd aportes-service       && mvn spring-boot:run
```

Por defecto cada servicio usa **H2 en archivo** (carpeta `data/`, persiste entre reinicios y no requiere instalar nada).
Al iniciar se crean las tablas, la cuenta de administrador y las dos oportunidades iniciales.

**Con MariaDB** (como en Fullstack II):

```bash
docker compose up -d            # levanta MariaDB y crea las 3 bases de datos
cd auth-service && mvn spring-boot:run -Dspring-boot.run.profiles=mariadb
# (igual en los otros dos servicios)
```

**Pruebas unitarias:** `mvn test` dentro de cada servicio.

**Cuenta de administrador de demostración:** `admin@corpaliv.cl` / `admin`. Es solo para el entorno académico:
se puede cambiar con las variables `ADMIN_CORREO`, `ADMIN_CLAVE` y `ADMIN_NOMBRE`, y el secreto del token con
`JWT_SECRET` (debe ser el mismo en los tres servicios).

### Conexión desde la app Android

| Dónde corre la app | Host a usar en Retrofit |
|--------------------|--------------------------|
| Emulador de Android Studio | `http://10.0.2.2:<puerto>/` |
| Celular físico (misma red Wi-Fi) | `http://<IP-del-PC>:<puerto>/` |

Como las URLs son `http` (sin TLS) la app necesita permitir tráfico en claro mientras se desarrolla
(`android:usesCleartextTraffic="true"` en el `AndroidManifest.xml` o un `network_security_config`).

## 6. Captura del APK firmado y .jks

`<Insertar aquí las capturas: archivo APK firmado, archivo .jks y configuración de firma en build.gradle.kts>`

## Estructura

```text
corpaliv-backend/
├── auth-service/            # JWT + cuenta de administrador
├── oportunidades-service/   # CRUD de oportunidades
├── aportes-service/         # Aportes de personas y empresas + Dashboard
├── docker/init.sql          # Crea las bases de datos en MariaDB
├── docker-compose.yml
└── docs/endpoints.http      # Pruebas de todos los endpoints
```

Cada servicio sigue las capas `controller → service → repository → model` con DTOs, manejo global de errores
(`exception/`) y seguridad (`security/`).
