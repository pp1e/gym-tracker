package com.example.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.database.DriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GymTrackerApplication(
                databasesBuilder =
                    DatabasesBuilder(
                        driverFactory = DriverFactory(context = this),
                    ),
                componentContext = defaultComponentContext(),
            )
        }
    }
}
