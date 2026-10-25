package com.enrollment.system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Components.ButtonMenu
import com.enrollment.system.Screens.courses.components.CourseScreen
import com.enrollment.system.Screens.enrollments.components.EnrollmentScreen
import com.enrollment.system.Screens.students.components.StudentScreen
import com.enrollment.system.ui.theme.MyApplicationTheme


val appColors = lightColorScheme(primary = Color(0xFF128FF4))


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = appColors) {
                Surface(modifier = Modifier.fillMaxWidth()) {
                    EnrollmentApp()
                }
            }
        }
    }
}

@Composable
fun EnrollmentApp(){
    var section by remember { mutableStateOf("courses") }

    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)
    ){
        Text(
            text = "Sistema de Matricula",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            ButtonMenu(
                text = "Cursos",
                selected = section == "courses",
                onClick = {section = "courses"},
                modifier = Modifier.weight(1f)
            )
            ButtonMenu(
                text = "Matriculas",
                selected = section == "enrollments",
                onClick = {section = "enrollments"},
                modifier = Modifier.weight(1f)
            )
            ButtonMenu(
                text = "Estudiantes",
                selected = section == "students",
                onClick = {section = "students"},
                modifier = Modifier.weight(1f)
            )

        }

        when(section) {
            "courses" -> CourseScreen()
            "enrollments" -> EnrollmentScreen()
            "students" -> StudentScreen()
        }

    }


}






