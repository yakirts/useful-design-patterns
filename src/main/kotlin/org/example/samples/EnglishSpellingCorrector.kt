package org.example.samples

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import java.io.File

class EnglishSpellingCorrector(val path: String) {

    val corpus =  File(path)
    val regex = "\\w+".toRegex()
    val WORDS = countWords()

    private fun countWords (): Object2LongOpenHashMap<String> {
        val counter = Object2LongOpenHashMap<String>()
        corpus.bufferedReader().forEachLine {
            val re = regex.findAll(it.lowercase())
            re.forEach { r ->
                counter.addTo(r.value,1)
            }
        }
        return counter
    }

    fun String.getPropability(N: Int = WORDS.size) = WORDS.getValue(this) / N


}