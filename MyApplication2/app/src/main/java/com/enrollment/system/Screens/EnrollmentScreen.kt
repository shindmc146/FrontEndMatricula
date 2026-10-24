package com.enrollment.system.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enrollment.system.Api.Api
import com.enrollment.system.Components.Alert
import com.enrollment.system.Components.ConfirmDialog
import com.enrollment.system.Components.Gray
import com.enrollment.system.Components.Green
import com.enrollment.system.Components.Red
import com.enrollment.system.Components.errorReader
import com.enrollment.system.domain.Course
import com.enrollment.system.domain.Enrollment
import com.enrollment.system.domain.Student
import kotlinx.coroutines.launch

@Composable
fun EnrollmentScreen(){
    var enrollments by remember {mutableStateOf<List<Enrollment>>(emptyList())}
    var message by remember { mutableStateOf("") }
    var reload by remember { mutableStateOf(0) }

    var createEnrollment by remember { mutableStateOf(false) }
    var enrollmentEdit by remember { mutableStateOf<Enrollment?>(null) }
    var enrollmentDelete by remember { mutableStateOf<Enrollment?>(null) }

    val scope= rememberCoroutineScope()

    LaunchedEffect(reload) {
        try{
            enrollments= Api.service.getEnrollments()
        }catch (e: Exception){
            message="Error al obtener las matriculas" + e.message
        }
    }

    Column(modifier=Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier=Modifier.padding(top=12.dp)){
            Text(text="Matriculas registradas",
                fontSize=20.sp,
                fontWeight= FontWeight.Bold,
                modifier=Modifier.weight(1f)
            )
            Button(onClick={createEnrollment=true}) {
                Text(text="Realizar matricula")
            }
        }

        Alert(text=message)

        for ( e in enrollments){
            EnrollmentCard(
                enrollment=e,
                onEdit={enrollmentEdit=e},
                onDelete={enrollmentDelete=e}
            )
        }

        if (createEnrollment){
            EnrollmentForm(
                enrollment=null,
                initialCourse=null,
                close={createEnrollment=false},
                save={
                    createEnrollment=false
                    message="Se registro correctamente la matricula"
                    reload+=1
                }
            )
        }

        val enrollmentE=enrollmentEdit
        if(enrollmentE!=null){
            EnrollmentForm(
                enrollment=enrollmentE,
                initialCourse=null,
                close={enrollmentEdit=null},
                save={
                    enrollmentEdit=null
                    message="Se actualizo la matricula correctamente"
                    reload+=1
                }
            )
        }

        val enrollmentD=enrollmentDelete
        if(enrollmentD!=null){
            ConfirmDialog(
                message="¿Está seguro que desea desmatricular a "+ enrollmentD.studentName +
                " del curso "+ enrollmentD.courseName,
                confirm = {
                    enrollmentDelete=null
                    scope.launch {
                        try{
                            val response=Api.service.deleteEnrollment(enrollmentD.id)
                           if(response.isSuccessful) {
                               message = "Se borró la matrícula"
                               reload += 1
                           }else{
                               message= errorReader(response)
                           }
                        }catch (e: Exception){
                            message="Error de conexión: " + e.message
                        }
                    }
                },
                cancel={ enrollmentDelete=null }
            )
        }

    }

}

@Composable
fun EnrollmentCard(enrollment: Enrollment, onEdit: ()-> Unit, onDelete: ()-> Unit){
    Card(modifier = Modifier.fillMaxWidth().padding(top=12.dp)) {
        Column(modifier=Modifier.padding(16.dp)) {
            Text(text=enrollment.studentName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text="Curso: "+enrollment.courseCode+" - "+ enrollment.courseName)
            Text(text="Fecha de matricula: "+ enrollment.enrollmentDate)

            if(enrollment.state){
                Text(text="Estado: Activa", color = Green, fontWeight = FontWeight.Bold)
            }else{
                Text(text="Estado: Retirada", color= Gray, fontWeight = FontWeight.Bold)
            }

            Row(modifier=Modifier.padding(top=4.dp)){
                TextButton(onClick=onEdit) {
                    Text(text="Editar")
                }
                TextButton(onClick=onDelete){
                    Text(text="Borrar", color= Red)
                }
            }
        }
    }
}

@Composable
fun EnrollmentForm(enrollment: Enrollment?, initialCourse: Course?, close: ()-> Unit, save: ()-> Unit){

    var students by remember { mutableStateOf<List<Student>>(emptyList()) }
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var state by remember {mutableStateOf(if (enrollment==null) true else enrollment.state)}
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try{
            students= Api.service.getStudents()
            courses=Api.service.getCourses()

            if(enrollment!=null){
                for(s in students){
                    if(s.id==enrollment.studentId){
                        selectedStudent=s
                    }
                }
                for(c in courses){
                    if(c.id==enrollment.courseId){
                        selectedCourse=c
                    }
                }
            }
        }catch (e: Exception){
            error="No se pudieron cargar las listas"+ e.message
        }
    }

    val studentsName = mutableListOf<String>()
    for(e in students){
        studentsName.add(e.name)
    }

    val coursesName = mutableListOf<String>()
    for (c in courses){
        coursesName.add(courseText(c))
    }

    var studentText="Seleccione un estudiante"
    val currentStudent= selectedStudent
    if(currentStudent!=null){
        studentText=currentStudent.name
    }

    var courseText ="Seleccione un curso"
    val currentCourse=selectedCourse
    if(currentCourse!=null){
        courseText=currentCourse.code+" - "+currentCourse.name
    }

    val stateOptions = listOf("Activa", "Retirada")


}


fun courseText(course: Course): String{

    var detail= course.availableEnrollments.toString() + " cupos"

    if(course.state=="Completo"){
        detail="Cupo lleno"
    }
    if(course.state=="Cerrado"){
        detail="Cerrado"
    }
    return course.code+ " - " + course.name + " ("+detail+")"
}