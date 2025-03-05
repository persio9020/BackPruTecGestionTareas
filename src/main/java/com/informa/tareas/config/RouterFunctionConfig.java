package com.informa.tareas.config;

import com.informa.tareas.handler.AutenticacionHandler;
import com.informa.tareas.handler.UsuarioHandler;
import com.informa.tareas.handler.TareaHandler;
import com.informa.tareas.model.dto.*;
import com.informa.tareas.model.dto.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;

import java.util.ArrayList;
import java.util.HashMap;


@Configuration
public class RouterFunctionConfig {


    @RouterOperations(
            value = {
                    @RouterOperation(path = "/v1/login", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.POST,
                            beanMethod = "login",
                            operation = @Operation(
                                    operationId = "login",
                                    summary = "Inicio de sesion ",
                                    description = "Endpoint logear un usuario en el sistema.",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = AuthRequest.class
                                                    )
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "OK",
                                                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                                            @ApiResponse(responseCode = "401", description = "Unauthorized")
                                    }
                            )

                    ),
                    @RouterOperation(path = "/v1/registrarse", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.POST,
                            beanMethod = "registrarUsuario",
                            operation = @Operation(
                                    operationId = "crear",
                                    summary = "Registra un nuevo usuario",
                                    description = "Endpoint para registrar usuarios.",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = UsuarioRequest.class
                                                    )
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Created",
                                                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
                                            @ApiResponse(responseCode = "400", description = "Bad Request",
                                                    content = @Content(schema = @Schema(implementation = HashMap.class)))
                                    }
                            )

                    ),
                    @RouterOperation(path = "/v1/listar", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.GET,
                            beanMethod = "listar",
                            operation = @Operation(
                                    operationId = "listar",
                                    summary = "Listar usuarios",
                                    description = "Endpoint para listar usuarios",
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "OK",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            array = @ArraySchema(schema = @Schema(type = "object"))
                                                    )
                                            )
                                    }
                            )

                    ),


            })
    @Bean
    public RouterFunction<ServerResponse> routerFunctionAdministracionUsuarios(AutenticacionHandler autenticacionHandler, UsuarioHandler usuarioHandler, TareaHandler tareaHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/v1/login"), autenticacionHandler::login)
                .andRoute(RequestPredicates.POST("/v1/registrarse"), usuarioHandler::registrarUsuario)
                .andRoute(RequestPredicates.GET("/v1/listar"), usuarioHandler::listar);
    }

    @RouterOperations(
            value = {
                    @RouterOperation(path = "/v1/tarea", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.POST,
                            beanMethod = "crear",
                            operation = @Operation(
                                    operationId = "crear",
                                    summary = "Crea nueva tarea",
                                    description = "Endpoint para registrar tarea.",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TareaInsertRequest.class
                                                    )
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Created",
                                                    content = @Content(schema = @Schema(implementation = TareaResponse.class))),
                                            @ApiResponse(responseCode = "400", description = "Bad Request",
                                                    content = @Content(schema = @Schema(implementation = HashMap.class)))
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.GET,
                            beanMethod = "obtenerTareas",
                            operation = @Operation(
                                    operationId = "obtenerTareas",
                                    summary = "Obtiene todas las tareas que correspondan con los parámetros",
                                    description = "Endpoint listar tareas.",
                                    parameters = {
                                            @Parameter(name = "titulo", description = "Filtrar por título", required = false),
                                            @Parameter(name = "descripcion", description = "Filtrar por descripción", required = false),
                                            @Parameter(name = "estado", description = "Filtrar por estado", required = false),
                                            @Parameter(name = "posicionPagina", description = "Posición de la pagina", required = false),
                                            @Parameter(name = "numeroFilas", description = "Posición de la pagina", required = false),
                                    },
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "Ok",
                                                    content = @Content(schema = @Schema(implementation = TareaResponse.class))),
                                            @ApiResponse(responseCode = "404", description = "Not Found")
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.GET,
                            beanMethod = "obtenerTareasPorId",
                            operation = @Operation(
                                    operationId = "obtenerTareasPorId",
                                    summary = "Obtiene la tarea por id",
                                    description = "Endpoint que obtiene una tarea por id",
                                    parameters = {
                                            @Parameter(name = "id", description = "ID de la tarea", required = true)
                                    },
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "Ok",
                                                    content = @Content(schema = @Schema(implementation = TareaResponseUpdate.class))),
                                            @ApiResponse(responseCode = "404", description = "Not Found")
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.PUT,
                            beanMethod = "actualizar",
                            operation = @Operation(
                                    operationId = "actualizar",
                                    summary = "Actualiza una tarea",
                                    description = "Endpoint para actualizar tarea.",
                                    requestBody = @RequestBody(
                                            required = true,
                                            content = @Content(
                                                    schema = @Schema(
                                                            implementation = TareaUpdateRequest.class
                                                    )
                                            )
                                    ),
                                    responses = {
                                            @ApiResponse(responseCode = "201", description = "Created",
                                                    content = @Content(schema = @Schema(implementation = Response.class))),
                                            @ApiResponse(responseCode = "400", description = "Bad Request",
                                                    content = @Content(schema = @Schema(implementation = HashMap.class)))
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.DELETE,
                            beanMethod = "eliminar",
                            operation = @Operation(
                                    operationId = "eliminar",
                                    summary = "Elimina una tarea",
                                    description = "Endpoint para eliminar una tarea por id",
                                    parameters = {
                                            @Parameter(name = "id", description = "ID de la tarea", required = true)
                                    },
                                    responses = {
                                            @ApiResponse(responseCode = "204", description = "No Content",
                                                    content = @Content(schema = @Schema(implementation = TareaResponse.class))),
                                            @ApiResponse(responseCode = "404", description = "Not Found",
                                                    content = @Content(schema = @Schema(implementation = HashMap.class)))
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea/usuario/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.GET,
                            beanMethod = "obtenerTareasPorIdUsuario",
                            operation = @Operation(
                                    operationId = "obtenerTareasPorIdUsuario",
                                    summary = "Obtiene la tarea por usuario",
                                    description = "Endpoint que obtiene una tarea por usuario",
                                    parameters = {
                                            @Parameter(name = "id", description = "ID del usuario", required = true),
                                            @Parameter(name = "titulo", description = "Filtrar por título", required = false),
                                            @Parameter(name = "descripcion", description = "Filtrar por descripción", required = false),
                                            @Parameter(name = "estado", description = "Filtrar por estado", required = false),
                                            @Parameter(name = "posicionPagina", description = "Posición de la pagina", required = false),
                                            @Parameter(name = "numeroFilas", description = "Posición de la pagina", required = false),
                                    },
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "OK",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            array = @ArraySchema(schema = @Schema(type = "object"))
                                                    )
                                            ),
                                            @ApiResponse(responseCode = "404", description = "Not Found")
                                    }
                            )
                    ),
                    @RouterOperation(path = "/v1/tarea/usuario-notificaciones/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
                            method = RequestMethod.GET,
                            beanMethod = "obtenerPorIdUsuarioTabla",
                            operation = @Operation(
                                    operationId = "obtenerPorIdUsuarioTabla",
                                    summary = "Obtiene la tarea por usuario para las notificaciones",
                                    description = "Endpoint que obtiene una tarea por usuario para las notificaciones",
                                    parameters = {
                                            @Parameter(name = "id", description = "ID del usuario", required = true)
                                    },
                                    responses = {
                                            @ApiResponse(responseCode = "200", description = "OK",
                                                    content = @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            array = @ArraySchema(schema = @Schema(type = "object"))
                                                    )
                                            ),
                                            @ApiResponse(responseCode = "404", description = "Not Found")
                                    }
                            )
                    ),
            })
    @Bean
    public RouterFunction<ServerResponse> routerFunctionAdministracionTareas(AutenticacionHandler autenticacionHandler, UsuarioHandler usuarioHandler, TareaHandler tareaHandler) {
        return RouterFunctions.route(RequestPredicates.POST("/v1/tarea"), tareaHandler::crear)
                .andRoute(RequestPredicates.GET("/v1/tarea").and(
                        RequestPredicates.queryParam("titulo", t -> true)
                                .or(RequestPredicates.queryParam("descripcion", d -> true))
                                .or(RequestPredicates.queryParam("estado", e -> true))
                                .or(RequestPredicates.queryParam("posicionPagina", e -> true))
                                .or(RequestPredicates.queryParam("numeroFilas", e -> true))
                ), tareaHandler::obtenerTareas)
                .andRoute(RequestPredicates.GET("/v1/tarea/{id}"), tareaHandler::obtenerTareasPorId)
                .andRoute(RequestPredicates.PUT("/v1/tarea"), tareaHandler::actualizar)
                .andRoute(RequestPredicates.DELETE("/v1/tarea/{id}"), tareaHandler::eliminar)
                .andRoute(RequestPredicates.GET("/v1/tarea/usuario/{id}").and(
                        RequestPredicates.queryParam("titulo", t -> true)
                                .or(RequestPredicates.queryParam("descripcion", d -> true))
                                .or(RequestPredicates.queryParam("estado", e -> true))
                                .or(RequestPredicates.queryParam("posicionPagina", e -> true))
                                .or(RequestPredicates.queryParam("numeroFilas", e -> true))
                ), tareaHandler::obtenerTareasPorIdUsuario)
                .andRoute(RequestPredicates.GET("/v1/tarea/usuario-notificaciones/{id}")
                        , tareaHandler::obtenerPorIdUsuarioTabla);
    }
}
