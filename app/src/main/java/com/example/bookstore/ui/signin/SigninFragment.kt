package com.example.bookstore.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.common.viewBinding
import com.example.bookstore.databinding.FragmentSigninBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SigninFragment : Fragment(R.layout.fragment_signin) {
    private val binding by viewBinding(FragmentSigninBinding::bind)
    private val viewModel by viewModels<SigninViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.let {
            findNavController().navigate(R.id.action_signinFragment_to_homeFragment2)
        }

        with(binding) {
            btnSignin.setOnClickListener {
                viewModel.signIn(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
            tvSignup.setOnClickListener {
                findNavController().navigate(R.id.action_signinFragment_to_signUpFragment)
            }
        }
        initObserves()
    }

    private fun initObserves() {
        with(binding) {
            viewModel.signInState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is SignInState.GoToHome -> {
                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment2)
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

