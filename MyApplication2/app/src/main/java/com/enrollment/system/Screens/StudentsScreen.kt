package com.enrollment.system.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Alert
import com.enrollment.system.Components.ConfirmDialog
import com.enrollment.system.Components.Red
import com.enrollment.system.Components.errorReader
import com.enrollment.system.domain.Student
import kotlinx.coroutines.launch


@Composable
fun StudentScreen(){
    var students by remember { mutableStateOf<List<Student>>(emptyList())}
    var message by remember { mutableStateOf("") }
    var reload by remember { mutableStateOf(0) }

    var creatingStudent by remember { mutableStateOf(false) }
    var editStudent by remember { mutableStateOf<Student?>(null) }
    var deleteStudent by remember { mutableStateOf<Student?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(reload) {
        try {
            students = Api.service.getStudents();
        }catch(e: Exception){
            message = "Error al sacar los estudiantes " + e.message;
        }
    }


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Estudiantes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = {creatingStudent = true}) {
                Text(text = "Agregar Estudiante")
            }
        }

        Alert(text = message)

        if (students.isEmpty()){
            Text(text = "No hay estudiantes registrados")
        }

        for (s in students){
            Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = s.name)
                    }
                    TextButton(onClick = {editStudent = s}) {
                        Text(text = "Editar")
                    }
                    TextButton(onClick = {deleteStudent = s}) {
                        Text(text = "Borrar", color = Red)
                    }
                }
            }
        }
    }



    if (creatingStudent){
        StudentForm(student = null, close = {creatingStudent = false},
            save = {
                creatingStudent = false
                message = "Estudiante Creado"
                reload += 1
            })
    }

    val studentEdit = editStudent;

    if (studentEdit != null){
        StudentForm(
            student = studentEdit,
            close = {editStudent = null},
            save = {
                editStudent = null
                message = "Estudiante actualizado"
                reload += 1
            }
        )
    }

    val studentDelete = deleteStudent;
    if (studentDelete != null){
        ConfirmDialog(
            message = "¿Está seguro que desea borrar a " + studentDelete.name + "?",
            confirm = {
                deleteStudent = null
                scope.launch {
                    try {
                        val response = Api.service.deleteStudent(studentDelete.id)
                        if(response.isSuccessful){
                            message = "Se borró el estudiante"
                            reload += 1
                        }else{
                            message = errorReader(response)
                        }
                    }catch(e: Exception){
                        message = "Error al borrar estudiante" + e.message
                    }
                }
            },
            cancel = {deleteStudent = null}
        )
    }

}



@Composable
fun StudentForm(student: Student?, close: () -> Unit, save: () -> Unit){
    var studentName by remember {mutableStateOf(student?.name ?: "")}
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    AlertDialog (
        onDismissRequest = close,
        title = {Text(text = if(student == null) "Nuevo estudiante" else "Editar estudiante")},
        text = {

                OutlinedTextField(
                    value = studentName,
                    onValueChange = {text -> studentName = text},
                    label = {Text(text = "Nombre Completo")}
                )

                if (error != ""){
                    Text(text = error, color = Red, modifier = Modifier.padding(top = 8.dp))
                }

        },
        confirmButton = {
            Button(onClick = {
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



