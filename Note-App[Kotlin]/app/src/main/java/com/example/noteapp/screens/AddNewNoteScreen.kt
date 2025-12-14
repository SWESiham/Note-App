package com.example.noteapp.screens

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteViewModel
import java.io.File
import java.io.FileOutputStream

val noteColors = listOf(
    Color(0xFF2E003E),
    Color(0xFFF47000C),
    Color(0xFFF880808),
    Color(0xFFF630330),
    Color(0xFF263D66),
    Color(0xFF5E2C68),
    Color(0xFF455A64)
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel,
    noteId: Int = -1,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(noteColors[0]) }

    val animatedBackground by animateColorAsState(
        targetValue = selectedColor,
        animationSpec = tween(durationMillis = 500),
        label = "ColorAnimation"
    )

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var finalImagePath by remember { mutableStateOf("") }
    var noteTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val notes by viewModel.notes.collectAsState()
    val noteToEdit = remember(noteId) { notes.find { it.id == noteId } }

    LaunchedEffect(noteToEdit) {
        noteToEdit?.let {
            title = it.header
            subtitle = it.subtitle
            body = it.body
            selectedColor = Color(it.color)
            if (it.imagePath.isNotEmpty()) {
                imageUri = Uri.parse(it.imagePath)
                finalImagePath = it.imagePath
            }
            noteTime = it.time
        }
    }

    val currentDate = remember {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE")
        java.time.LocalDate.now().format(formatter)
    }

    val imageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            val savedPath = saveImageToInternalStorage(context, uri)
            finalImagePath = savedPath ?: ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onBack() },
                    modifier = Modifier
                        .background(Color.Black.copy(0.3f), CircleShape)
                        .size(45.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }

                Button(
                    onClick = {
                        if (title.isNotEmpty() || body.isNotEmpty()) {
                            if (noteId == -1) {
                                viewModel.insertNote(
                                    Note(
                                        header = title, body = body, subtitle = subtitle,
                                        time = System.currentTimeMillis(),
                                        color = selectedColor.toArgb(),
                                        imagePath = finalImagePath
                                    )
                                )
                            } else {
                                viewModel.updateNote(
                                    Note(
                                        id = noteId,
                                        header = title, body = body, subtitle = subtitle,
                                        time = noteTime,
                                        color = selectedColor.toArgb(),
                                        imagePath = finalImagePath
                                    )
                                )
                            }
                            onBack()
                        } else {
                            Toast.makeText(context, "Note is empty!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(0.3f)),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) Text("Title", color = Color.White.copy(0.5f), fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date
            Text(
                text = currentDate,
                color = Color.White.copy(0.6f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Subtitle
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(0.5f))
                )
                Spacer(modifier = Modifier.width(12.dp))
                BasicTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    textStyle = TextStyle(color = Color.White.copy(0.9f), fontSize = 18.sp),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        if (subtitle.isEmpty()) Text("Subtitle...", color = Color.White.copy(0.5f), fontSize = 18.sp)
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Image
            imageUri?.let {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(0.2f)),
                        contentScale = ContentScale.FillWidth
                    )
                    IconButton(
                        onClick = {
                            imageUri = null
                            finalImagePath = ""
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .background(Color.Black.copy(0.6f), CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, "Remove", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Body
            BasicTextField(
                value = body,
                onValueChange = { body = it },
                textStyle = TextStyle(color = Color.White.copy(0.9f), fontSize = 18.sp, lineHeight = 28.sp),
                modifier = Modifier.fillMaxSize(),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (body.isEmpty()) Text("Type something...", color = Color.White.copy(0.4f), fontSize = 18.sp)
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.height(100.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color(0xFF1F1F1F),
            shadowElevation = 15.dp,
            tonalElevation = 5.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { imageLauncher.launch("image/*") },
                    modifier = Modifier
                        .border(1.dp, Color.Gray.copy(0.5f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Outlined.Image, "Add Image", tint = Color.White)
                }

                Spacer(modifier = Modifier.weight(1f))

                noteColors.forEach { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 34.dp else 28.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}