package com.example.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.database.DriverFactory
import com.example.gymtracker.routing.RootRouter
import com.example.gymtracker.settings.SettingsStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingsStorage.init(this)
        enableEdgeToEdge()
        val router =
            RootRouter(
                componentContext =  defaultComponentContext(),
                storeFactory = DefaultStoreFactory(),
                databasesBuilder = DatabasesBuilder(
                    driverFactory = DriverFactory(context = this),
                ),
            )

        setContent {
            GymTrackerApplication(
                router = router,
            )
        }
    }
}
