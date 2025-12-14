package com.example.noteapp

import com.example.noteapp.data.Note
import com.example.noteapp.data.NotesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeNotesDao : NotesDao {

    val notes = mutableListOf<Note>()

    override fun getAllNotes(): Flow<List<Note>> =
        flowOf(notes.toList())

    override suspend fun insert(note: Note) {
        notes.add(note)
    }

    override suspend fun delete(note: Note) {
        notes.remove(note)
    }

    override suspend fun update(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) notes[index] = note
    }

    override suspend fun getNoteById(id: Int): Note? =
        notes.find { it.id == id }

}

