package com.enrollment.system.Screens.courses.components

import android.app.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Red
import com.enrollment.system.domain.Course
import com.enrollment.system.domain.Student

@Composable
fun CourseDetail(course: Course, close: () -> Unit) {
    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(course.id) {
        try {
            students = Api.service.getStudentsByCourse(course.id)
        } catch (e: Exception) {
            error = "Error al obtener los estudiantes: " + e.message
        }
        loading = false
    }

    AlertDialog(
        onDismissRequest = close,
        title = { Text(text = course.code + " - " + course.name) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = "Estudiantes matriculados: " + students.size + " de " + course.maximumEnrollment,
                    fontWeight = FontWeight.Bold
                )

                if (loading) {
                    Text("Cargando...")
                } else if (error != "") {
                    Text(error, color = Red)
                } else if (students.isEmpty()) {
                    Text("No hay estudiantes matriculados")
                } else {
                    for (s in students) {
                        Text(s.name, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = close) {
                Text("Cerrar")
            }
        }
    )
}