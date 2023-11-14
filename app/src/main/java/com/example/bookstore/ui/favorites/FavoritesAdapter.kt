package com.example.bookstore.ui.favorites

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.common.loadImage
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.ItemFavBinding

class FavoritesAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, FavoritesAdapter.ItemFavViewHolder>(ProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFavViewHolder =
        ItemFavViewHolder(
            ItemFavBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )

    override fun onBindViewHolder(holder: ItemFavViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ItemFavViewHolder(
        private val binding: ItemFavBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductTitle.text = product.title
            imgFavproduct.loadImage(product.imageOne)
            ratingBar2.rating = ((product.rate)?.toFloat() ?: 1) as Float
            root.setOnClickListener {
                productListener.onProductClick(product.id)
            }
            icDelete.setOnClickListener {
                productListener.onDeleteButtonClick(product)
            }

            if (product.saleState == true) {
                tvProductSalePrice.text = "$${product.salePrice}"
                tvProductPrice.setText(Html.fromHtml("<s>$${product.price}</s>"))
                imgSale3.visible()
            } else {
                tvProductPrice.text = "$${product.price}"
            }
        }
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface ProductListener {
        fun onProductClick(id: Int)
        fun onDeleteButtonClick(product: ProductUI)
        fun onClearClick()
    }
}