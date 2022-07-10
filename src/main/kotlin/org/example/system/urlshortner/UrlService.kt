package org.example.system.urlshortner

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class UrlService {

    val urlShortener = UrlShortener()

    fun mount(app: Application) = app.apply {
        routing {
            post("/api/v1/data/shorten") {
                val url = this.call.receive<UrlClient.Request>()
                val short = try {
                    urlShortener.short(url.url)
                }catch (err: Exception){
                    return@post this.call.respond(err.localizedMessage)
                }
                this.call.respond("Successfully shorten url - $short")
            }

            get("/api/v1") {
                val urlParams = call.parameters
                val longUrl = try {
                    urlShortener.redirect(urlParams["id"]?.toIntOrNull()!!)
                }catch (err: Exception){
                    return@get this.call.respond(err.localizedMessage)
                }
                this.call.respondRedirect(longUrl,false)
            }
        }
    }

    companion object {

        private const val DEFAULT_PORT = 8888

        @JvmStatic
        fun main(args: Array<String>) {
            val nettyEngine = embeddedServer(Netty, port = DEFAULT_PORT) {
                install(ContentNegotiation) {
                    gson()
                }
              /*  install(HttpsRedirect)*/
            }
            UrlService().mount(nettyEngine.application)
            nettyEngine.start(true) as Unit
        }
    }
}