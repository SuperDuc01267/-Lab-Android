package com.example.baitaplab.lab2

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class Student(
    val id: Int,
    val name: String,
    val major: String
)

class Lab2Activity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudentListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen() {

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val studentList = remember {
        listOf(
            Student(1, "Nguyễn Văn A", "Công nghệ thông tin"),
            Student(2, "Trần Thị B", "Thiết kế đồ họa"),
            Student(3, "Lê Văn C", "Quản trị kinh doanh"),
            Student(4, "Phạm Văn D", "An toàn thông tin")
        )
    }

    val filteredList = studentList.filter {
        it.name.contains(searchQuery.trim(), ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Danh sách sinh viên")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = {
                    Text("Tìm kiếm theo tên sinh viên")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = filteredList,
                    key = { it.id }
                ) { student ->
                    StudentCard(
                        student = student,
                        onViewClick = {
                            Toast.makeText(
                                context,
                                student.name,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentCard(
    student: Student,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name.split(" ").last().take(1).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }


            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = student.major,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }


            OutlinedButton(
                onClick = onViewClick
            ) {
                Text("Xem chi tiết")
            }
        }
    }
}

