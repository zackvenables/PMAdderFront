package com.example.pmadderfront

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.net.HttpURLConnection
import java.net.URL

class Utilities {
    fun searchAsync (query: String) = GlobalScope.async {
        val formattedQuery = query.replace(" ", "+")
        val url = URL("https://true-emu-lovely.ngrok-free.app/search?query=$formattedQuery")
        var result = ""

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    println(line)
                    result += line
                }
            }
        }
        return@async result
    }

    fun addAsync (downloadURL: String) = GlobalScope.async {
        val url = URL("https://true-emu-lovely.ngrok-free.app/add?url=$downloadURL")
        var result = ""

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"  // optional default is GET
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    result += line
                }
            }
        }
        return@async result
    }
}
