# Normas Base de Desarrollo del Proyecto (`rick-morty-api`)

Este documento define las reglas de diseño, arquitectura, convenciones de código y buenas prácticas detectadas en el repositorio para que los agentes y desarrolladores mantengan la coherencia técnica del proyecto.

---

## 1. Stack Tecnológico y Runtime
* **Java 21**: Usar características modernas de Java (Virtual Threads, Pattern Matching, Stream API con `toList()`, constructores compactos o records para DTOs).
* **Quarkus 3.40.1 (Platform)**: Pila reactiva moderna (`quarkus-rest`, `quarkus-rest-client-jackson`, `quarkus-arc`).
  * **Prohibido**: No utilizar librerías ni dependencias basadas en RESTEasy Classic (`quarkus-resteasy`).
* **Base de datos & Migraciones**:
  * PostgreSQL en entornos de producción y desarrollo (`dev`).
  * H2 en memoria (`jdbc:h2:mem:test`) para el perfil de pruebas (`%test`).
  * Cualquier cambio en el modelo relacional debe incluir su script Flyway versionado en `src/main/resources/db/migration/V{version}__{description}.sql`.

---

## 2. Arquitectura y Organización en Capas (`org.ivione93`)

El proyecto sigue una arquitectura desacoplada y orientada a responsabilidades claras:

```
src/main/java/org/ivione93/
├── boundary/             # Endpoints REST y exposición HTTP (JAX-RS / Jakarta REST)
├── dto/                  # Objetos de transferencia de datos y modelos de respuesta
│   └── rickmortyapi/     # DTOs mapeados directamente de APIs externas
├── entity/               # Entidades de base de datos JPA / Panache
├── exception/            # ExceptionMappers centralizados para errores HTTP
└── services/             # Lógica de negocio y orquestación
    ├── async/            # Ejecución asíncrona no bloqueante y gestión de timeouts
    ├── converters/       # Mapeadores puros entre DTOs externos e internos
    ├── dataservices/     # Clientes REST declarativos (MicroProfile REST Client)
    └── providers/        # Proveedores con capa de caché declarativa
```

### Reglas por Capa:

### A. Capa de Exposición (`boundary`)
* Todos los recursos REST deben residir en `org.ivione93.boundary` y estar anotados con `@ApplicationScoped` y `@Path(...)`.
* Inyección de dependencias exclusivamente mediante `@Inject`.
* Si un endpoint realiza operaciones bloqueantes o I/O intensivo, utilizar la anotación `@RunOnVirtualThread` (soporte nativo de Java 21 en Quarkus).
* No implementar lógica de negocio en el resource; delegar siempre en la capa `services`.

### B. Capa de Negocio (`services`)
* Anotar los servicios con `@ApplicationScoped`.
* Orquestar la obtención de datos llamando a los servicios asíncronos (`services.async`), aplicar lógica y convertir mediante `services.converters`.
* Lanzar excepciones claras capturables por el `ExceptionHandler`.

### C. Servicios Asíncronos (`services.async`)
* Heredar de `AsyncCallService` para reutilizar el `ManagedExecutor` inyectado (MicroProfile Context Propagation) y el timeout configurado (`api.timeout.milliseconds`).
* Encapsular llamadas externas en `CompletableFuture` aplicando `.orTimeout(...)` y manejando fallos con `.exceptionally(...)` y `toCompletionException(...)`.

### D. Capa de Integración Externa (`services.dataservices` y `services.providers`)
* **Interfaces HTTP**: Definir en `dataservices` interfaces anotadas con `@RegisterRestClient(configKey = "...")` y `@RegisterForReflection`.
* **Configuración**: La URL base y timeouts deben definirse en `application.properties` usando el `configKey` (ej. `quarkus.rest-client.rickmorty.url`).
* **Caché**: Utilizar `services.providers` con `@CacheResult(cacheName = "...")` y métodos de invalidación con `@CacheInvalidateAll`. Inyectar el cliente con `@RestClient`.

### E. Capa de Persistencia (`entity`)
* Utilizar **Hibernate ORM with Panache** con patrón Active Record.
* Si el identificador no es autogenerado secuencialmente por JPA (como los IDs provenientes de la API externa), extender de `PanacheEntityBase` y declarar explícitamente `@Id public Integer id;`.
* Utilizar campos públicos sin métodos *getters/setters* redundantes (Panache realiza bytecode rewriting en tiempo de compilación).
* **Transacciones**: Las operaciones de escritura (`persist()`, `delete()`) deben ejecutarse en métodos de servicio anotados con `@Transactional` (ej. en `CharactersService`), nunca en métodos de las entidades.
* Para desacoplar eventos tras transacciones, usar eventos CDI: `characterEvent.fire(...)` y observadores `@Observes(during = TransactionPhase.AFTER_SUCCESS)`.

### F. Mapeadores (`services.converters`)
* Clases `@ApplicationScoped` dedicadas exclusivamente a la transformación inmutable de DTOs.
* Evitar conversiones manuales en controladores o entidades. Manejar valores nulos con valores seguros por defecto (ej. `"unknown"`).

---

## 3. Logging y Trazabilidad
* Usar siempre el logger estático de Quarkus: `io.quarkus.logging.Log`.
* Métodos preferidos: `Log.info(...)`, `Log.infof(...)`, `Log.debugf(...)`, `Log.errorf(...)`.
* **Prohibido**:
  * No usar `System.out.println` ni `System.err.println`.
  * No instanciar loggers manualmente (`LoggerFactory.getLogger(...)`).

---

## 4. Normas de Testing
* **Ubicación**: Espejo en `src/test/java/org/ivione93/...`.
* **Tests de Endpoints / Integración**:
  * Anotar con `@QuarkusTest`.
  * Utilizar `io.restassured.RestAssured` con sintaxis BDD (`given().when().then()`).
  * Inyectar mocks o espías mediante `@InjectMock` o `@InjectSpy` sobre beans CDI o interfaces `@RestClient`.
* **Aislamiento de Persistencia**:
  * Preferir `@TestTransaction` para pruebas que modifiquen datos, garantizando rollback automático sin requerir limpiezas manuales (`deleteAll()`).
* **Tests Unitarios Puros**:
  * Para clases sin dependencias del contenedor (ej. `RickMortyConverter`), crear tests unitarios estándar con JUnit 5 sin `@QuarkusTest` para ejecución ultrarrápida.

---

## 5. Convenciones de Configuración (`application.properties`)
* Agrupar configuraciones por secciones identificadas con comentarios (`# quarkus config`, `# flyway`, `# logging config`, `# rest client`).
* Utilizar perfiles estándar de Quarkus:
  * `%dev`: Logs detallados (`DEBUG`), Dev UI activada, consola coloreada.
  * `%test`: Base de datos en memoria H2, logs concisos (`INFO`), `devservices.enabled=false`.
* Todas las llamadas externas deben tener un timeout explícito definido.
