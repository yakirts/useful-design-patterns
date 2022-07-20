package org.example.architectural

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.IOException


/*Here's the Image microservice implementation.*/
interface ImageClient {
    val imagePath: String?
}

class ImageClientImpl : ImageClient {
    override val imagePath: String?
        get() {
            val httpClient =  HttpClient(CIO){
                Json {
                    serializer = GsonSerializer()
                    accept(ContentType.Application.Json)
                }
            }
            try {
                val response = runBlocking {
                    httpClient.get<String>("http://localhost:50005/image-path")
                }
                return response
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }
}

/*Here's the Price microservice implementation.*/
interface PriceClient {
    val price: String?
}

class PriceClientImpl : PriceClient {
    override val price: String?
        get() {
            val httpClient =  HttpClient(CIO){
                Json {
                    serializer = GsonSerializer()
                    accept(ContentType.Application.Json)
                }
            }

            try {
                val response = runBlocking {
                    httpClient.get<String>("http://localhost:50006/price")
                }
                return response
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }
}

/*
//Here we can see how API Gateway maps the requests to the microservices.
class ApiGateway {

    private val imageClient: ImageClient? = null


    private val priceClient: PriceClient? = null

    @get:RequestMapping(path = "/desktop", method = RequestMethod.GET)
    val productDesktop: DesktopProduct
        get() {
            val desktopProduct = DesktopProduct()
            desktopProduct.setImagePath(imageClient!!.imagePath)
            desktopProduct.setPrice(priceClient!!.price)
            return desktopProduct
        }

    @get:RequestMapping(path = "/mobile", method = RequestMethod.GET)
    val productMobile: MobileProduct
        get() {
            val mobileProduct = MobileProduct()
            mobileProduct.setPrice(priceClient!!.price)
            return mobileProduct
        }
}*/
