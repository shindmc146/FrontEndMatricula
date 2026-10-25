package com.enrollment.system.Screens.students.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Red
import com.enrollment.system.Components.errorReader
import com.enrollment.system.domain.Student
import kotlinx.coroutines.launch


@Composable
fun StudentForm(student: Student?, close: () -> Unit, save: () -> Unit){
    var studentName by remember {mutableStateOf(student?.name ?: "")}
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    AlertDialog (
        onDismissRequest = close,
        title = {Text(text = if(student == null) "Nuevo estudiante" else "Editar estudiante")},
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { text -> studentName = text },
                    label = { Text(text = "Nombre Completo") }
                )

                if (error != "") {
                    Text(text = error, color = Red, modifier = Modifier.padding(top = 8.dp))
                }
            }

        },
        confirmButton = {
            Button(onClick = {
                val regexLetters = Regex("^[\\p{L} ]+$")

                if (!studentName.trim().matches(regexLetters) || studentName == ""){
                    error = "Debe ingresar un nombre valido"
                }else {
                    val data = Student(
                        id = student?.id ?: 0, name = studentName
                    )


                    scope.launch {
                        try {
                            val response = if (student == null) {
                                Api.service.registerStudent(data)
                            } else {
                                Api.service.updateStudent(student.id, data)
                            }

                            if (response.isSuccessful) {
                                save();
                            } else {
                                error = errorReader(response = response)
                            }
                        } catch (e: Exception) {
                            error = "Error al guardar el estudiante: " + e.message
                        }
                    }
                }
            }) {
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