package com.github.brandysm.electroniclabnotebook.mobileapplication.biometric

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityBiometricBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.HomeActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.afterTextChanged
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.ui.LoggedInUserView
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.ui.LoginResult
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.ui.LoginViewModel
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.ui.LoginViewModelFactory

class BiometricActivity : AppCompatActivity() {

    val SHARED_PREFS_FILENAME = "biometric_prefs"
    val CIPHERTEXT_WRAPPER = "ciphertext_wrapper"

    private lateinit var binding: ActivityBiometricBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var cryptographyManager: CryptographyManager
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiometricBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = binding.username
        val password = binding.password
        val authorize = binding.authorize

        binding.cancel.setOnClickListener { finish() }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@BiometricActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            authorize.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@BiometricActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                showBiometricPromptForEncryption(loginResult.success)
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            authorize.setOnClickListener {
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun showBiometricPromptForEncryption(loginResult: LoggedInUserView) {
        val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKey = getString(R.string.secret_key)
            cryptographyManager = CryptographyManager()
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKey)
            val biometricPrompt =
                createBiometricPrompt(this, ::encryptAndStoreServerToken, loginResult)
            val promptInfo = createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun encryptAndStoreServerToken(
        authResult: BiometricPrompt.AuthenticationResult,
        loginResult: LoggedInUserView
    ) {
        authResult.cryptoObject?.cipher?.apply {
            loginResult.accessToken.let { token ->
                val encryptedServerTokenWrapper = cryptographyManager.encryptData(token, this)
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                    encryptedServerTokenWrapper,
                    applicationContext,
                    SHARED_PREFS_FILENAME,
                    Context.MODE_PRIVATE,
                    CIPHERTEXT_WRAPPER
                )
            }
        }
        //updateUiWithUser(loginResult)
        setResult(Activity.RESULT_OK)

        //Complete and destroy login activity once successful
        finish()
    }

    fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.prompt_info_title))
            setSubtitle(activity.getString(R.string.prompt_info_subtitle))
            setDescription(activity.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_password))
        }.build()

    fun createBiometricPrompt(
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult, loginResult: LoggedInUserView) -> Unit,
        loginResult: LoggedInUserView
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                processSuccess(result, loginResult)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        val openHomeActivityIntent: Intent = Intent(this, HomeActivity::class.java)
        startActivity(openHomeActivityIntent)
        //
        Toast.makeText(
            applicationContext,
            "$welcome",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}