package com.example.bookstore.ui.detail

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.common.loadImage
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.request.AddToCartRequest
import com.example.bookstore.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)
        observeData()

        binding.backHome.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_homeFragment2)
        }

        binding.btnAddToCart.setOnClickListener {
            val addToCartRequest = AddToCartRequest(auth.currentUser?.uid.toString(), args.id)
            viewModel.addToCart(addToCartRequest)
        }

    }

    private fun observeData() = with(binding) {

        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailState.Data -> {
                    imgProduct.loadImage(state.product.imageOne)
                    tvProductTitle.text = state.product.title
                    if (state.product.saleState == true) {
                        price.setText(Html.fromHtml("<s>$${state.product.price}</s>"))
                        salePrice.text = "$${state.product.salePrice}"
                        imgSale5.visible()
                    } else {
                        price.text = "$${state.product.price}"
                    }
                    tvCategory.text = state.product.category
                    tvCount.text = " ${state.product.count}"
                    txtDescDetail.text = state.product.description
                    ratingBar.rating = ((state.product.rate)?.toFloat() ?: 1) as Float
                }

                is DetailState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }

                is DetailState.AddToBag -> {
                    Snackbar.make(requireView(), state.baseResponse.message.toString(), 1000).show()
                }
            }
        }
    }
}