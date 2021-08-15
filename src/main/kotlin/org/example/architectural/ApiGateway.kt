package org.example.architectural

import java.io.IOException
import java.net.URI
import java.net.http.HttpClient

import java.net.http.HttpResponse.BodyHandlers

import java.net.http.HttpRequest
import java.net.http.HttpResponse





/*Here's the Image microservice implementation.*/
interface ImageClient {
    val imagePath: String?
}

class ImageClientImpl : ImageClient {
    override val imagePath: String?
        get() {
            val httpClient: HttpClient? = HttpClient.newHttpClient()
            val httpGet = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:50005/image-path"))
                .build()
            try {
                val httpResponse: HttpResponse<String>? = httpClient?.send(httpGet, BodyHandlers.ofString())
                return httpResponse?.body()
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
            val httpClient = HttpClient.newHttpClient()
            val httpGet = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:50006/price"))
                .build()
            try {
                val httpResponse = httpClient.send(httpGet, BodyHandlers.ofString())
                return httpResponse.body()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }
}

/*Here we can see how API Gateway maps the requests to the microservices.
class ApiGateway {
    @Resource
    private val imageClient: ImageClient? = null

    @Resource
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