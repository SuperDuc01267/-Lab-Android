package com.example.baitaplab.lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


data class Course(val id: Int, val title: String, val author: String)


sealed interface CourseUiState {
    data object Loading : CourseUiState
    data class Success(val list: List<Course>) : CourseUiState
    data class Error(val message: String) : CourseUiState
}

class Lab5UiStateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Lab5App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lab5App() {
    // Bước 5C — Quản lý State
    var uiState by remember { mutableStateOf<CourseUiState>(CourseUiState.Loading) }
    var reloadKey by remember { mutableStateOf(0) }


    val sampleCourses = listOf(
        Course(1, "Lập trình Android với Kotlin", "ThS. Nguyễn Quốc Dũng"),
        Course(2, "Lập trình Jetpack Compose cơ bản", "Google Developer"),
        Course(3, "Thiết kế UI/UX cho ứng dụng Mobile", "Design Team")
    )

    // Bước 5D — LaunchedEffect và delay
    LaunchedEffect(reloadKey) {
        uiState = CourseUiState.Loading
        delay(1200)


        uiState = if (reloadKey % 2 == 0) {
            CourseUiState.Success(sampleCourses)
        } else {
            CourseUiState.Error("Không thể kết nối đến máy chủ. Vui lòng thử lại!")
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("UI State — Khóa học") }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is CourseUiState.Loading -> LoadingContent(innerPadding)
            is CourseUiState.Success -> SuccessContent(innerPadding, state.list) { reloadKey++ }
            is CourseUiState.Error -> ErrorContent(innerPadding, state.message) { reloadKey++ }
        }
    }
}


@Composable
fun LoadingContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Đang tải...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun SuccessContent(
    paddingValues: PaddingValues,
    courses: List<Course>,
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = courses,
                key = { it.id }
            ) { course ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Giảng viên: ${course.author}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Button(
            onClick = onReload,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Tải lại")
        }
    }
}


@Composable
fun ErrorContent(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Thử lại")
        }
    }
}