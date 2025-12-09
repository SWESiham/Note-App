package com.example.noteapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.NoteViewModel
import com.example.noteapp.screens.AddNoteScreen
import com.example.noteapp.screens.MainScreen  // Add this import
import com.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // Get ViewModel
                val viewModel: NoteViewModel = viewModel()

                // Track which screen to show
                var currentScreen by remember { mutableStateOf("main") }  // Start with MainScreen

                // Show different screens based on state
                when (currentScreen) {
                    "add" -> {
                        // Your friend's AddNoteScreen for creating new notes
                        AddNoteScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "main" }  // Fixed: added onBack parameter
                        )
                    }
                    "main" -> {
                        // Your MainScreen (shows list of notes)
                        MainScreen(
                            onAddNoteClick = { currentScreen = "add" },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}