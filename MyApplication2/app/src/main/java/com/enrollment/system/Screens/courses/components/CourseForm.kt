package com.enrollment.system.Screens.courses.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Red
import com.enrollment.system.Components.Select
import com.enrollment.system.domain.Course
import kotlinx.coroutines.launch

@Composable
fun CourseForm(course: Course?, close: () -> Unit, save: () -> Unit){
    var courseCode by remember {mutableStateOf(course?.code ?: "")}
    var courseName by remember {mutableStateOf(course?.name ?: "")}
    var courseDescription by remember {mutableStateOf(course?.description ?: "")}
    var courseCredits by remember {mutableStateOf(if (course?.credits != null) course.credits.toString() else "")}
    var coursePicture by remember {mutableStateOf(course?.picture ?: "")}
    var courseGroup by remember {mutableStateOf(course?.group ?: "")}
    var courseMaximumEnrollment by remember {mutableStateOf(if (course?.maximumEnrollment != null) course.maximumEnrollment.toString() else "")}
    var courseState by remember {mutableStateOf(course?.state ?: "")}
    var courseSchedule by remember {mutableStateOf(course?.schedule ?: "")}


    val courseStates = listOf<String>("Disponible", "Cerrado")
    val schedules = listOf<String>("Mañana", "Tarde", "Noche")

    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val numPad = KeyboardOptions(keyboardType = KeyboardType.Number)


    AlertDialog(onDismissRequest = close,
        title = {
            Text(text = if (course == null) "Nuevo curso" else "Editar curso")
        },
        text = {

            Column(modifier = Modifier.verticalScroll(rememberScrollState())){
                OutlinedTextField(
                    value = courseName,
                    onValueChange = {text -> courseName = text},
                    label = { Text(text = "Nombre") },
                    maxLines = 1
                )
                OutlinedTextField(
                    value = courseCode,
                    onValueChange = {text -> courseCode = text},
                    label = { Text(text = "Codigo") },
                    maxLines = 1
                )

                OutlinedTextField(
                    value = courseDescription,
                    onValueChange = {text -> courseDescription = text},
                    label = { Text(text = "Descripcion") },
                    maxLines = 5
                )

                OutlinedTextField(
                    value = courseCredits,
                    onValueChange = {text -> courseCredits = text},
                    label = { Text(text = "Creditos") },
                    keyboardOptions = numPad,
                    maxLines = 1,
                )

                OutlinedTextField(
                    value = courseGroup,
                    onValueChange = {text -> courseGroup = text},
                    label = { Text(text = "Grupo") },
                    maxLines = 1,
                )

                if (course != null){
                    Select(
                        title = "Estado",
                        text = courseState,
                        options = courseStates,
                        whenClick = { position -> courseState = courseStates[position]}
                    )
                }

                OutlinedTextField(
                    value = courseMaximumEnrollment,
                    onValueChange = {text -> courseMaximumEnrollment = text},
                    label = { Text(text = "Cupos maximos") },
                    keyboardOptions = numPad,
                    maxLines = 1
                )

                OutlinedTextField(
                    value = coursePicture,
                    onValueChange = {text -> coursePicture = text},
                    label = { Text(text = "URL de la imagen") },
                )


                Select(
                    title = "Horario",
                    text = courseSchedule,
                    options = schedules,
                    whenClick = { position -> courseSchedule = schedules[position]}
                )
            }


            if (error != ""){
                Text(text = error, color = Red, modifier = Modifier.padding(bottom = 30.dp))
            }
        },
        confirmButton = {
            Button(onClick = {

                val regexNums = Regex("^[0-9]*$")
                val regexLetters = Regex("^[a-zA-Z ]*$")
                val regex = Regex("^[a-zA-Z0-9]*$")

                if (!courseName.trim().matches(regexLetters) || courseName == ""){
                    error = "Debe ingresar un nombre valido"
                }else if (!courseCode.matches(regex) || courseCode == ""){
                    error = "Debe ingresar un codigo valido"
                }else if (!courseDescription.trim().matches(regexLetters) || courseDescription == "") {
                    error = "Debe ingresar una descripcion valida"
                }else if(!courseCredits.matches(regexNums) || courseCredits == ""){
                    error = "Debe ingresar creditos validos"
                }else if (!courseGroup.matches(regex) || courseGroup == ""){
                    error = "Debe ingresar un grupo valido"
                }else if (course != null && courseState == ""){
                    error = "Debe seleccionar un estado valido"
                }else if (!courseMaximumEnrollment.matches(regexNums) || courseMaximumEnrollment == ""){
                    error = "Debe ingresar un numero de cupos valido"
                }else if (coursePicture == ""){
                    error = "Debe ingresar una URL de imagen"
                }else if (courseSchedule == ""){
                    error = "Debe seleccionar un horario valido"
                }
                else {
                    val data = Course(
                        course?.id ?: 0,
                        courseCode, courseName,
                        courseDescription,
                        courseCredits.toInt(),
                        coursePicture,
                        courseGroup,
                        courseMaximumEnrollment.toInt(),
                        0,
                        courseState,
                        courseSchedule)

                    scope.launch {

                        try {
                            var response = if (course == null){
                                Api.service.createCourse(data)
                            }else{
                                Api.service.updateCourse(course.id, data)
                            }
                        }catch (e: Exception){
                            error = "Error al guardar el curso" + e.message
                        }
                    }
                    save()
                }
            }){
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = close) {
                Text("Cancelar")
            }
        }
    )
}