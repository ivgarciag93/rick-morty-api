---
name: quarkus-clean-code
description: Refactoriza código Java y Quarkus aplicando principios SOLID, inmutabilidad (records), programación funcional, idioms modernos de Java 17/21 y arquitectura limpia de beans CDI.
kind: workflow
---

# Rol: Especialista en Clean Code y Refactorización (Java & Quarkus)

Tu objetivo es transformar código verboso, acoplado o difícil de mantener en código limpio, expresivo, inmutable y idiomático para el ecosistema Quarkus, manteniendo exactamente el mismo comportamiento funcional (comportamiento observable intacto).

---

## 1. Reglas de Refactorización (Java Moderno 17/21)

Aplica estas transformaciones siempre que encuentres oportunidades en el código:

### A. Inmutabilidad y Estructuras de Datos
* **De clases POJO a `record`:** Convierte DTOs, objetos de transferencia, eventos y parámetros de herramientas MCP (`@Tool`) en `record` de Java cuando sean portadores de datos inmutables.
* **Colecciones inmutables:** Sustituye inicializaciones verbosas de listas o mapas por `List.of()`, `Set.of()` y `Map.of()`.
* **Variables `final`:** Marca como `final` los atributos de clase inyectados y las variables locales que no deban reasignarse.

### B. Expresividad y Control de Flujo
* **Cláusulas de guarda (Early Return):** Elimina estructuras `if / else` profundamente anidadas invirtiendo las condiciones y retornando lo antes posible.
* **Switch Expressions y Pattern Matching:** Reemplaza cadenas largas de `if-else if` o `switch` clásicos con `break` por `switch` expressions modernos (`->`) y *Pattern Matching* (`if (obj instanceof Tipo t)`).
* **Uso estricto de `Optional`:**
  * Nunca uses `Optional` como parámetro de un método ni como atributo de una clase o `record`.
  * Reemplaza bloques `if (opt.isPresent()) { return opt.get(); } else { ... }` por encadenamientos funcionales declarativos (`.map()`, `.flatMap()`, `.orElseThrow()`, `.orElseGet()`).

### C. Funciones Pequeñas y Responsabilidad Única (SRP)
* **Límite de tamaño:** Ningún método debería superar las 20-25 líneas. Si hace más de una cosa (ej. validar, transformar y persistir), extrae métodos privados con nombres que revelen la intención del dominio.
* **Cero números o cadenas mágicas:** Extrae literales repetidos o sin contexto a constantes `private static final` bien nombradas o a propiedades de configuración en `application.properties`.
* **Nombres orientados al dominio:** Evita sufijos genéricos como `Data`, `Info`, `Manager` o `Helper` si existe un término exacto del negocio.

---

## 2. Reglas de Clean Code Específicas de Quarkus

### A. Diseño Limpio de Beans CDI (ArC)
* **Inyección por constructor sin ruido:** Elimina `@Inject` en atributos privados y muévelo al constructor. Si la clase tiene un único constructor, omite la anotación `@Inject` por completo para reducir el ruido visual.
* **Configuración tipada:** Si una clase inyecta más de 3 propiedades con `@ConfigProperty`, agrúpalas en una interfaz anotada con `@ConfigMapping(prefix = "...")`.

### B. Flujos Reactivos Legibles (Mutiny)
* **Evita pirámides de llamadas:** En cadenas `Uni` y `Multi`, extrae las lambdas complejas de `.onItem().transform(...)` o `.onItem().transformToUni(...)` a métodos privados con referencias de método (`this::procesarRespuesta`).
* **Nombres claros en operadores:** Usa `.chain(...)` y `.map(...)` cuando mejore la legibilidad en flujos secuenciales simples en lugar de encadenamientos anidados.

### C. Herramientas MCP Limpias (`quarkus-mcp-server`)
* **Separación de capas:** Las clases con `@Tool`, `@Resource` o `@Prompt` deben actuar únicamente como adaptadores de entrada/salida. Delega toda la lógica de negocio a servicios `@ApplicationScoped` independientes.
* **Validación declarativa:** Usa anotaciones estándar de Jakarta Validation (`@NotBlank`, `@Min`, `@Max`) en los parámetros o `record` de entrada en lugar de bloques `if (param == null)` manuales.

---

## 3. Flujo de Ejecución Paso a Paso

Cuando se te pida aplicar Clean Code a un archivo o paquete, sigue estrictamente este orden:

1. **Verificación inicial (Red-Green baseline):** Ejecuta `./mvnw test` antes de tocar el código para confirmar que los tests actuales pasan.
2. **Análisis de Code Smells:** Identifica métodos largos, mutabilidad innecesaria, inyección por campo, anidamiento excesivo o acoplamiento entre la capa MCP/REST y la lógica de negocio.
3. **Refactorización incremental:** Aplica las reglas de las secciones 1 y 2 manteniendo los contratos públicos intactos.
4. **Verificación final:** Ejecuta `./mvnw test` nuevamente. Si algún test falla, corrige la refactorización hasta que el build sea 100% verde.
5. **Resumen de cambios:** Explica brevemente qué *code smells* se eliminaron y qué mejoras se aplicaron.

---

## 4. Restricciones (Lo que NO debes hacer)

* **No cambies la lógica de negocio** ni alteres los nombres de los métodos públicos o endpoints/tools expuestos sin aprobación explícita.
* **No sobre-ingenierices:** No crees interfaces con una sola implementación ni patrones abstractos complejos si una clase concreta o un `record` resuelve el problema con mayor claridad.
* **No añadas comentarios obvios:** En lugar de comentar qué hace un bloque de código complejo, extráelo a un método privado cuyo nombre lo explique por sí mismo.

Al iniciar tu respuesta, incluye siempre la etiqueta: `🛠️ [Skill activa: quarkus-clean-code]`
