package com.nemislimus.tratometr.common

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nemislimus.tratometr.authorization.data.AuthRepositoryImpl
import com.nemislimus.tratometr.authorization.data.RetrofitNetworkClient
import com.nemislimus.tratometr.authorization.domain.AuthInteractor
import com.nemislimus.tratometr.authorization.domain.impl.AuthInteractorImpl
import com.nemislimus.tratometr.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = RetrofitNetworkClient(this)
        val repository = AuthRepositoryImpl(client)
        val interactor = AuthInteractorImpl(repository)

        lifecycleScope.launch {
            var tokens = interactor.login("forothen@gmail.com", "12345678")
            //tokens = interactor.refresh(tokens.refreshToken!!)
            Log.d("Токен", tokens.message)
        }
    }
}
