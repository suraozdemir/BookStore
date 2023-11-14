package com.example.bookstore.ui.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.data.util.CardType

class PaymentViewModel : ViewModel() {
    val cardNumber = MutableLiveData("")
    val cardNumberPlaceholder = MutableLiveData("**** **** **** ****")
    val cardType = MutableLiveData(CardType.NO_TYPE)
}