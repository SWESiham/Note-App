package com.example.noteapp.screens
import androidx.compose.foundation.layout.IntrinsicSize
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.noteapp.data.Note
import com.example.noteapp.data.NoteViewModel

val noteColors = listOf(
    Color(0xFF1F1F1F),
    Color(0xFFF47000C),
    Color(0xFFF880808),
    Color(0xFFF630330),
    Color(0xFF263D66),
    Color(0xFF5E2C68),
    Color(0xFF000000)
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(noteColors[0]) }
    var image by remember { mutableStateOf<Uri?>(null) }
    val time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val currentDate = remember {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE, d MMMM")
        java.time.LocalDate.now().format(formatter)
    }

    val imageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        image = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Navbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back btn
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Save btn
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF333333))
                        .clickable {
                            if (title.isNotEmpty() || body.isNotEmpty()) {
                                viewModel.insertNote(
                                    Note(
                                        header = title,
                                        body = body,
                                        subtitle = subtitle,
                                        time = time,
                                        color = selectedColor.toArgb(),
                                        imagePath = image?.toString() ?: ""
                                    )
                                )
                                Toast.makeText(context, "Note saved successfully", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // title
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { text ->
                    if (title.isEmpty()) Text(
                        "Title",
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium)
                    text()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // date
            Text(text = currentDate, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // subtitle line
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.7f))
                )
                Spacer(modifier = Modifier.width(10.dp))
                //subtitle txt
                BasicTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    textStyle = TextStyle(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp),
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { text ->
                        if (subtitle.isEmpty()) Text("Subtitle...", color = Color.Gray.copy(alpha = 0.5f), fontSize = 16.sp)
                        text()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // img input
            image?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.2f)),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // body
            BasicTextField(
                value = body,
                onValueChange = { body = it },
                textStyle = TextStyle(color = Color.White.copy(alpha = 0.85f), fontSize = 18.sp, lineHeight = 26.sp),
                modifier = Modifier.fillMaxSize(),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { text ->
                    if (body.isEmpty()) Text("Start typing...", color = Color.Gray.copy(alpha = 0.4f), fontSize = 18.sp)
                    text()
                }
            )
            Spacer(modifier = Modifier.height(100.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .height(60.dp),
            shape = RoundedCornerShape(50),
            color = Color(0xFF252525),
            shadowElevation = 10.dp,
            tonalElevation = 5.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // img icon
                IconButton(onClick = { imageLauncher.launch("image/*") }) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = "Add Image",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                noteColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 2.dp else 0.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }
        }
    }
}

