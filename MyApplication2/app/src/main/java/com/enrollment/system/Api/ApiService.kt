package com.enrollment.system.Api

import com.enrollment.system.domain.Course
import com.enrollment.system.domain.Enrollment
import com.enrollment.system.domain.EnrollmentData
import com.enrollment.system.domain.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @GET("students")
    suspend fun getStudents(): List<Student>

    @POST("registerStudent")
    suspend fun registerStudent(@Body student: Student): Response<Unit>

    @PUT("putStudent/{id}")
    suspend fun updateStudent(@Path("id") id: Int, @Body student: Student): Response<Unit>

    @DELETE("deleteStudent/{id}")
    suspend fun deleteStudent(@Path("id") id : Int): Response<Unit>


    @GET("courses")
    suspend fun getCourses(): List<Course>

    @POST("createCourse")
    suspend fun createCourse(@Body course: Course): Response<Unit>

    @PUT("putCourse/{id}")
    suspend fun updateCourse(@Path("id") id: Int, @Body course: Course): Response<Unit>

    @DELETE("deleteCourse/{id}")
    suspend fun deleteCourse(@Path("id") id : Int): Response<Unit>


    @GET("enrollments")
    suspend fun getEnrollments(): List<Enrollment>
    @POST("registerEnrollment")
    suspend fun createEnrollment(@Body enrollment: EnrollmentData): Response<Unit>
    @PUT("updateEnrollment/{id}")
    suspend fun updateEnrollment(@Path("id") id: Int, @Body enrollment: EnrollmentData): Response<Unit>
    @DELETE("deleteEnrollment/{id}")
    suspend fun deleteEnrollment(@Path("id") id: Int): Response<Unit>



}