package com.snoutscout.app

import android.app.Application
import com.snoutscout.app.di.AppContainer

class SnoutScoutApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
