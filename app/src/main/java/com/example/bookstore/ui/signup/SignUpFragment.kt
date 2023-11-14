package com.example.bookstore.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.common.gone
import com.example.bookstore.common.viewBinding
import com.example.bookstore.common.visible
import com.example.bookstore.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val viewModel: SignUpViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSignup.setOnClickListener {
                viewModel.signUp(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
        initObserves()
    }

    private fun initObserves() {
        with(binding) {
            viewModel.signUpState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    SignUpState.Loading -> progressBar4.visible()
                    is SignUpState.GoToHome -> {
                        progressBar4.gone()
                        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment2)
                    }

                    else -> {
                        progressBar4.gone()
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}