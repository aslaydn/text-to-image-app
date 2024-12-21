package com.example.texttoimage.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object JsonRequest {
    private val JSON = "application/json; charset=utf-8".toMediaType()

    fun create(prompt: String): RequestBody {
        val jsonObject = JSONObject()
        jsonObject.put("inputs", prompt)
        jsonObject.put("wait_for_model", true)
        return jsonObject.toString().toRequestBody(JSON)
    }
}