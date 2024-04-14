package com.example.languagedevelopmentapp.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLDecoder
import java.net.URLEncoder

object DataTransformer {
    fun <T> transformToJsonString(data: T): String {
        val jsonString = Gson().toJson(data)
        return URLEncoder.encode(jsonString,"UTF-8")
    }

    fun <T> transformToModel(jsonStringData: String, classModel: Class<T>): T {
        val decodedStringData = URLDecoder.decode(jsonStringData,"UTF-8")
        val type = TypeToken.getParameterized(classModel, classModel).type
        return Gson().fromJson(decodedStringData, type)
    }
}