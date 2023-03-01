package modules

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import storages.TokenStorage

val clientModule = module {
    factory {
        val url = "https://device-com.onrender.com/backend/admin/"
        val tokenStorage: TokenStorage by inject()
        HttpClient(Js) {
            install(Resources)
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = false
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging)
            defaultRequest {
                url(url)
                tokenStorage.value.value?.accessToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }
        }
    }
}