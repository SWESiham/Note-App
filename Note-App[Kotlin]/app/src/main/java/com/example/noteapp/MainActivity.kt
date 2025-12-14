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
import com.example.noteapp.screens.MainScreen
import com.example.noteapp.screens.SplashScreen
import com.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                val viewModel: NoteViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("splash") }
                var selectedNoteId by remember { mutableIntStateOf(-1) }

                when (currentScreen) {
                    "splash" -> {
                        SplashScreen(onTimeout = {
                            currentScreen = "main"
                        })
                    }
                    "add" -> {
                        AddNoteScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "main" }
                        )
                    }
                    "edit" -> {
                        if (selectedNoteId != -1) {
                            AddNoteScreen(
                                viewModel = viewModel,
                                noteId = selectedNoteId,
                                onBack = {
                                    currentScreen = "main"
                                    selectedNoteId = -1
                                }
                            )
                        } else {
                            currentScreen = "main"
                        }
                    }
                    "main" -> {
                        MainScreen(
                            onAddNoteClick = { currentScreen = "add" },
                            onEditNoteClick = { id ->
                                selectedNoteId = id
                                currentScreen = "edit"
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}