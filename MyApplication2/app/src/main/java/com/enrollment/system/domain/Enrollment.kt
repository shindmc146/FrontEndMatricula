package com.enrollment.system.domain

data class Enrollment (
    val id: Int,
    val studentId: Int,
    val courseId: Int,
    val studentName: String,
    val courseCode: String,
    val state: String,
    val enrollmentDate: String
)

