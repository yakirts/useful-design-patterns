package org.example.system.cache

import java.util.LinkedList

interface Cache<K, V> {
    fun add(k: K, v: V)
    fun get(k: K): V
}

class LruCache<K, V>(val capacity: Int) : Cache<K, V> {

    private val cacheMap = object : LinkedHashMap<K, V>(capacity,0.8f, true) {

        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size >= capacity
        }

    }

    override fun add(k: K, v: V) {
        cacheMap[k] = v
    }

    override fun get(k: K): V {
        val v = cacheMap[k] ?: throw Exception("No such element")
        cacheMap[k] = v
        return v
    }

    override fun toString(): String {
        return cacheMap.toString()
    }

}

class SimpleLruCache<V>(val capacity: Int) {

    private val set = HashSet<V>(capacity)
    private val cache = LinkedList<V>()

    fun look(v: V){
        if (v in set) {
            cache.remove(v)
        }else {
            if (cache.size >= capacity) {
                set.remove(cache.removeFirst())
            }
        }
        set.add(v)
        cache.add(v)
    }

    override fun toString(): String {
        return cache.toString()
    }

}


fun main() {
    /*val mapLruCache = LruCache<Int, String>(4)
    mapLruCache.add(1, "yes")
    mapLruCache.add(2, "no")
    mapLruCache.add(3, "yes-no")
    println(mapLruCache)
    mapLruCache.get(1)
    println(mapLruCache)
    mapLruCache.add(4, "ok")
    mapLruCache.add(5, "not-ok")
    println(mapLruCache)*/


    val simpleCache = SimpleLruCache<Int>(3)
    simpleCache.look(1)
    simpleCache.look(2)
    simpleCache.look(3)
    simpleCache.look(1)
    println(simpleCache)
    simpleCache.look(4)
    println(simpleCache)
    simpleCache.look(5)
    println(simpleCache)

}