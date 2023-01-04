package com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ApplicationProgrammingInterfaceUtility
import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ElectronicLabNotebookBackendApplicationProgrammingInterface
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ShareProjectDataSource {

    fun getOtherAccounts(): MutableList<Account> {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<List<Account>> =
            electronicLabNotebookBackendApplicationProgrammingInterface.getOtherAccounts(
                ApplicationProgrammingInterfaceUtility.accessToken
            )
        val response: Response<List<Account>> = call.execute()

        val otherAccountList: MutableList<Account> = response.body()!!.toMutableList()
        return otherAccountList
    }

    fun shareProjectWithAccount(projectId: Long, accountId: Long) {
        val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
            retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)

        val call: Call<Project> =
            electronicLabNotebookBackendApplicationProgrammingInterface.shareProjectWithAccount(
                ApplicationProgrammingInterfaceUtility.accessToken, projectId, accountId
            )
        val response: Response<Project> = call.execute()
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