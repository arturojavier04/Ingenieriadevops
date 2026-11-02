# Proyecto Microservicio - Asignatura DevOps (DOY0101)

## Integrantes
* Javier Cruz
* Yerickson Rodriguez               

---

## 1. Modelo de Ramificación Seleccionado (IE1)

**Modelo elegido:** GitFlow

### Justificación Técnica:
Hemos seleccionado **GitFlow** como nuestra estrategia de ramificación por las siguientes razones clave:
* **Separación de entornos:** Aísla el código estable listo para producción (`main`) de la línea principal de integración (`develop`).
* **Trabajo colaborativo y aislado:** Permite que cada integrante desarrolle nuevas características en ramas `feature/` dedicadas sin interferir con el trabajo del otro.
* **Gestión de emergencias:** Ofrece un mecanismo claro mediante ramas `hotfix/` para solucionar errores críticos en producción rápidamente sin pausar el desarrollo en curso.

---

## 2. Guía de Buenas Prácticas y Convenciones (IE5)

### Naming de Ramas
* `main`: Contiene únicamente código probado y listo para despliegue en producción.
* `develop`: Rama base para la integración continua de características.
* `feature/<nombre-descriptivo>`: Para nuevas funcionalidades (ej: `feature/autenticacion-usuario`).
* `hotfix/<nombre-descriptivo>`: Para correcciones urgentes en producción (ej: `hotfix/corregir-puerto-bd`).

### Convención de Commits (Conventional Commits)
Todos los mensajes de commit deben seguir la estructura de *Conventional Commits*:
* `feat: <descripción>` -> Nueva funcionalidad (ej: `feat: agrega endpoint de usuarios`).
* `fix: <descripción>` -> Corrección de un error (ej: `fix: corrige validación de token`).
* `docs: <descripción>` -> Cambios en documentación (ej: `docs: actualiza README`).
* `style: <descripción>` -> Formato o correcciones de estilo sin afectar funcionalidad.

### Estrategia de Revisión y Merge
1. **Uso obligatorio de Pull Requests (PR):** Ningún cambio se integra directamente a `main` o `develop`.
2. **Revisión por pares:** Todo PR debe ser revisado y aprobado por el otro integrante antes de fusionarse (*Merge*).


## Preguntas de reflexion.

En nuestro caso usamos herramientas de inteligencia artificial tales como Codex o Claude para ayudarnos con sintaxis de ci.yml o comandos específicos de git. 

Reforzamos de manera bastante profunda como utilizar la plataforma de GitHub y conocimos muchos apartados con los que no habíamos tenido la oportunidad o necesidad de trabajar anteriormente tal como lo es el Action. 

Las dificultades tecnicas vinieron principalmente del lado de los push y pull como tambien con la sintaxis de yml. Nos enfrentamos varias veces a ramas que se quedaban atras, pushes que no se hacían completos o de la forma que deseabamos. De igual manera tuvimos que migrar en una oportunidad de repositorio puesto que clonamos el repositorio dentro del repositorio (?) aun no entendemos muy bien por qué sucedió pero estos pequeños errores nos hacen tomar consciencia de qué cosas tenemos que hacer y qué cosas no y en qué orden hay que hacerlas para futuras ocasiones. El trabajo de igual forma se nos hizo bastante entretenido y didactico así que nos enfrentamos al resto de de la materia con bastantes ganas de seguir aprendiendo. 

---

# Evaluación Parcial 2: Pipeline CI/CD (DOY0101)

## 3. Descripción del pipeline

El pipeline está en `.github/workflows/ci-cd.yml` (workflow "EP2 - Pipeline CI/CD ms-producto"). Se ejecuta en cada `push` y `pull_request` hacia `main` y `develop`, y también de forma manual (`workflow_dispatch`). Usa permisos mínimos (`contents: read`).

| Job | Qué hace |
| --- | --- |
| `pruebas` | Descarga el repositorio, configura Java 21 (Temurin) con caché de Maven, ejecuta `mvn clean verify` (pruebas JUnit 5 con Mockito y base H2 en memoria con el perfil `test`) y guarda los reportes de pruebas como artefacto (`reportes-junit`). |
| `despliegue` | Solo corre si `pruebas` terminó bien y el evento no es un Pull Request. Construye la imagen Docker, genera credenciales temporales aleatorias para MySQL, levanta los contenedores con Docker Compose, verifica que la API responda HTTP 200 en `/api/productos` (hasta 30 intentos cada 3 s), muestra los logs si algo falla y siempre baja los contenedores al final. |

### Relación con los indicadores

| Indicador | Evidencia en el repositorio |
| --- | --- |
| IE1 Contenedores | `ms-producto/Dockerfile` y `.dockerignore` |
| IE2 Pruebas automatizadas | Job `pruebas` y pruebas en `ms-producto/src/test` |
| IE3 Seguridad y escalabilidad | Dependabot, usuario sin privilegios, credenciales fuera del código, límites de recursos y healthchecks |
| IE4 Despliegue y trazabilidad | Job `despliegue`, imagen etiquetada con el SHA del commit y flujo GitFlow con PR |
| IE5 Orquestación | `ms-producto/compose.yaml` |

## 4. Contenerización (IE1)

- Dockerfile multi-etapa: se compila con `maven:3.9-eclipse-temurin-21` y se ejecuta con `eclipse-temurin:21-jre`, lo que deja una imagen final más liviana.
- El contenedor corre con un usuario sin privilegios (`10001`).
- Incluye `HEALTHCHECK` que consulta `http://localhost:8083/api/productos`.
- `.dockerignore` excluye `target/`, `.git/` y archivos de entorno.

## 5. Orquestación con Docker Compose (IE5)

`ms-producto/compose.yaml` define dos servicios:

- `mysql` (imagen `mysql:8.4`): con healthcheck propio, volumen persistente `mysql_data` y reinicio automático.
- `ms-producto`: expone el puerto 8083, recibe la conexión a la base por variables de entorno y espera a que MySQL esté saludable (`depends_on` con `service_healthy`).

## 6. Seguridad y escalabilidad (IE3)

- **Dependabot** (`.github/dependabot.yml`): revisión semanal de dependencias Maven, imágenes Docker y GitHub Actions, con un máximo de 5 PRs abiertos por ecosistema.
- **Credenciales:** las contraseñas de MySQL no están escritas en el repositorio; el pipeline las genera aleatoriamente en cada ejecución y Compose las recibe como variables de entorno.
- **Mínimo privilegio:** el workflow usa solo `contents: read` y el contenedor no corre como root.
- **Recursos acotados:** `cpus` y `mem_limit` definidos por servicio en Compose (MySQL: 1 CPU y 768 MB; microservicio: 1 CPU y 512 MB).
- **Análisis de código e imagen:** el escaneo con CodeQL, Trivy y la cobertura con JaCoCo están preparados en la rama `feature/seguridad-pipeline` (PR #18), en revisión al momento de esta entrega.

## 7. Trazabilidad y calidad (IE4)

- **Del commit al despliegue:** cada imagen se construye con dos etiquetas, `ms-producto:ep2` y `ms-producto:<SHA del commit>`, y cada ejecución de Actions queda asociada a ese commit.
- **Calidad antes de desplegar:** el job `despliegue` depende de `pruebas` (`needs`), por lo que no se despliega código con pruebas fallidas.
- **Flujo de trabajo:** GitFlow, con ramas `feature/` y `fix/`, Pull Requests y revisión del otro integrante antes de fusionar.
- **Evidencia:** los reportes JUnit se guardan como artefacto en cada ejecución y el despliegue verifica la respuesta de la API antes de darlo por exitoso.

## 8. Alcance y mejoras pendientes

- La orquestación se resolvió con Docker Compose, sin Kubernetes.
- La imagen no se publica en un registro de contenedores.
- Aún no hay umbral mínimo de cobertura de pruebas.
- El workflow `ci.yml` de la EP1 es una simulación y se mantiene solo como referencia histórica.

## 9. Uso de Inteligencia Artificial

- **Herramientas usadas:** Claude (Anthropic) [agregar Gemini u otras si corresponde].
- **Cómo se aplicaron:** apoyo para revisar el estado del repositorio, preparar la estructura de los workflows y redactar la parte descriptiva de este README. El equipo revisó y validó el contenido.
- **Cita:** [agregar la referencia según https://bibliotecas.duoc.cl/ia]

## 10. Conclusiones y reflexiones individuales

### Javier Cruz y Yerickson Rodriguez
[Como equipo aprendimos la importancia de trabajar de manera colaborativa en el desarrollo y despliegue de aplicaciones, utilizando herramientas como Git, GitHub y Docker.

Comprendimos cómo organizar el trabajo mediante ramas, commits y Pull Requests, además de la importancia de implementar procesos de integración y despliegue continuo (CI/CD).

También adquirimos conocimientos sobre la creación y administración de contenedores, la automatización de pruebas, la seguridad de las aplicaciones y la optimización de recursos.

En general durante esta experiencia nos permitió comprender como poder aplicar los principios de DevOps en un proyecto real, mejorando nuestra organización, comunicación y capacidad para resolver problemas en equipo.]
