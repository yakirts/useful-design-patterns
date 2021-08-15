package org.example.behavioral

internal interface Handler<I : Any, O : Any> {
    fun process(input: I): O
}

internal class Pipeline<I : Any, O : Any>(private val currentHandler: Handler<I, O>) {

    fun <K : Any> addHandler(newHandler: Handler<O, K>): Pipeline<I, K> {
        return Pipeline(object : Handler<I, K> {
            override fun process(input: I): K = newHandler.process(currentHandler.process(input))
        })
    }

    fun execute(input: I): O {
        return currentHandler.process(input)
    }
}

class A : Handler<String, String> {
    override fun process(input: String): String {
        println("In A")
        return input + "1.0"
    }
}

class B : Handler<String, Double> {
    override fun process(input: String): Double {
        println("In B")
        return 2.0
    }
}

class C : Handler<Double, Float> {
    override fun process(input: Double): Float {
        println("In C")
        return 3f
    }
}


fun main() {
    val handlers = Pipeline(A()).addHandler(B()).addHandler(C())
    handlers.execute("welcome")
}