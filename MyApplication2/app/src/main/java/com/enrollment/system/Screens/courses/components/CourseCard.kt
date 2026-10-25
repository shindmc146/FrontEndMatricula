package com.enrollment.system.Screens.courses.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enrollment.system.Components.Red
import com.enrollment.system.domain.Course

@Composable
fun CourseCard(
    course: Course,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onEnroll: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clickable{ onClick()}){
        Column {
            if (course.picture != ""){
                AsyncImage(
                    model = course.picture,
                    contentDescription = "Imagen del curso",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
            }

            Text(text = course.code+ " - " + course.name, fontWeight = FontWeight.Bold)
            Text(text = "Creditos: " + course.credits, fontWeight = FontWeight.Bold)
            Text(text= "Grupo: " + course.state, fontWeight = FontWeight.Bold)
            Text(text = "Cupos: " + (course.maximumEnrollment - course.availableEnrollments) + "/" + course.maximumEnrollment)
            Text(text = "Descripcion: " + course.description)
            Text(text = "Estado: " + course.state, fontWeight = FontWeight.Bold)
            Text(text = "Horario: " + course.schedule)

            Row() {

                Button(onClick = onEnroll, enabled = course.state == "Disponible") {
                    Text(text = "Matricular")
                }

                TextButton(onClick = onEdit) {
                    Text(text = "Editar")
                }
                TextButton(onClick = onDelete) {
                    Text(text = "Borrar", color = Red)
                }
            }
        }
    }
}