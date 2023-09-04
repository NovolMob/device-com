package ru.novolmob.devicecom.modules

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.novolmob.devicecom.datastore.ITokenDataStore

val clientModule = module {
    factory {
        val url = "http://test.antares-software.ru:10008/"
        val tokenDataStore: ITokenDataStore by inject()
        runBlocking {
            while (!tokenDataStore.initialized()) delay(200)
        }
        HttpClient(OkHttp) {

            engine {
                config {
                    followRedirects(true)
                    followSslRedirects(true)
                }
            }

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
                tokenDataStore.tokenFlow.value?.accessToken?.let {
                    header(HttpHeaders.Authorization, "Bearer $it")
                }
            }
        }
    }
}