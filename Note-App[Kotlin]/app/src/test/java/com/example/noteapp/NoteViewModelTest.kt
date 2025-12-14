package com.example.noteapp
import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteViewModel
import com.example.noteapp.data.NotesDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var dao: NotesDao
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        dao = mock(NotesDao::class.java)

        `when`(dao.getAllNotes()).thenReturn(flowOf(emptyList()))

        val app = mock(Application::class.java)
        viewModel = NoteViewModel(app, dao)
    }

    @Test
    fun insertNote_callsDaoInsert() {
        runTest {
            val note = Note(1, "Title", "Body", 123)
            viewModel.insertNote(note)
            verify(dao).insert(note)

        }
    }

    @Test
    fun deleteNote_callsDaoDelete() {
        runTest {
            val note = Note(1, "Title", "Body", 123)

            viewModel.deleteNote(note)

            verify(dao).delete(note)
        }
    }
    @Test
    fun updateNote_callsDaoUpdate() {
        runTest {
            val note = Note(1, "Title", "Body", 123)

            viewModel.updateNote(note)

            verify(dao).update(note)
        }
    }
}
