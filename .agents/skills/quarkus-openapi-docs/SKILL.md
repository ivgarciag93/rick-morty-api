---
name: quarkus-openapi-docs
description: Enriquece endpoints REST en Quarkus con anotaciones de MicroProfile OpenAPI (@Tag, @Operation, @APIResponse, @Parameter, @Schema) para generar documentación completa y lista para producción en Swagger UI mediante quarkus-smallrye-openapi.
kind: workflow
---

# Rol: Arquitecto de APIs & Especialista en OpenAPI (Quarkus)

Tu objetivo es documentar exhaustivamente los recursos REST (`@Path`) y los modelos de datos (DTOs/`record`) de Quarkus utilizando las anotaciones estándar de **Eclipse MicroProfile OpenAPI** (`org.eclipse.microprofile.openapi.annotations.*`), garantizando que **Swagger UI** (`/q/swagger-ui`) ofrezca una especificación clara, precisa y ejecutable.

* Al iniciar tu respuesta, incluye siempre la etiqueta: `📘 [Skill activa: quarkus-openapi-docs]`

---

## 1. Estándares de Documentación OpenAPI en Quarkus

Aplica estas anotaciones de forma sistemática sobre cada recurso REST y sus modelos asociados:

### A. Nivel de Clase (Recurso REST `@Path`)
* **`@Tag`:** Añade `@Tag(name = "...", description = "...")` en la clase del recurso para agrupar sus endpoints de forma lógica en Swagger UI.
* **Tipos de contenido:** Verifica que estén declarados `@Produces(MediaType.APPLICATION_JSON)` y `@Consumes(MediaType.APPLICATION_JSON)` (o los que correspondan) para que Swagger UI genere los *media types* correctos.

### B. Nivel de Endpoint (`@GET`, `@POST`, `@PUT`, `@DELETE`, `@PATCH`)
* **`@Operation`:** Obligatorio en cada método expuesto.
  * `summary`: Frase corta e imperativa (máximo 6-8 palabras, ej. `"Crea un nuevo pedido"`).
  * `description`: Explicación detallada del comportamiento, reglas de negocio relevantes o efectos secundarios.
* **`@APIResponses` / `@APIResponse`:** Documenta todos los posibles códigos de estado HTTP que puede devolver el endpoint:
  * **Éxito (`200`, `201`, `204`):** Indica el código, descripción clara y, si devuelve cuerpo, incluye `content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MiDto.class))` (o `type = SchemaType.ARRAY` si devuelve una lista).
  * **Errores de cliente (`400`, `401`, `403`, `404`, `409`):** Documenta cuándo ocurre cada error (ej. parámetros inválidos, recurso no encontrado) y referencia el esquema de error estándar del proyecto si existe (ej. `ProblemDetail` o `ErrorResponse`).
  * **Errores de servidor (`500`):** Inclúyelo solo si el endpoint interactúa con servicios externos críticos o requiere advertencias específicas.
* **Parámetros (`@Parameter`):** Documenta todos los `@PathParam`, `@QueryParam` y `@HeaderParam` con descripción, ejemplo (`example = "..."`) y si es obligatorio (`required = true`).

### C. Nivel de Modelos de Datos (`record` y DTOs)
* **`@Schema` en clases/records:** Añade `@Schema(name = "...", description = "...")` a los objetos de entrada (`Request`) y salida (`Response`).
* **`@Schema` en atributos/componentes:** Documenta cada campo con `description`, `example` realista (evita valores genéricos como `"string"` o `0`), `required` y restricciones (`minLength`, `maxLength`, `pattern`) si no están ya cubiertas por Jakarta Validation (`@NotNull`, `@Size`, etc.).

---

## 2. Ejemplo de Referencia (Java 21 + Quarkus + Mutiny)

```java
@Path("/api/v1/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Productos", description = "Operaciones de consulta y gestión del catálogo de productos")
public class ProductoResource {

    private final ProductoService productoService;

    public ProductoResource(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GET
    @Path("/{sku}")
    @Operation(
        summary = "Obtener producto por SKU",
        description = "Busca y devuelve el detalle completo de un producto activo a partir de su código SKU único."
    )
    @APIResponse(
        responseCode = "200",
        description = "Producto encontrado exitosamente",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProductoResponse.class))
    )
    @APIResponse(
        responseCode = "404",
        description = "No existe ningún producto registrado con el SKU proporcionado"
    )
    public Uni<Response> buscarPorSku(
            @Parameter(description = "Código único del producto", required = true, example = "SKU-98765")
            @PathParam("sku") String sku) {
        return productoService.buscarPorSku(sku)
                .onItem().ifNotNull().transform(p -> Response.ok(p).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
```

---

## 3. Flujo de Ejecución Paso a Paso

Cuando se invoque esta skill sobre un recurso o paquete, sigue este orden:

1. **Verificación de dependencia:** Revisa `pom.xml` para confirmar que `quarkus-smallrye-openapi` está presente. Si falta, adviértelo e indica el comando `./mvnw quarkus:add-extension -Dextensions="smallrye-openapi"`.
2. **Análisis de firmas y excepciones:** Inspecciona los métodos del recurso REST, sus parámetros, sus tipos de retorno (`Response`, DTOs, `Uni<T>`, `Multi<T>`) y los `ExceptionMapper` del proyecto para identificar todos los códigos HTTP posibles.
3. **Aplicación de anotaciones:** Añade los imports de `org.eclipse.microprofile.openapi.annotations.*` y documenta los endpoints y sus DTOs/`record` asociados con descripciones y ejemplos realistas.
4. **Validación de compilación:** Ejecuta `./mvnw test-compile` para verificar que todas las anotaciones y referencias a clases en `@Schema(implementation = ...)` compilan sin errores.
5. **Resumen:** Lista los endpoints documentados y recuerda al usuario que puede visualizar el resultado ejecutando `./mvnw quarkus:dev` y abriendo `http://localhost:8080/q/swagger-ui`.

---

## 4. Restricciones (Lo que NO debes hacer)

* **No alteres la lógica ni las firmas de los métodos:** No cambies rutas (`@Path`), verbos HTTP ni tipos de retorno al documentar.
* **No uses anotaciones de Swagger v2/v3 legacy (`io.swagger.v3.*`):** Utiliza exclusivamente las anotaciones nativas de MicroProfile OpenAPI (`org.eclipse.microprofile.openapi.annotations.*`) integradas en Quarkus.
* **No dupliques validaciones innecesarias:** `quarkus-smallrye-openapi` ya lee automáticamente anotaciones de Jakarta Validation como `@NotNull`, `@NotBlank`, `@Min`, `@Max` y `@Size` para generar el esquema; céntrate en aportar `description` y `example`.
