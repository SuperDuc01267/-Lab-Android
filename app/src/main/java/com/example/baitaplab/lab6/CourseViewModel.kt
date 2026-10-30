package com.example.baitaplab.lab6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baitaplab.lab5.Course
import com.example.baitaplab.lab5.CourseUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {


    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)

    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    private var loadCount = 0


    private val sampleCourses = listOf(
        Course(1, "Lập trình Android với Kotlin", "ThS. Nguyễn Quốc Dũng"),
        Course(2, "Lập trình Jetpack Compose nâng cao", "Google Developer"),
        Course(3, "Quản lý State với ViewModel & StateFlow", "Android Team")
    )

    init {
        load()
    }

    fun load() {
        val attempt = loadCount
        loadCount++

        viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            delay(1200)


            _uiState.value = if (attempt % 2 == 0) {
                CourseUiState.Success(sampleCourses)
            } else {
                CourseUiState.Error("Không tải được dữ liệu. Thử lại!")
            }
        }
    }
}