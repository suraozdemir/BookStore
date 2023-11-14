package com.example.bookstore.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.common.setCreditCardTextWatcher
import com.example.bookstore.common.setExpiryDateFilter
import com.example.bookstore.databinding.FragmentPaymentBinding
import com.example.bookstore.data.util.CardType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val viewModel by viewModels<PaymentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {

        with(binding) {

            btnGoSuccess.setOnClickListener {
                val owner = etName.text.toString()
                val number = etCardNumber.setCreditCardTextWatcher()
                val cvv = etCardCvv.text.toString()
                val date = etExpiryDate.setExpiryDateFilter()
                val address = etAdress.text.toString()

                if (checkInfos(
                        owner,
                        number.toString(),
                        cvv,
                        date.toString(),
                        address
                    )
                ) {
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToSuccessFragment())
                } else {
                    Toast.makeText(requireContext(), "Missing Fields !", Toast.LENGTH_SHORT).show()
                }

            }
        }

        viewModel.cardNumber.observe(viewLifecycleOwner) { value ->
            replacePlaceholder(value)
            checkCardType(value)
        }
    }

    private fun replacePlaceholder(value: String) {
        var placeholderString = "**** **** **** ****"

        value.forEach {
            val sb = StringBuilder(placeholderString)
            val firstIndex = placeholderString.indexOf("*")
            sb.setCharAt(firstIndex, it)
            placeholderString = sb.toString()
        }
        viewModel.cardNumberPlaceholder.postValue(placeholderString)
    }

    private fun checkCardType(value: String) {
        if (value.length < 4) {
            viewModel.cardType.value = CardType.NO_TYPE
            return
        }

        if (value.startsWith("4")) {
            viewModel.cardType.value = CardType.VISA_CARD
        } else {
            viewModel.cardType.value = CardType.MASTER_CARD
        }
    }

    private fun checkInfos(
        owner: String,
        number: String,
        cvv: String,
        date: String,
        address: String
    ): Boolean {

        val checkInfos = when {
            owner.isNullOrEmpty() -> false
            number.isNullOrEmpty() -> false
            date.isNullOrEmpty() -> false
            cvv.isNullOrEmpty() -> false
            address.isNullOrEmpty() -> false
            else -> true
        }
        return checkInfos
    }
}
