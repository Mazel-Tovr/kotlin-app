package com.epam.kotlinapp

import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Contact
import de.nielsfalk.ktor.swagger.version.shared.Group
import de.nielsfalk.ktor.swagger.version.shared.Information
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import de.nielsfalk.ktor.swagger.version.v3.Schema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.StringValues
import io.ktor.util.pipeline.*
import io.ktor.util.toMap
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.find
import kotlin.collections.flatMap
import kotlin.collections.joinToString
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mapOf
import kotlin.collections.mapValues
import kotlin.collections.max
import kotlin.collections.mutableListOf
import kotlin.collections.set


data class PetModel(val id: Int?, val name: String) {
    companion object {
        val exampleSpike = mapOf(
            "id" to 1,
            "name" to "Spike"
        )

        val exampleRover = mapOf(
            "id" to 2,
            "name" to "Rover"
        )
    }
}

data class PetsModel(val pets: MutableList<PetModel>) {
    companion object {
        val exampleModel = mapOf(
            "pets" to listOf(
                PetModel.exampleSpike,
                PetModel.exampleRover
            )
        )
    }
}

data class Model<T>(val elements: MutableList<T>)

val sizeSchemaMap = mapOf(
    "type" to "number",
    "minimum" to 0
)

fun rectangleSchemaMap(refBase: String) = mapOf(
    "type" to "object",
    "properties" to mapOf(
        "a" to mapOf("${'$'}ref" to "$refBase/size"),
        "b" to mapOf("${'$'}ref" to "$refBase/size")
    )
)

val data = PetsModel(
    mutableListOf(
        PetModel(1, "max"),
        PetModel(2, "moritz")
    )
)

fun newId() = ((data.pets.map { it.id ?: 0 }.max()) ?: 0) + 1

@Group("pet operations")
@Location("/pets/{id}")
class pet(val id: Int)

@Group("pet operations")
@Location("/pets")
class pets

@Group("generic operations")
@Location("/genericPets")
class genericPets

const val petUuid = "petUuid"

@Group("generic operations")
@Location("/pet/custom/{id}")
class petCustomSchemaParam(
    @Schema(petUuid)
    val id: String
)

val petIdSchema = mapOf(
    "type" to "string",
    "format" to "date",
    "description" to "The identifier of the pet to be accessed"
)

@Group("shape operations")
@Location("/shapes")
class shapes

@Group("debug")
@Location("/request/info")
class requestInfo

@Group("debug")
@Location("/request/withHeader")
class withHeader

class Header(val optionalHeader: String?, val mandatoryHeader: Int)

@Group("debug")
@Location("/request/withQueryParameter")
class withQueryParameter

class QueryParameter(val optionalParameter: String?, val mandatoryParameter: Int)

fun run(port: Int, wait: Boolean = true): ApplicationEngine {
    println("Launching on port `$port`")
    val server = embeddedServer(Netty, port) {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Locations)
        install(SwaggerSupport) {
            forwardRoot = true
            val information = Information(
                version = "0.1",
                title = "sample api implemented in ktor",
                description = "This is a sample which combines [ktor](https://github.com/Kotlin/ktor) with [swaggerUi](https://swagger.io/). You find the sources on [github](https://github.com/nielsfalk/ktor-swagger)",
                contact = Contact(
                    name = "Niels Falk",
                    url = "https://nielsfalk.de"
                )
            )
            swagger = Swagger().apply {
                info = information
                definitions["size"] = sizeSchemaMap
                definitions[petUuid] = petIdSchema
                definitions["Rectangle"] = rectangleSchemaMap("#/definitions")
            }
            openApi = OpenApi().apply {
                info = information
                components.schemas["size"] = sizeSchemaMap
                components.schemas[petUuid] = petIdSchema
                components.schemas["Rectangle"] = rectangleSchemaMap("#/components/schemas")
            }
        }
        routing {
            get<pets>("all".responds(ok<PetsModel>(example("model", PetsModel.exampleModel)))) {
                call.respond(data)
            }
            post<pets, PetModel>(
                "create"
                    .description("Save a pet in our wonderful database!")
                    .examples(
                        example("rover", PetModel.exampleRover, summary = "Rover is one possible pet."),
                        example("spike", PetModel.exampleSpike, summary = "Spike is a different posssible pet.")
                    )
                    .responds(
                        created<PetModel>(
                            example("rover", PetModel.exampleRover),
                            example("spike", PetModel.exampleSpike)
                        )
                    )
            ) { _, entity ->
                call.respond(Created, entity.copy(id = newId()).apply {
                    data.pets.add(this)
                })
            }
            get<pet>(
                "find".responds(
                    ok<PetModel>(),
                    notFound()
                )
            ) { params ->
                data.pets.find { it.id == params.id }
                    ?.let {
                        call.respond(it)
                    }
            }
            put<pet, PetModel>(
                "update".responds(
                    ok<PetModel>(),
                    notFound()
                )
            ) { params, entity ->
                if (data.pets.removeIf { it.id == params.id && it.id == entity.id }) {
                    data.pets.add(entity)
                    call.respond(entity)
                }
            }

            patch<pet, PetModel>(
                "update with patch".responds(
                    ok<PetModel>(),
                    notFound()
                )
            ) { params, entity ->
                if (data.pets.removeIf { it.id == params.id && it.id == entity.id }) {
                    data.pets.add(entity)
                    call.respond(entity)
                }
            }
            delete<pet>(
                "delete".responds(
                    ok<Unit>(),
                    notFound()
                )
            ) { params ->
                if (data.pets.removeIf { it.id == params.id }) {
                    call.respond(Unit)
                }
            }
            get<shapes>(
                "all".responds(
                    ok("Rectangle")
                ).operationId("getAllShapes")
            ) {
                call.respondText(
                    """
                    {
                        "a" : 10,
                        "b" : 25
                    }
                """.trimIndent(), ContentType.Application.Json
                )
            }
            get<genericPets>("all".responds(ok<Model<PetModel>>())) {
                call.respond(data)
            }

            get<petCustomSchemaParam>("pet by id".responds(ok<PetModel>())) {
            }
            get<requestInfo>(
                responds(ok<Unit>()),
                respondRequestDetails()
            )
            get<withQueryParameter>(
                responds(ok<Unit>())
                    .parameter<QueryParameter>(),
                respondRequestDetails()
            )
            get<withHeader>(
                responds(ok<Unit>())
                    .header<Header>(),
                respondRequestDetails()
            )
        }
    }
    return server.start(wait = wait)
}

fun respondRequestDetails(): suspend PipelineContext<Unit, ApplicationCall>.(Any) -> Unit {
    return {
        call.respond(
            mapOf(
                "parameter" to call.parameters,
                "header" to call.request.headers
            ).format()
        )
    }
}

private fun Map<String, StringValues>.format() =
    mapValues {
        it.value.toMap()
            .flatMap { (key, value) -> value.map { key to it } }
            .map { (key, value) -> "$key: $value" }
            .joinToString(separator = ",\n")
    }
        .map { (key, value) -> "$key:\n$value" }
        .joinToString(separator = "\n\n")
fun main() {
    run(8080,true)
}

