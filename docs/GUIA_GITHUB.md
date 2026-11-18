# Guía: subir el backend a GitHub (y cumplir la rúbrica de GitHub y Trello)

La rúbrica (IE 3.3.2) pide **commits por integrante**, planificación en Trello y un repositorio **público con acceso
al docente**. Además, si se detectan cambios en el repositorio después de la fecha límite, la nota es 1.0 grupal e
individual: deja todo subido antes.

## 1. Crear el repositorio vacío

1. GitHub → **New repository** → nombre `corpaliv-backend`.
2. Visibilidad **Public**.
3. **No** marques "Add a README", ".gitignore" ni licencia (el proyecto ya los trae).
4. Crear. Copia la URL (`https://github.com/<usuario>/corpaliv-backend.git`).

## 2. Dar acceso al compañero/a y al docente

Settings → **Collaborators** → *Add people*: tu pareja y el docente (por su usuario o correo de GitHub).

## 3. Primer push (lo hace una sola persona)

```bash
# 1. Descomprime corpaliv-backend.zip y entra a la carpeta
cd corpaliv-backend

# 2. Verifica que Git te identifica con TU correo de GitHub (si no, los commits no aparecen a tu nombre)
git config --global user.name  "Tu Nombre"
git config --global user.email "tu-correo-de-github@ejemplo.com"

# 3. Inicializa y sube
git init -b main
git add .
git status                     # revisa que NO aparezcan target/ ni data/ (los ignora .gitignore)
git commit -m "chore: estructura inicial de los microservicios"
git remote add origin https://github.com/<usuario>/corpaliv-backend.git
git push -u origin main
```

Si GitHub pide contraseña, usa un **Personal Access Token** (Settings → Developer settings → Tokens) o inicia sesión
con `gh auth login`.

## 4. Trabajo en equipo con commits de ambos

Reparte los microservicios y que **cada uno haga sus propios commits** (no subas todo desde un solo computador):

| Integrante | Rama sugerida |
|------------|---------------|
| A | `feature/auth-service`, `feature/aportes-service` |
| B | `feature/oportunidades-service`, `feature/pruebas-unitarias` |

El otro integrante:

```bash
git clone https://github.com/<usuario>/corpaliv-backend.git
cd corpaliv-backend
git checkout -b feature/oportunidades-service
# ...trabaja: ajustes, pruebas, validaciones, README...
git add .
git commit -m "feat(oportunidades): filtro por categoría y validaciones"
git push -u origin feature/oportunidades-service
```

Luego abre un **Pull Request** hacia `main` en GitHub, que lo revise la otra persona y haz *Merge*. Para traer los
cambios del otro: `git checkout main && git pull`.

Mensajes de commit técnicos y descriptivos (los evalúa la rúbrica): `feat(aportes): ...`, `fix(auth): ...`,
`test(oportunidades): ...`, `docs: ...`.

Revisa la contribución de cada uno con `git shortlog -sn` o en GitHub → **Insights → Contributors**.

## 5. Trello (evidencia de planificación distribuida)

Crea un tablero `CorpalivApp – Parcial 4` con las listas **Backlog · En progreso · En revisión · Hecho** y tarjetas
asignadas a cada integrante, por ejemplo:

- Microservicio de autenticación (JWT) · A
- Microservicio de oportunidades (CRUD) · B
- Microservicio de aportes (CRUD + Dashboard) · A
- Integración Retrofit con los microservicios (app) · B
- Consumo de API externa en la app · A
- Pruebas unitarias ViewModel/Repository (≥ 80 %) · A y B
- APK firmado y llave .jks · B
- README y capturas · A

Mueve las tarjetas mientras avanzan, agrega checklists y deja capturas del tablero para el README. Pega la URL del
tablero en el README (hazlo visible para el docente).

## 6. Repositorio de la app (APK y .jks)

La rúbrica pide el APK firmado y la llave en el repositorio. Sube eso al repositorio de **CorpalivApp** (o a una
carpeta `release/` del que decidan). Usa un `.jks` creado solo para este trabajo, no el de otra app tuya, porque en un
repositorio público cualquiera puede descargarlo.

## 7. Antes de entregar

- [ ] README completo (integrantes, URL de Trello, API externa y capturas del APK/.jks).
- [ ] Ambos integrantes tienen commits visibles en el repositorio de la app y en el del backend.
- [ ] Docente agregado como colaborador.
- [ ] `mvn test` pasa en los tres servicios.
- [ ] Opcional: etiqueta de la entrega → `git tag v1.0-parcial4 && git push origin v1.0-parcial4`.
- [ ] Subir el código al enlace de AVA grupal (un solo integrante) y activar el enlace individual de cada uno.
- [ ] **No hacer cambios al repositorio después de la fecha límite.**
