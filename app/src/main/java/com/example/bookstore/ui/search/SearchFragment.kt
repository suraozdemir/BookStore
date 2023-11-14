package com.example.bookstore.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.common.invisible
import com.example.bookstore.common.viewBinding
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.FragmentSearchBinding
import com.example.bookstore.ui.home.HomeState
import com.example.bookstore.ui.home.HomeViewModel
import com.example.bookstore.ui.signin.SigninViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    SearchAdapter.SearchProductListener {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by viewModels<SearchViewModel>()
    private val viewModelHome by viewModels<HomeViewModel>()
    private val viewModelSignin by viewModels<SigninViewModel>()
    private val searchAdapter by lazy { SearchAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rvSearch.adapter = searchAdapter

            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?): Boolean {
                    text?.let {
                        if (it.length > 3) {
                            viewModel.getProductSearch(it)
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    text?.let {
                        if (it.length > 3) {
                            viewModel.getProductSearch(it)
                        }
                    }
                    return true
                }
            })
        }
        observeData()
    }

    private fun observeData() {
        viewModel.searchState.observe(viewLifecycleOwner) { state ->

            when (state) {

                SearchState.Loading -> {
                    binding.progressBar2.visible()
                }

                is SearchState.Data -> {
                    searchAdapter.submitList(state.products)
                    binding.progressBar2.invisible()
                }

                is SearchState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.orEmpty(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar2.invisible()
                }
            }

        }

    }

    private fun cartObserver() {

        viewModelHome.homeState.observe(viewLifecycleOwner) {
            if (it is HomeState.Response) {
                binding.progressBar2.invisible()
                Toast.makeText(
                    requireContext(),
                    it.crud.message,
                    Toast.LENGTH_SHORT
                ).show()
            } else if (it is HomeState.Error) {
                binding.progressBar2.invisible()
                Toast.makeText(
                    requireContext(),
                    it.throwable.message.orEmpty(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragment2ToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onCartButtonClick(id: Int) {
        val addToCartRequest = AddToCartRequest(viewModelSignin.currentUser!!.uid, id)
        viewModelHome.addToCart(addToCartRequest)
        cartObserver()
    }

    override fun onFavButtonClick(product: ProductUI) {
        viewModel.addToFavorites(product)
    }

}