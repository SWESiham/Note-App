package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val header: String,
    val body: String,
    val time: Long = System.currentTimeMillis(),
    val subtitle: String = "",
    val imagePath: String = "",
    val color: Int = 0,
)