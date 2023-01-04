package com.github.brandysm.electroniclabnotebook.mobileapplication.login.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ApplicationProgrammingInterfaceUtility
import com.github.brandysm.electroniclabnotebook.mobileapplication.applicationprogramminginterface.ElectronicLabNotebookBackendApplicationProgrammingInterface
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.data.model.LoggedInUser
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val retrofit = Retrofit.Builder().baseUrl(ApplicationProgrammingInterfaceUtility.baseUrl)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create()).build()
            val electronicLabNotebookBackendApplicationProgrammingInterface: ElectronicLabNotebookBackendApplicationProgrammingInterface =
                retrofit.create(ElectronicLabNotebookBackendApplicationProgrammingInterface::class.java)
            val call: Call<LoggedInUser> =
                electronicLabNotebookBackendApplicationProgrammingInterface.login(
                    username,
                    password
                )
            val response: Response<LoggedInUser> = call.execute();
            //
            var fakeUser: LoggedInUser = LoggedInUser(
                java.util.UUID.randomUUID().toString(),
                "Jane Doe",
                response.body()!!.accessToken,
                response.body()!!.refreshToken
            )
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
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