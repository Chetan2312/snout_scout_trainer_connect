package com.chetan.snoutscout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chetan.snoutscout.navigation.AppNavGraph
import com.chetan.snoutscout.core.theme.SnoutScoutTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnoutScoutTheme {
                AppNavGraph()
            }
        }
    }
}