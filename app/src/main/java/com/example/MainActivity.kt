package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.CyberViewModel
import com.example.ui.MainScreen
import com.example.ui.theme.CyberTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CyberViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CyberTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
