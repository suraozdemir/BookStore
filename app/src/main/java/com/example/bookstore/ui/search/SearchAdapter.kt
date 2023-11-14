package com.example.bookstore.ui.search

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
import com.example.bookstore.databinding.ItemSearchBinding

class SearchAdapter(

    private val searchProductListener: SearchProductListener

) : ListAdapter<ProductUI, SearchAdapter.ItemSearchViewHolder>(SearchProductDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSearchViewHolder =
        ItemSearchViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            searchProductListener
        )

    override fun onBindViewHolder(holder: ItemSearchViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ItemSearchViewHolder(
        private val binding: ItemSearchBinding,
        private val searchProductListener: SearchProductListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductUI) = with(binding) {
            tvProductTitle.text = product.title
            imgSerproduct.loadImage(product.imageOne)
            ratingBar5.rating = ((product.rate)?.toFloat() ?: 1) as Float
            var isFavorite = product.isFavorite

            imgSerproduct.setOnClickListener {
                searchProductListener.onProductClick(product.id)
            }

            imgFav.setOnClickListener {
                searchProductListener.onCartButtonClick(product.id)
            }

            if (isFavorite) {
                imgFav.setImageResource(R.drawable.ic_favorites_selected)
            }

            imgFav.setOnClickListener {
                isFavorite = !isFavorite
                imgFav.apply {
                    if (isFavorite) {
                        searchProductListener.onFavButtonClick(product)
                        imgFav.setImageResource(R.drawable.ic_favorites_selected)
                    } else {
                        imgFav.setImageResource(R.drawable.ic_bag_unselected)
                    }
                }
            }

            if (product.saleState == true) {
                tvProductPrice.setText(Html.fromHtml("<s>$${product.price}</s>"))
                tvProductSalePrice.visible()
                tvProductSalePrice.text = "$${product.salePrice}"
                imgSale2.visible()
            } else {
                tvProductPrice.text = "$${product.price}"
            }
        }
    }

    class SearchProductDiffCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface SearchProductListener {
        fun onProductClick(id: Int)
        fun onCartButtonClick(id: Int)
        fun onFavButtonClick(product: ProductUI)
    }
}