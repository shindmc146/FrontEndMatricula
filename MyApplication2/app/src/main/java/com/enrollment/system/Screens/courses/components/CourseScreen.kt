package com.enrollment.system.Screens.courses.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Alert
import com.enrollment.system.Components.ConfirmDialog
import com.enrollment.system.Components.errorReader
import com.enrollment.system.Screens.enrollments.components.EnrollmentForm
import com.enrollment.system.domain.Course
import kotlinx.coroutines.launch

@Composable
fun CourseScreen(){
    var courses by remember { mutableStateOf<List<Course>>(emptyList())}
    var message by remember { mutableStateOf("") }
    var reload by remember { mutableStateOf(0) }

    var createCourse by remember { mutableStateOf(false) }
    var courseEdit by remember { mutableStateOf<Course?>(null) }
    var courseDelete by remember {mutableStateOf<Course?>(null)}

    var courseToCheck by remember {mutableStateOf<Course?>(null)}
    var courseToEnroll by remember {mutableStateOf<Course?>(null)}
    val scope = rememberCoroutineScope()

    LaunchedEffect(reload){
        try{
            courses = Api.service.getCourses()
        }catch(e: Exception){
            message = "Error al obtener los cursos" + e.message
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cursos disponibles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = {createCourse = true}) {
                Text(text = "Agregar Curso")
            }
        }

        Text(
            text = "Toque un curso para ver sus estudiantes matriculados",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Alert(text = message)

        for (c in courses){
            CourseCard(
                course = c,
                onClick = {courseToCheck = c},
                onEnroll = {courseToEnroll = c},
                onEdit = {courseEdit = c},
                onDelete = {courseDelete = c})
        }


        val courseTC = courseToCheck
        if (courseTC != null){
            CourseDetail(
                course = courseTC,
                close = {courseToCheck = null},)
        }

        val courseTE = courseToEnroll
        if (courseTE != null){
            EnrollmentForm(
                enrollment = null,
                initialCourse = courseTE,
                close = {courseToEnroll = null},
                save = {
                    courseToEnroll = null
                    message = "Se registro correctamente la matricula"
                    reload += 1
                }
            )
        }

        if (createCourse) {
            CourseForm(
                course = null,
                close = { createCourse = false },
                save = {
                    createCourse = false
                    message = "Se registro correctamente el curso"
                    reload += 1
                }
                )
        }

        val courseE = courseEdit
        if (courseE != null){
            CourseForm(
                course = courseE,
                close = {courseEdit = null},
                save = {
                    courseEdit = null
                    message = "Se actualizo correctamente el curso"
                    reload += 1
                }
            )
        }
        val courseD = courseDelete
        if (courseD != null){
            ConfirmDialog(message = "¿Está seguro que desea borrar el curso " + courseD.name + "?",
                confirm = {
                    courseDelete = null
                    scope.launch {
                        try {
                            val response = Api.service.deleteCourse(courseD.id)
                            if (response.isSuccessful){
                                message = "Se borró el curso"
                                reload += 1
                            }else{
                                message = errorReader(response)
                            }
                        }catch (e: Exception){
                            message = "Error al borrar el curso: " + e.message
                        }
                    }
                },
                cancel = {courseDelete = null}
            )
        }

    }
}