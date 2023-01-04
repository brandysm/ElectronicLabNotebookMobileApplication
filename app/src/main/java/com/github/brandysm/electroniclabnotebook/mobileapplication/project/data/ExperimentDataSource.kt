package com.github.brandysm.electroniclabnotebook.mobileapplication.project.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ApplicationProgrammingInterfaceUtility
import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ElectronicLabNotebookBackendApplicationProgrammingInterface
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment
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

class ExperimentDataSource {

    fun getExperiments(projectId: Long): MutableList<Experiment> {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<List<Experiment>> =
            electronicLabNotebookBackendApplicationProgrammingInterface.getExperiments(
                ApplicationProgrammingInterfaceUtility.accessToken, projectId
            )
        val response: Response<List<Experiment>> = call.execute()

        val experimentList: MutableList<Experiment> = response.body()!!.toMutableList()
        return experimentList
    }

    fun addExperiment(projectId: Long, experiment: Experiment): Experiment {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<Experiment> =
            electronicLabNotebookBackendApplicationProgrammingInterface.addExperiment(
                ApplicationProgrammingInterfaceUtility.accessToken, projectId,
                experiment
            )
        val response: Response<Experiment> = call.execute()

        return response.body()!!
    }

    fun removeExperiment(projectId: Long, experimentId: Long) {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<ResponseBody> =
            electronicLabNotebookBackendApplicationProgrammingInterface.removeExperiment(
                ApplicationProgrammingInterfaceUtility.accessToken,
                projectId, experimentId
            )
        val response: Response<ResponseBody> = call.execute()
    }

    fun modifyExperiment(projectId: Long, experimentId: Long, experiment: Experiment): Experiment {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<Experiment> =
            electronicLabNotebookBackendApplicationProgrammingInterface.modifyExperiment(
                ApplicationProgrammingInterfaceUtility.accessToken,
                projectId, experimentId, experiment
            )
        val response: Response<Experiment> = call.execute()
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