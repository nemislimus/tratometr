package com.nemislimus.tratometr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nemislimus.tratometr.authorization.data.RetrofitNetworkClient
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.LoginRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.RefreshTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.RegistrationRequest
import com.nemislimus.tratometr.databinding.ActivityMainBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkClient = RetrofitNetworkClient()

        var accessToken = ""
        var refreshToken = ""

        val registrationRequest = RegistrationRequest("forothen@gmail.com", "12345678")
        val loginRequest = LoginRequest("forothen@gmail.com", "12345678")

        lifecycleScope.launch {
            val response = networkClient.doRequest(loginRequest) as AuthResponse

            delay(2000)
            accessToken = response.accessToken
            refreshToken = response.refreshToken
            binding.resultAuth.text = response.resultCode.toString()
            binding.accessToken.text = response.accessToken
            binding.refreshToken.text = response.refreshToken
            accessToken = response.accessToken
            refreshToken = response.refreshToken

            delay(2000)
            val refreshResponse =
                networkClient.doRequest(RefreshTokenRequest(refreshToken)) as RefreshTokenResponse
            binding.resulRefresh.text = refreshResponse.resultCode.toString()
            binding.refAccessToken.text = refreshResponse.accessToken
            binding.refRefreshToken.text = refreshResponse.refreshToken
            accessToken = refreshResponse.accessToken
            refreshToken = refreshResponse.refreshToken

            delay(2000)
            val checkResponse =
                networkClient.doRequest(CheckTokenRequest(accessToken)) as CheckTokenResponse
            binding.resultCheck.text = checkResponse.resultCode.toString()
            binding.refValue.text = checkResponse.isValid.toString()

            delay(1000)
            cancel()
        }
    }
}