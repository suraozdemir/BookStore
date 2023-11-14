package com.example.bookstore.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.common.invisible
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoritesAdapter.ProductListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModels<FavoritesViewModel>()
    private val favoriteProductsAdapter by lazy { FavoritesAdapter(this) }
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteProducts()

        observeData()

        with(binding) {
            rvFavorites.adapter = favoriteProductsAdapter

            btnClearFav.setOnClickListener {
                onClearClick()
            }
        }

    }

    private fun observeData() = with(binding) {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->

            when (state) {

                FavoriteState.Loading -> {
                    progressBar3.visible()
                }

                is FavoriteState.Data -> {
                    binding.rvFavorites.visible()
                    favoriteProductsAdapter.submitList(state.products)
                    progressBar3.invisible()
                }

                is FavoriteState.Error -> {
                    binding.rvFavorites.invisible()
                    Toast.makeText(
                        requireContext(),
                        state.throwable.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar3.invisible()
                }
            }

        }
    }

    override fun onClearClick() {
        viewModel.clearFavorites()
    }

    override fun onProductClick(id: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    override fun onDeleteButtonClick(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }

}