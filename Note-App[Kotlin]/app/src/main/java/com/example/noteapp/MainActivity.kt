package com.example.noteapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.data.NoteViewModel
import com.example.noteapp.screens.AddNoteScreen
import com.example.noteapp.screens.MainScreen
import com.example.noteapp.screens.SplashScreen
import com.example.noteapp.ui.theme.NoteAppTheme
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider




class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                val context = LocalContext.current
                val dao = NoteDatabase.getInstance(context).noteDao()

                val viewModel: NoteViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return NoteViewModel(
                                context.applicationContext as Application,
                                dao
                            ) as T
                        }
                    }
                )

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