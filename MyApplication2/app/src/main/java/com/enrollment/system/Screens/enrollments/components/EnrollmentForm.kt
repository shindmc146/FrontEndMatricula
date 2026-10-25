package com.enrollment.system.Screens.enrollments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Red
import com.enrollment.system.Components.Select
import com.enrollment.system.Components.errorReader
import com.enrollment.system.domain.Course
import com.enrollment.system.domain.Enrollment
import com.enrollment.system.domain.EnrollmentData
import com.enrollment.system.domain.Student
import kotlinx.coroutines.launch

@Composable
fun EnrollmentForm(
    enrollment: Enrollment?,
    initialCourse: Course?,
    close: () -> Unit,
    save: () -> Unit
) {

    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var selectedCourse by remember { mutableStateOf<Course?>(initialCourse) }
    var state by remember { mutableStateOf(enrollment?.state ?: "Activa") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            students = Api.service.getStudents()
            courses = Api.service.getCourses()

            if (enrollment != null) {
                for (s in students) {
                    if (s.id == enrollment.studentId) {
                        selectedStudent = s
                    }
                }
                for (c in courses) {
                    if (c.id == enrollment.courseId) {
                        selectedCourse = c
                    }
                }
            }
        } catch (e: Exception) {
            error = "No se pudieron cargar las listas" + e.message
        }
    }

    val studentsName = mutableListOf<String>()
    for (e in students) {
        studentsName.add(e.name)
    }

    val coursesName = mutableListOf<String>()
    for (c in courses) {
        coursesName.add(courseText(c))
    }

    var studentText = "Seleccione un estudiante"
    val currentStudent = selectedStudent
    if (currentStudent != null) {
        studentText = currentStudent.name
    }

    var courseText = "Seleccione un curso"
    val currentCourse = selectedCourse
    if (currentCourse != null) {
        courseText = currentCourse.code + " - " + currentCourse.name
    }

    val stateOptions = listOf("Activa", "Retirada")


    AlertDialog(
        onDismissRequest = close,
        title = { Text(text = if (enrollment == null) "Nueva matricula" else "Editar matricula") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Select(
                    title = "Estudiante",
                    text = studentText,
                    options = studentsName,
                    whenClick = { position -> selectedStudent = students[position] })
                if (initialCourse == null) {
                    Select(
                        title = "Curso",
                        text = courseText,
                        options = coursesName,
                        whenClick = { position -> selectedCourse = courses[position] }
                    )
                }else {
                    Select(
                        title = "Curso",
                        text = courseText,
                        options = null,
                        whenClick = null
                    )
                }


                if (enrollment != null) {
                    Select(
                        title = "Estado",
                        text = state,
                        options = stateOptions,
                        whenClick = { position ->
                            state = stateOptions[position]
                        })
                }


                if (error != "") {
                    Text(text = error, color = Red, modifier = Modifier.padding(top = 12.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val student = selectedStudent
                val course = selectedCourse

                if (student == null || course == null) {
                    error = "Debe seleccionar un estudiante y un curso"
                } else {
                    val data = EnrollmentData(studentId = student.id, courseId = course.id, state = state)


                    scope.launch {
                        try {
                            val response = if (enrollment == null) {
                                Api.service.createEnrollment(data)
                            } else {
                                Api.service.updateEnrollment(enrollment.id, data)
                            }

                            if (response.isSuccessful) {
                                save()
                            } else {
                                error = errorReader(response)
                            }
                        } catch (e: Exception) {
                            error = "Error al guardar la matricula: " + e.message

                        }
                    }
                }
            }

            ) {
                Text(text = "Guardar")
            }


        },
        dismissButton = {
            TextButton(onClick = close) {
                Text(text = "Cancelar")
            }
        }
    )
}