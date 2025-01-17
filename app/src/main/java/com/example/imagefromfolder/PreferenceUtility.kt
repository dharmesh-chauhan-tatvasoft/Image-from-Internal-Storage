package com.example.imagefromfolder

import android.content.Context
import android.content.SharedPreferences

fun storePrefStringData(context: Context, key: String, value: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun storePrefIntData(context: Context, key: String, value: Int) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt(key, value)
    editor.apply()
}

fun getStorePrefStringData(context: Context, key: String, defaultValue: String = ""): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue)
}

fun getStorePrefIntData(context: Context, key: String, defaultValue: Int = 0): Int {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(key, defaultValue)
}