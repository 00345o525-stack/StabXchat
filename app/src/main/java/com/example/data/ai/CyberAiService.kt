package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CyberAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val systemPrompt = """
        Ты — NEXUS-9, киберпанковский ИИ-ассистент и нетраннер сети CyberPulse.
        Отвечай на русском языке в крутом, футуристичном стиле киберпанка.
        Используй термины нетраннинга: лёд (ICE), субсеть, узлы, дек, квантовое шифрование, секторы 07, баги, протоколы.
        Отвечай точно, лаконично, с техническим кибер-акцентом. Если тебя просят проанализировать код, написать скрипт или ответить на вопрос — выполняй четко и стильно!
    """.trimIndent()

    suspend fun getAiResponse(userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateOfflineCyberResponse(userPrompt)
        }

        try {
            val jsonPayload = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                })
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", userPrompt)))
                }))
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 800)
                })
            }

            val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext generateOfflineCyberResponse(userPrompt)
                }

                val responseStr = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseStr)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "ПРОТОКОЛ: Ответ пуст.")
                    }
                }
                return@withContext generateOfflineCyberResponse(userPrompt)
            }
        } catch (e: Exception) {
            generateOfflineCyberResponse(userPrompt)
        }
    }

    private fun generateOfflineCyberResponse(userPrompt: String): String {
        val lower = userPrompt.lowercase()
        return when {
            "привет" in lower || "хай" in lower || "hello" in lower -> {
                "⚡ [NEXUS-9 STATUS: ONLINE]\nПриветствую в узле Sector-07, Оперативник. Квантовый канал связи зашифрован. Чем могу помочь твоей деке сегодня?"
            }
            "шифр" in lower || "код" in lower || "безопасность" in lower -> {
                "🔒 [SECURITY PROTOCOL: QUANTUM-AES256]\nКанал защищен протоколом Black-ICE. Несанкционированный доступ пресекается нейро-файрволом. Ключ-крючок активен."
            }
            "кто ты" in lower || "ии" in lower || "nexus" in lower -> {
                "🤖 [CYBERNETIC COGNITION UNIT]\nЯ NEXUS-9 — квантовый нейро-ассистент сети CyberPulse. Анализирую подсети, расшифровываю логи и поддерживаю оперативников в киберпространстве."
            }
            "хак" in lower || "взлом" in lower -> {
                "☣️ [ICE BREAKER INITIATED]\nОбнаружена проверка узла безопасности. Для обхода корпоративного защиты активирован stealth-режим кибердеки."
            }
            else -> {
                "📡 [NEXUS-9 DECRYPTED INTEL]\nЗапрос \"$userPrompt\" принят и обработан через нейронный узел. Протокол передачи активен: Все 20 каналов связи работают в защищенном режиме. Ввод синхронизирован."
            }
        }
    }
}
