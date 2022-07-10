package org.example.system.urlshortner

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

class UrlClient(val serviceUrl: String) {

    private val client = HttpClient(CIO){
        Json {
            serializer = GsonSerializer()
            accept(ContentType.Application.Json)
        }
    }

     suspend fun callShort(url: String): String =
        client.post("$serviceUrl/api/v1/data/shorten"){
            contentType(ContentType.Application.Json)
            body= Request(url)
        }

     suspend fun redirect(url: String): Any {
         val hash = url.split("/").last()

         val result  = client.get<String>("$serviceUrl/api/v1"){
             contentType(ContentType.Application.Json)
             parameter("id",hash)
         }
         return result
     }



    data class Request(val url: String)
}


suspend fun main() {
    val client = UrlClient("http://localhost:8888")
    val get = client.redirect("https://tinyurl.com/1")
    println()
    /*val longUrl = withContext(Dispatchers.IO) {
        val response = client.callShort("https://en.wikipedia.org/wiki/Systems_design")
        println(response)
    }*/
}