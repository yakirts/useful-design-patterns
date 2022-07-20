package org.example.behavioral

internal interface Processor<I : Any, O : Any> {
    fun process(input: I): O
}

internal class Pipeline<I : Any, O : Any>(private val currentProcessor: Processor<I, O>) {

    fun <K : Any> addHandler(newProcessor: Processor<O, K>): Pipeline<I, K> {
        return Pipeline(object : Processor<I, K> {
            override fun process(input: I): K = newProcessor.process(currentProcessor.process(input))
        })
    }

    fun execute(input: I): O {
        return currentProcessor.process(input)
    }
}

class A : Processor<String, String> {
    override fun process(input: String): String {
        println("In A")
        return input + "1.0"
    }
}

class B : Processor<String, Double> {
    override fun process(input: String): Double {
        println("In B")
        return 2.0
    }
}

class C : Processor<Double, Float> {
    override fun process(input: Double): Float {
        println("In C")
        return 3f
    }
}


fun main() {
    val handlers = Pipeline(A()).addHandler(B()).addHandler(C())
    handlers.execute("welcome")
}