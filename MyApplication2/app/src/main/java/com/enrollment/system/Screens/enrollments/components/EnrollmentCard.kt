package com.enrollment.system.Screens.enrollments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Components.Red
import com.enrollment.system.domain.Enrollment

@Composable
fun EnrollmentCard(enrollment: Enrollment, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = enrollment.studentName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Curso: " + enrollment.courseCode + " - " + enrollment.courseName)
            Text(text = "Fecha de matricula: " + enrollment.enrollmentDate)
            Text(text = "Estado: " + enrollment.state, fontWeight = FontWeight.Bold)


            Row(modifier = Modifier.padding(top = 12.dp)){
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