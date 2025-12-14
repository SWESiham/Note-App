package com.example.noteapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application,private val dao: NotesDao) : AndroidViewModel(application){
//    private val database = NoteDatabase.getInstance(application)
//    private val dao = database.noteDao()
    val notes = dao.getAllNotes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertNote(note: Note) {
        viewModelScope.launch {
            dao.insert(note)
        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            dao.update(note)
        }
    }
}