package com.chetan.snoutscout

import android.app.Application
import com.chetan.snoutscout.app.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SnoutScoutApp : Application() {

    lateinit var appContainer: AppContainer
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)

        applicationScope.launch {
            appContainer.seedLoader.seedIfEmpty()
        }
    }
}