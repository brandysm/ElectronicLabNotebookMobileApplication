package com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface

import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.model.Document
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.data.model.LoggedInUser
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ElectronicLabNotebookBackendApplicationProgrammingInterface {
    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoggedInUser>

    @GET("/account")
    fun getAccounts(@Header("Authorization") accessToken: String): Call<List<Account>>

    @GET("/todo")
    fun getTodos(@Header("Authorization") accessToken: String): Call<List<Todo>>

    @POST("/todo")
    fun addTodo(@Header("Authorization") accessToken: String, @Body todo: Todo): Call<Todo>

    @DELETE("/todo/{id}")
    fun removeTodo(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long
    ): Call<ResponseBody>

    @PATCH("/todo/{id}")
    fun modifyTodo(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
        @Body todo: Todo
    ): Call<Todo>

    @GET("/project")
    fun getProjects(@Header("Authorization") accessToken: String): Call<List<Project>>

    @POST("/project")
    fun addProject(
        @Header("Authorization") accessToken: String,
        @Body project: Project
    ): Call<Project>

    @DELETE("/project/{id}")
    fun removeProject(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long
    ): Call<ResponseBody>

    @PATCH("/project/{id}")
    fun modifyProject(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Long,
        @Body project: Project
    ): Call<Project>

    @GET("/project/{projectId}/experiment")
    fun getExperiments(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long
    ): Call<List<Experiment>>

    @POST("/project/{projectId}/experiment")
    fun addExperiment(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Body experiment: Experiment
    ): Call<Experiment>

    @DELETE("/project/{projectId}/experiment/{experimentId}")
    fun removeExperiment(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long
    ): Call<ResponseBody>

    @PATCH("/project/{projectId}/experiment/{experimentId}")
    fun modifyExperiment(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long,
        @Body experiment: Experiment
    ): Call<Experiment>

    @GET("/project/{projectId}/experiment/{experimentId}/document")
    fun getDocuments(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long
    ): Call<List<Document>>

    @POST("/project/{projectId}/experiment/{experimentId}/document")
    fun addDocument(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long,
        @Body document: Document
    ): Call<Document>

    @DELETE("/project/{projectId}/experiment/{experimentId}/document/{documentId}")
    fun removeDocument(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long,
        @Path("documentId") documentId: Long
    ): Call<ResponseBody>

    @GET("/project/{projectId}/experiment/{experimentId}/document/{documentId}")
    fun getDocument(
        @Header("Authorization") accessToken: String,
        @Path("projectId") projectId: Long,
        @Path("experimentId") experimentId: Long,
        @Path("documentId") documentId: Long
    ): Call<Document>

    @GET("/account/other")
    fun getOtherAccounts(@Header("Authorization") accessToken: String): Call<List<Account>>

    @PUT("/project/{projectId}/account/{accountId}")
    fun shareProjectWithAccount(
        @Header("Authorization") accessToken: String, @Path("projectId") projectId: Long,
        @Path("accountId") accountId: Long,
    ): Call<Project>
}