package com.example.gymtracker.settings

import platform.Foundation.NSUserDefaults

actual class SettingsStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getString(key: String): String? =
        defaults.stringForKey(key)

    actual fun putInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), forKey = key)
    }

    actual fun getInt(key: String): Int? {
        val hasValue = defaults.objectForKey(key) != null
        return if (hasValue) defaults.integerForKey(key).toInt() else null
    }

    actual fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    actual fun getBoolean(key: String): Boolean? {
        val hasValue = defaults.objectForKey(key) != null
        return if (hasValue) defaults.boolForKey(key) else null
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}
