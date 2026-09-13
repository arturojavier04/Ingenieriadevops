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