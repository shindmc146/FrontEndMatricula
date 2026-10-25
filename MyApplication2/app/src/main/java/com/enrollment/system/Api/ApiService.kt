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
    @POST("students")
    suspend fun registerStudent(@Body student: Student): Response<Unit>
    @PUT("students/{id}")
    suspend fun updateStudent(@Path("id") id: Int, @Body student: Student): Response<Unit>
    @DELETE("students/{id}")
    suspend fun deleteStudent(@Path("id") id : Int): Response<Unit>

    @GET("courses")
    suspend fun getCourses(): List<Course>
    @GET("courses/{id}/students")
    suspend fun getStudentsByCourse(@Path("id") id: Int): List<Student>
    @POST("courses")
    suspend fun createCourse(@Body course: Course): Response<Unit>
    @PUT("courses/{id}")
    suspend fun updateCourse(@Path("id") id: Int, @Body course: Course): Response<Unit>
    @DELETE("courses/{id}")
    suspend fun deleteCourse(@Path("id") id : Int): Response<Unit>

    @GET("enrollments")
    suspend fun getEnrollments(): List<Enrollment>
    @POST("enrollments")
    suspend fun createEnrollment(@Body enrollment: EnrollmentData): Response<Unit>
    @PUT("enrollments/{id}")
    suspend fun updateEnrollment(@Path("id") id: Int, @Body enrollment: EnrollmentData): Response<Unit>
    @DELETE("enrollments/{id}")
    suspend fun deleteEnrollment(@Path("id") id: Int): Response<Unit>

}