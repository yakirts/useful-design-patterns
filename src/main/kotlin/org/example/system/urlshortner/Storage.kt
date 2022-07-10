package org.example.system.urlshortner

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


class ShortUrlPersistenceStorage(private val dbPath: String) {

    private val db = DBConnectionHandler.handle("$dbPath/short-url.db")
    val urlOperation = ShortUrlPersistentOperations(db)

}

class ShortUrlPersistentOperations(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(Urls)
        }
    }
    fun insert(url: String,default: String) = transaction(db) {
        val id = Urls.insertAndGetId {
            it[longUrl] = url
            it[shortUrl] = ""
        }
        Urls.update({Urls.id eq id}) {
            it[shortUrl] = default + id.value
        }
        return@transaction default + id.value
    }

    fun get(url: String) = transaction(db){
        Urls.select { Urls.longUrl eq url  }.firstOrNull()
    }

    fun get(id: Long) = transaction(db){
        Urls.select { Urls.id eq id  }.firstOrNull()
    }
}
object Urls : LongIdTable() {
    var shortUrl = varchar("shortUrl", 30)
    val longUrl = varchar("longUrl", 255)
}


object DBConnectionHandler {
    fun handle(path: String): Database = Database.connect("jdbc:sqlite:$path", "org.sqlite.JDBC")
}
