package org.example.system.urlshortner

class UrlShortener {

    val PREFIX = "https://tinyurl.com/"
    val storage = ShortUrlPersistenceStorage("/home/yakirt/IdeaProjects/design-patterns-examples/workspace")

    fun short(url: String): String {
        return storage.urlOperation.get(url)?.get(Urls.shortUrl) ?: return storage.urlOperation.insert(url, PREFIX)
    }

    fun redirect(url: String): String {
        val id = url.split("/").last().toLong()
        return storage.urlOperation.get(id)?.get(Urls.longUrl) ?: "Bad url"
    }
}