package com.enrollment.system.Screens.enrollments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Alert
import com.enrollment.system.Components.ConfirmDialog
import com.enrollment.system.Components.errorReader
import com.enrollment.system.domain.Course
import com.enrollment.system.domain.Enrollment
import kotlinx.coroutines.launch

@Composable
fun EnrollmentScreen() {
    var enrollments by remember { mutableStateOf<List<Enrollment>>(emptyList()) }
    var message by remember { mutableStateOf("") }
    var reload by remember { mutableStateOf(0) }

    var createEnrollment by remember { mutableStateOf(false) }
    var enrollmentEdit by remember { mutableStateOf<Enrollment?>(null) }
    var enrollmentDelete by remember { mutableStateOf<Enrollment?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(reload) {
        try {
            enrollments = Api.service.getEnrollments()
        } catch (e: Exception) {
            message = "Error al obtener las matriculas" + e.message
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = "Matriculas registradas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Button(onClick = { createEnrollment = true }) {
                Text(text = "Realizar matricula")
            }
        }

        Alert(text = message)

        for (e in enrollments) {
            EnrollmentCard(
                enrollment = e,
                onEdit = { enrollmentEdit = e },
                onDelete = { enrollmentDelete = e }
            )
        }

        if (createEnrollment) {
            EnrollmentForm(
                enrollment = null,
                initialCourse = null,
                close = { createEnrollment = false },
                save = {
                    createEnrollment = false
                    message = "Se registro correctamente la matricula"
                    reload += 1
                }
            )
        }

        val enrollmentE = enrollmentEdit
        if (enrollmentE != null) {
            EnrollmentForm(
                enrollment = enrollmentE,
                initialCourse = null,
                close = { enrollmentEdit = null },
                save = {
                    enrollmentEdit = null
                    message = "Se actualizo la matricula correctamente"
                    reload += 1
                }
            )
        }

        val enrollmentD = enrollmentDelete
        if (enrollmentD != null) {
            ConfirmDialog(
                message = "¿Está seguro que desea desmatricular a " + enrollmentD.studentName +
                        " del curso " + enrollmentD.courseName,
                confirm = {
                    enrollmentDelete = null
                    scope.launch {
                        try {
                            val response = Api.service.deleteEnrollment(enrollmentD.id)
                            if (response.isSuccessful) {
                                message = "Se borró la matrícula"
                                reload += 1
                            } else {
                                message = errorReader(response)
                            }
                        } catch (e: Exception) {
                            message = "Error al borrar la matricula: " + e.message
                        }
                    }
                },
                cancel = { enrollmentDelete = null }
            )
        }

    }

}


fun courseText(course: Course): String {

    var availableEnrollment = course.availableEnrollments.toString() + " cupos"

    if (course.state == "Completo") {
        availableEnrollment = "Cupo lleno"
    }
    if (course.state == "Cerrado") {
        availableEnrollment = "Cerrado"
    }
    return course.code + " - " + course.name + " (" + availableEnrollment + ")"
}