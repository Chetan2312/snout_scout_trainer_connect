package com.chetan.snoutscout

import android.app.Application
import com.chetan.snoutscout.app.AppContainer

class SnoutScoutApp : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}