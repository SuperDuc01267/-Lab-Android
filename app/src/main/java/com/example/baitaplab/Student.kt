package com.example.baitaplab

data class Student(
    val id: Int,
    val name: String,
    val major: String,
    val email: String = "",
    val phone: String = ""
)

val sampleStudents = listOf(
    Student(1, "Nguyễn Văn A", "Công nghệ thông tin", "anv@gmail.com", "0901111222"),
    Student(2, "Trần Thị B", "Thiết kế đồ họa", "btt@gmail.com", "0902222333"),
    Student(3, "Lê Văn C", "Quản trị kinh doanh", "clv@gmail.com", "0903333444"),
    Student(4, "Phạm Văn D", "An toàn thông tin", "dpv@gmail.com", "0904444555")
)

fun findStudentById(id: Int): Student? {
    return sampleStudents.find { it.id == id }
}