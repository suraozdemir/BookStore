package com.example.bookstore.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.common.gone
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.ProductListener, SaleProductsAdapter.ProductListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeAdapter by lazy { HomeAdapter(this) }
    private val saleAdapter by lazy { SaleProductsAdapter(this) }
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllProducts.adapter = homeAdapter
        viewModel.getProducts()

        binding.rvSaleProducts.adapter = saleAdapter
        viewModel.getSaleProducts()
        observeData()

        logOut()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragment2ToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onHomeFavoriteButtonClick(product: ProductUI) {
        if (product.isFavorite) {
            viewModel.deleteFromFavorites(product)
        } else {
            viewModel.addToFavorites(product)
        }
    }

    override fun onFavoriteButtonClick(product: ProductUI) {
        if (product.isFavorite) {
            viewModel.deleteFromFavorites(product)
        } else {
            viewModel.addToFavorites(product)
        }
    }

    override fun onAddCartButtonClick(id: Int) {
        val addToCartRequest = AddToCartRequest(auth.currentUser!!.uid, id)
        viewModel.addToCart(addToCartRequest)
    }

    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Data -> {
                    homeAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }

                is HomeState.SaleData -> {
                    saleAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }

                is HomeState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    binding.progressBar.gone()
                }

                is HomeState.Loading -> {
                    binding.progressBar.visible()
                    val delayMillis: Long = 2000 // 2 saniye (2000 milisaniye)
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.gone()
                    }, delayMillis)
                }

                else -> {

                }
            }
        }
    }

    override fun onSaleClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragment2ToDetailFragment(id)
        findNavController().navigate(action)
    }

    private fun logOut() {

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val action = HomeFragmentDirections.actionHomeFragment2ToSigninFragment()
            findNavController().navigate(action)
        }
        false
    }
}
