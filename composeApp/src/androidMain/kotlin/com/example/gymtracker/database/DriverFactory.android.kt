package com.example.gymtracker.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
//        val dbFile = context.getDatabasePath("gymtracker.db")
//        if (dbFile.exists()) {
//            dbFile.delete()
//        }
        return AndroidSqliteDriver(Database.Schema.synchronous(), context, "gymtracker.db")
    }
}
