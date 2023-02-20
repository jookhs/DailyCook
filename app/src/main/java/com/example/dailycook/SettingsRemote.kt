package com.example.dailycook

import android.content.Context
import android.content.res.AssetManager
import com.example.dailycook.model.SettingsConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.io.Reader


private const val MAIN_SETTINGS_KEY = "main_settings"
private const val MAIN_SETTINGS_PATH = "main_settings.json"

class SettingsRemote(context: Context) {

    private var mainConfig: SettingsConfig? = null
    private var context = context

    fun toolConfig(): SettingsConfig? {
        return mainConfig ?: getSetting(
            key = MAIN_SETTINGS_PATH,
            clazz = SettingsConfig::class.java,
            defaultValue = null
        ).also {
            mainConfig = it
        }
    }

    private fun <T> getSetting(key: String, clazz: Class<T>, defaultValue: T?): T? {
        try {
            val gson = GsonBuilder().create()
            val setting: T = gson.fromJson<T>(context.assets.readAssetsFile(key), clazz)
            if (setting != null) {
                return setting
            }
        } catch (var6: Exception) {

        }
        return defaultValue
    }

    private fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}
}