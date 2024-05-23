package com.example.firebasechatdemo.screens.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.firebasechatdemo.navigations.RootNavGraph
import com.example.firebasechatdemo.utils.SessionManagerClass
import com.example.firebasechatdemo.ui.theme.FirebaseChatDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var sessionManagerClass: SessionManagerClass? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManagerClass = SessionManagerClass(applicationContext)
        setContent {
            FirebaseChatDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavGraph(sessionManagerClass!!)
                }
            }
        }
    }
}