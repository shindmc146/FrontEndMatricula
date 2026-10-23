package com.enrollment.system.domain

data class Course(
    val id: Int,
    val code: String,
    val name: String,
    val description: String,
    val credits: Int,
    val picture: String,
    val group: String,
    val maximumEnrollment: Int,
    val state: String,
    val schedule: String
)