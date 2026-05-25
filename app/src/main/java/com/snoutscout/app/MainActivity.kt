package com.snoutscout.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.snoutscout.app.core.theme.SnoutScoutTheme
import com.snoutscout.app.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnoutScoutTheme {
                NavGraph()
            }
        }
    }
}
