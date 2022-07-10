package org.example.system.urlshortner

class UrlShortener {

    val PREFIX = "https://tinyurl.com/"
    val storage = ShortUrlPersistenceStorage("path to db")

    fun short(url: String): String {
        return storage.urlOperation.get(url)?.get(Urls.shortUrl) ?: return storage.urlOperation.insert(url, PREFIX)
    }

    fun redirect(id: Int): String {
        return storage.urlOperation.get(id)?.get(Urls.longUrl) ?: "Bad url"
    }
}