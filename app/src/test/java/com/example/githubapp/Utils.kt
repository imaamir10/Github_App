package com.example.githubapp

import java.io.InputStreamReader

object Utils {

    fun readFileResource(filename: String): String {
        val inputStream = Utils::class.java.getResourceAsStream(filename)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach() {
            builder.append(it)
        }
        return builder.toString()
    }

}