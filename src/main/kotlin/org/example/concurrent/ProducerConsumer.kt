package org.example.concurrent

import java.security.SecureRandom
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Consumer(private val name: String, private val queue: ItemQueue) {

    /**
     * Consume item from the queue.
     */
    @Throws(InterruptedException::class)
    fun consume() {
        val item: Item = queue.take()
        println(
            "Consumer [${this.name}] consume item [${item.id}] produced by [${item.producer}]"
        )
    }
}


class Producer(private val name: String, private val queue: ItemQueue) {
    private var itemId = 0

    /**
     * Put item in the queue.
     */
    @Throws(InterruptedException::class)
    fun produce() {
        val item = Item(name, itemId++)
        queue.put(item)
        Thread.sleep(RANDOM.nextInt(2000).toLong())
    }

    companion object {
        private val RANDOM: SecureRandom = SecureRandom()
    }
}


object App {

    @JvmStatic
    fun main(args: Array<String>) {
        val queue = ItemQueue()
        val executorService = Executors.newFixedThreadPool(5)
        for (i in 0..1) {
            val producer = Producer("Producer_$i", queue)
            executorService.submit<Any> {
                while (true) {
                    producer.produce()
                }
            }
        }
        for (i in 0..2) {
            val consumer = Consumer("Consumer_$i", queue)
            executorService.submit<Any> {
                while (true) {
                    consumer.consume()
                }
            }
        }
        executorService.shutdown()
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS)
            executorService.shutdownNow()
        } catch (e: InterruptedException) {
            println("Error waiting for ExecutorService shutdown")
        }
    }
}