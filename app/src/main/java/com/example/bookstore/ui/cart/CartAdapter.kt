package com.example.bookstore.ui.cart

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.common.loadImage
import com.example.bookstore.common.visible
import com.example.bookstore.data.model.response.ProductUI
import com.example.bookstore.databinding.ItemCartBinding

class CartAdapter(
    private val productListener: ProductListener
) : ListAdapter<ProductUI, CartAdapter.ItemCartViewHolder>(ProductDiffCallBack()) {

    class ItemCartViewHolder(
        private val binding: ItemCartBinding,
        private val productListener: ProductListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) = with(binding) {
            var productCount = 1

            tvTitle.text = product.title
            imgProductCart.loadImage(product.imageOne)
            ratingBar4.rating = ((product.rate)?.toFloat() ?: 1) as Float
            if (product.saleState == true) {
                productPrice.setText(Html.fromHtml("<s>$${product.price}</s>"))
                productSalePrice.text = "$${product.salePrice}"
                imgSale4.visible()
            } else {
                productPrice.text = "$${product.price}"
            }
            productAmountCart.text = productCount.toString()

            root.setOnClickListener {
                productListener.onCartItemClick(product.id)
            }

            btnDelete.setOnClickListener {
                productListener.onDeleteItemClick(product.id)
            }

            btnPlus.setOnClickListener {
                productListener.onIncreaseClick(product.price)
                productCount++
                productAmountCart.text = productCount.toString()
            }

            btnMinus.setOnClickListener {
                if (productCount != 1) {
                    productListener.onDecreaseClick(product.price)
                    productCount--
                    productAmountCart.text = productCount.toString()
                } else {
                    productListener.onDeleteItemClick(product.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartViewHolder {
        return ItemCartViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            productListener
        )
    }

    override fun onBindViewHolder(holder: ItemCartViewHolder, position: Int) {
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
        fun onCartItemClick(id: Int)
        fun onDeleteItemClick(id: Int)
        fun onIncreaseClick(price: Double?)
        fun onDecreaseClick(price: Double?)
        fun onClearCart(userId: String)
    }
}