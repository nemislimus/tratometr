package com.nemislimus.tratometr

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nemislimus.tratometr.authorization.data.RetrofitNetworkClient
import com.nemislimus.tratometr.authorization.data.dto.RegistrationRequest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = findViewById<TextView>(R.id.message)

        val networkClient = RetrofitNetworkClient()

        val registrationRequest = RegistrationRequest("forothen@gmail.com", "12345678")

        lifecycleScope.launch {
            val response = networkClient.doRequest(registrationRequest)
            message.text = response.resultCode.toString()
        }
    }
}