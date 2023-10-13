package com.mac.allomalar.internet

import com.mac.allomalar.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("alloma/{id}")
    suspend fun getAlloma(@Path("id") id: Int): Response<Alloma>

    @GET("alloma/{id}/subject/")
    suspend fun geAllSubjectsOfAlloma(@Path("id") id: Int): Response<List<Subject>>

    @GET("centuries/")
    suspend fun getCenturies(): Response<List<Century>>

    @GET("centuries/madrasas/")
    suspend fun getMadrasas(): Response<List<Madrasa>>

    @GET("centuries/madrasas/allomas")
    suspend fun getMadrasaAndAllomas(): Response<List<MadrasaAndAllomas>>

    @GET("books")
    suspend fun getAllBooks(): Response<List<Book>>

    @GET("science")
    suspend fun getScience(): Response<List<Science>>

    @GET("alloma/subject/{id}/subject-info/")
    suspend fun getSubjectInfo(@Path("id") id: Int): Response<List<SubjectInfo>>
}