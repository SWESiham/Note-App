package com.example.noteapp

import android.app.Application
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelJUnitTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var dao: FakeNotesDao
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        dao = FakeNotesDao()
        val app = mock(Application::class.java)
        viewModel = NoteViewModel(app, dao)
    }

    @Test
    fun insertNote_addsNoteToList() = runTest {
        val note = Note(1, "Title", "Body", 123)

        viewModel.insertNote(note)

        assert(dao.notes.contains(note))
    }

    @Test
    fun deleteNote_removesNoteFromList() = runTest {
        val note = Note(1, "Title", "Body", 123)
        dao.notes.add(note)

        viewModel.deleteNote(note)

        assert(!dao.notes.contains(note))
    }


    @Test
    fun getAllNotes_returnsAllNotes() = runTest {
        val note1 = Note(1, "", "A", 100)
        val note2 = Note(2, "B", "B", 200)

        dao.insert(note1)
        dao.insert(note2)

        val result = dao.getAllNotes().first()

        assert(result.size == 2)
    }
}
