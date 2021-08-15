package org.example.behavioral

import java.security.InvalidParameterException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.IntStream
import kotlin.concurrent.thread


class Tenant(name: String, allowedCallsPerSecond: Int, callsCount: CallsCount) {
    val name: String
    val allowedCallsPerSecond: Int

    init {
        if (allowedCallsPerSecond < 0) {
            throw InvalidParameterException("Number of calls less than 0 not allowed")
        }
        this.name = name
        this.allowedCallsPerSecond = allowedCallsPerSecond
        callsCount.addTenant(name)
    }
}

class CallsCount {
    private val tenantCallsCount: MutableMap<String, AtomicLong> = ConcurrentHashMap()
    fun addTenant(tenantName: String) {
        tenantCallsCount.putIfAbsent(tenantName, AtomicLong(0))
    }

    fun incrementCount(tenantName: String) {
        tenantCallsCount[tenantName]!!.incrementAndGet()
    }

    fun getCount(tenantName: String): Long {
        return tenantCallsCount[tenantName]!!.get()
    }

    fun reset() {
        println("Resetting the map.")
        tenantCallsCount.replaceAll { k: String?, v: AtomicLong? ->
            AtomicLong(
                0
            )
        }
    }
}


interface Throttler {
    fun start()
}

class ThrottleTimerImpl(private val period: Long, private val callsCount: CallsCount) : Throttler {
    override fun start() {
        val t = thread(start = true,isDaemon = true){
            while (!Thread.currentThread().isInterrupted){
                Thread.sleep(period)
                callsCount.reset()
            }
        }
    }
}


internal class B2BService(timer: Throttler, private val callsCount: CallsCount) {
    fun dummyCustomerApi(tenant: Tenant): Int {
        val tenantName = tenant.name
        val count = callsCount.getCount(tenantName)
        println("Counter for ${tenant.name} : $count ")
        if (count >= tenant.allowedCallsPerSecond) {
            println("API access per second limit reached for: ${tenantName}")
            return -1
        }
        callsCount.incrementCount(tenantName)
        return randomCustomerId
    }

    private val randomCustomerId: Int
        get() = ThreadLocalRandom.current().nextInt(1, 10000)


    init {
        timer.start()
    }
}

fun main() {
    val callsCount = CallsCount()
    val adidas = Tenant("Adidas", 5, callsCount)
    val nike = Tenant("Nike", 6, callsCount)
    val executorService = Executors.newFixedThreadPool(2)
    executorService.execute { makeServiceCalls(adidas, callsCount) }
    executorService.execute { makeServiceCalls(nike, callsCount) }
    executorService.shutdown()
    try {
        executorService.awaitTermination(10, TimeUnit.SECONDS)
    } catch (e: InterruptedException) {
        println("Executor Service terminated: ${e.message}")
    }
}

private fun makeServiceCalls(tenant: Tenant, callsCount: CallsCount) {
    val timer = ThrottleTimerImpl(60_000, callsCount)
    val service = B2BService(timer, callsCount)
    // Sleep is introduced to keep the output in check and easy to view and analyze the results.
    IntStream.range(0, 20).forEach { i: Int ->
        service.dummyCustomerApi(tenant)
        try {
            Thread.sleep(1)
        } catch (e: InterruptedException) {
            println("Thread interrupted: ${e.message}")
        }
    }
}