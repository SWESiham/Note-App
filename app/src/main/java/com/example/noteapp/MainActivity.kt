package com.example.noteapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.NoteViewModel
import com.example.noteapp.screens.AddNoteScreen
import com.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // تعريف الـ ViewModel
                val viewModel: NoteViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("add") }
                if (currentScreen == "home") {
//                    HomeScreen(
//                        viewModel = viewModel,
//                        onAddNoteClick = {
//                            currentScreen = "add"
//                        },
//                        onNoteClick = { note ->
//                        }
//                    )
                    Text(text = "هنا الشاشة الرئيسية", modifier = Modifier.padding(50.dp))
                } else {
                    AddNoteScreen(
                        viewModel = viewModel,
                        onBack = {
                            currentScreen = "home"
                        }
                    )
                }
            }
        }
    }
}
