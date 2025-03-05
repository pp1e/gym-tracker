package com.example.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gymtracker.database.DriverFactory
import com.example.gymtracker.database.createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GymTrackerApplication(
                database = createDatabase(
                    driverFactory = DriverFactory(context = this)
                )
            )
        }
    }
}
