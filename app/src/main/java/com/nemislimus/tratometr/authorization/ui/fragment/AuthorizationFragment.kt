package com.nemislimus.tratometr.authorization.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.authorization.domain.models.Resource
import com.nemislimus.tratometr.authorization.ui.viewmodel.AuthorizationViewModel
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentAuthorizationBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationFragment : BindingFragment<FragmentAuthorizationBinding>() {

    @Inject
    lateinit var vmFactory: AuthorizationViewModel.Factory
    private lateinit var viewModel: AuthorizationViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAuthorizationBinding {
        return FragmentAuthorizationBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(), vmFactory)[AuthorizationViewModel::class]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()

            lifecycleScope.launch {
                Log.d(email, password)
                val response = viewModel.authorization(email, password)
                when (response) {
                    is Resource.Success -> {
                        viewModel.putTokensToStorage(response.value!!)
                        findNavController().navigate(R.id.action_authorizationFragment_to_expensesFragment)
                    }

                    is Resource.Error -> {
                        binding.wrongEmail.text = response.message
                        binding.wrongEmail.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private suspend fun login(email: String, password: String) {
        viewModel.authorization(email, password)
    }
}