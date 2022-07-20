package org.example.concurrent

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.security.SecureRandom

fun produce() = flow {
    emit(
        println(
            "Consumer consume item  produced by ]"
        )
    )
}

suspend fun main() {
    val random = SecureRandom()
    (0..100).forEach { _ ->
        produce().buffer().collect {
            delay(random.nextInt(2000).toLong())
            it
        }
    }
}