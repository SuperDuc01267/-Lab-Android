package com.example.baitaplab.lab3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.baitaplab.Student
import com.example.baitaplab.findStudentById

class Lab3DetailActivity : ComponentActivity() {

    companion object {
        const val EXTRA_STUDENT_ID = "extra_student_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val studentId = intent.getIntExtra(EXTRA_STUDENT_ID, -1)
        val student: Student? = findStudentById(studentId)

        setContent {
            MaterialTheme {
                if (student != null) {
                    StudentDetailScreen(
                        student = student,
                        onBack = { finish() }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Không tìm thấy sinh viên")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(student: Student, onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chi tiết sinh viên") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name.takeLast(1).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }


            Text(
                text = student.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Ngành: ${student.major}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Text(
                text = "Email: ${student.email}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "SĐT: ${student.phone}",
                style = MaterialTheme.typography.bodyLarge
            )


            Button(
                onClick = {
                    Toast.makeText(context, "Gọi điện cho ${student.phone}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text("Gọi điện")
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Gửi email tới ${student.email}", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gửi email")
            }
        }
    }
}