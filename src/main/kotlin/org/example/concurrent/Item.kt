package org.example.concurrent

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue


/**
 * Class take part of an [Producer]-[Consumer] exchange.
 */
class Item(val producer: String, val id: Int)

class ItemQueue {
    private val queue: BlockingQueue<Item>

    @Throws(InterruptedException::class)
    fun put(item: Item) {
        queue.put(item)
    }

    @Throws(InterruptedException::class)
    fun take(): Item {
        return queue.take()
    }

    init {
        queue = LinkedBlockingQueue(5)
    }
}
