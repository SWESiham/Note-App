package com.example.noteapp.data

import androidx.room.*
import com.example.noteapp.data.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY time DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)
}
