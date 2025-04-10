package com.nemislimus.tratometr.authorization.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.nemislimus.tratometr.R
import com.nemislimus.tratometr.authorization.ui.viewmodel.SplashViewModel
import com.nemislimus.tratometr.authorization.ui.viewmodel.SplashViewModel.Companion.ANIM_END_POINT
import com.nemislimus.tratometr.authorization.ui.viewmodel.SplashViewModel.Companion.ANIM_START_LOOP_POINT
import com.nemislimus.tratometr.authorization.ui.viewmodel.SplashViewModel.Companion.ANIM_START_POINT
import com.nemislimus.tratometr.common.appComponent
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashFragment : BindingFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var vmFactory: SplashViewModel.Factory
    private lateinit var viewModel: SplashViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(), vmFactory)[SplashViewModel::class]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lvLogoAnim.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                showLoopLogoAnimation()
            }
        })

        lifecycleScope.launch {
            delay(4000)
            if (!viewModel.checkAccessToken()!!) {
                findNavController().navigate(R.id.action_splashFragment_to_authorizationFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_expensesFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isDarkMode().observe(viewLifecycleOwner) { isDarkMode ->
            showStartLogoAnimation(isDarkMode)
        }
    }

    override fun onDestroyFragment() {
        super.onDestroyFragment()
        binding.lvLogoAnim.removeAllAnimatorListeners()
    }

    private fun showStartLogoAnimation(isDarkMode: Boolean) {
        binding.apply {
            if (isDarkMode) {
                lvLogoAnim.setAnimation(R.raw.logo_anim_dark_2)
            } else {
                lvLogoAnim.setAnimation(R.raw.logo_anim_light_2)
            }

            lvLogoAnim.repeatCount = 0
            lvLogoAnim.setMinAndMaxProgress(ANIM_START_POINT, ANIM_START_LOOP_POINT)
            lvLogoAnim.playAnimation()
        }
    }

    private fun showLoopLogoAnimation() {
        binding.lvLogoAnim.setMinAndMaxProgress(ANIM_START_LOOP_POINT, ANIM_END_POINT)
        binding.lvLogoAnim.repeatMode = LottieDrawable.RESTART
        binding.lvLogoAnim.playAnimation()
    }

    // Определяем тему для viewModel. Потом заменить на значение темы из Settings репозитория
    private fun isDarkModeChecking(): Boolean =
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

}