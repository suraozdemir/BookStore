package com.example.bookstore.ui.cart

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.common.gone
import com.example.bookstore.common.visible
import com.example.bookstore.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.ProductListener {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartAdapter(this) }
    private val viewModel by viewModels<CartViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartProducts(auth.currentUser?.uid.toString())

        observeData()

        binding.rvCartProducts.adapter = cartAdapter

        binding.btnClear.setOnClickListener {
            viewModel.clearCart(auth.currentUser?.uid.toString())
        }
        payButtonClick()
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CartState.Data -> {
                    rvCartProducts.visible()
                    cartAdapter.submitList(state.products)
                }

                is CartState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    rvCartProducts.gone()
                }

                is CartState.DeleteFromCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }

                is CartState.ClearCart -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
            }
        }
        viewModel.totalPriceAmount.observe(viewLifecycleOwner) { price ->
            val formattedPrice = String.format("$ %.2f", price)
            val editableText = Editable.Factory.getInstance().newEditable(formattedPrice)
            tvTotalprice.text = editableText
        }
    }

    override fun onCartItemClick(id: Int) {
        val action = CartFragmentDirections.actionCartFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDeleteItemClick(id: Int) {
        viewModel.deleteFromCart(id)
    }

    override fun onIncreaseClick(price: Double?) {
        TODO("Not yet implemented")
    }

    override fun onDecreaseClick(price: Double?) {
        TODO("Not yet implemented")
    }

    override fun onClearCart(userId: String) {
        viewModel.clearCart(userId)
    }


    private fun payButtonClick() {
        binding.btnPayment.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToPaymentFragment()
            findNavController().navigate(action)
        }
    }
}