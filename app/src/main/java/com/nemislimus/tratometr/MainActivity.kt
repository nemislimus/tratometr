package com.nemislimus.tratometr

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nemislimus.tratometr.authorization.data.RetrofitNetworkClient
import com.nemislimus.tratometr.authorization.data.dto.AuthResponse
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenRequest
import com.nemislimus.tratometr.authorization.data.dto.CheckTokenResponse
import com.nemislimus.tratometr.authorization.data.dto.LoginRequest
import com.nemislimus.tratometr.authorization.data.dto.RegistrationRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = findViewById<TextView>(R.id.message)

        val networkClient = RetrofitNetworkClient()

        var token = ""

        val registrationRequest = RegistrationRequest("forothen@gmail.com", "12345678")
        val loginRequest = LoginRequest("forothen@gmail.com", "12345678")

        lifecycleScope.launch {
            val response = networkClient.doRequest(loginRequest) as AuthResponse
            message.text = response.resultCode.toString()

            delay(2000)
            token = response.accessToken
            message.text = token
            Log.d("Token", token)

            delay(2000)
            val checkResponse = networkClient.doRequest(CheckTokenRequest(token)) as CheckTokenResponse
            message.text = "${checkResponse.isValid} ${checkResponse.resultCode}"
        }
    }
}