package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ApplicationProgrammingInterfaceUtility
import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ElectronicLabNotebookBackendApplicationProgrammingInterface
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class TodoDataSource {

    fun getTodos(): MutableList<Todo> {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<List<Todo>> =
            electronicLabNotebookBackendApplicationProgrammingInterface.getTodos(
                ApplicationProgrammingInterfaceUtility.accessToken
            )
        val response: Response<List<Todo>> = call.execute()

        val todoList: MutableList<Todo> = response.body()!!.toMutableList()
        return todoList
    }

    fun addTodo(todo: Todo): Todo {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call : Call<Todo> = electronicLabNotebookBackendApplicationProgrammingInterface.addTodo(ApplicationProgrammingInterfaceUtility.accessToken,
        todo)
        val response: Response<Todo> = call.execute()

        return response.body()!!
    }

    fun removeTodo(id: Long) {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<ResponseBody> = electronicLabNotebookBackendApplicationProgrammingInterface.removeTodo(ApplicationProgrammingInterfaceUtility.accessToken,
            id)
        val response: Response<ResponseBody> = call.execute()
    }

    fun modifyTodo(id: Long, todo: Todo): Todo {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<Todo> = electronicLabNotebookBackendApplicationProgrammingInterface.modifyTodo(ApplicationProgrammingInterfaceUtility.accessToken,
            id, todo)
        val response: Response<Todo> = call.execute()
        return response.body()!!
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()
    }
}