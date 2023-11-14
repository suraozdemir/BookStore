package com.example.bookstore.ui.home

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.common.loadImage
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.ItemProductBinding


class SaleProductsAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, SaleProductsAdapter.SaleProductViewHolder>(ProductDiffCallBack()) {

    class SaleProductViewHolder(
        private val binding: ItemProductBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductTitle.text = product.title
            ivProduct.loadImage(product.imageOne)
            ratingBar3.rating = ((product.rate)?.toFloat() ?: 1) as Float
            if (product.saleState == true) {
                tvProductPrice.setText(Html.fromHtml("<s>$${product.price}</s>"))
                tvProductSalePrice.text = "$${product.salePrice}"
                imgSale.visible()
            } else {
                tvProductPrice.text = "$${product.price}"
            }
            btnAdd.setOnClickListener {
                productListener.onAddCartButtonClick(product.id)
            }

            ivFavorite.isClickable = true
            ivFavorite.setImageResource(R.drawable.ic_favorites_selector)
            var isLiked = product.isFavorite

            ivFavorite.setOnClickListener {
                isLiked = !isLiked
                ivFavorite.apply {
                    if (isLiked) {
                        ivFavorite.setImageResource(R.drawable.ic_favorites_selected)
                        productListener.onFavoriteButtonClick(product)
                    } else {
                        ivFavorite.setImageResource(R.drawable.ic_favorites_unselected)
                    }
                }
            }
            root.setOnClickListener {
                productListener.onSaleClick(product.id ?: 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleProductViewHolder =
        SaleProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: SaleProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }

    interface ProductListener {
        fun onSaleClick(id: Int)
        fun onFavoriteButtonClick(product: ProductUI)
        fun onAddCartButtonClick(id: Int)
    }
}