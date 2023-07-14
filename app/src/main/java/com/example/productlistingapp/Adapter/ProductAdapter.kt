package com.example.productlistingapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.productlistingapp.R
import com.example.productlistingapp.databinding.ListItemBinding
import com.example.productlistingapp.models.ProductDetail.product_detail_Model
import com.example.productlistingapp.models.ProductDetail.product_detail_ModelItem

class ProductAdapter(private val context:Context,private var itemList: product_detail_Model):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var newData: List<product_detail_ModelItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setData(newDataList: product_detail_Model) {
        itemList= newDataList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: product_detail_ModelItem) {
           binding.apply {

               if(item.image.isNotEmpty()) {
                   Glide.with(context)
                       .load(item.image)
                       .diskCacheStrategy(DiskCacheStrategy.ALL) // Optional: Set the disk caching strategy
                       .into(itemImage)
               }
               else{
                   Glide.with(context)
                       .load(R.drawable.account)
                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                       .into(itemImage)
               }

               itemImage.visibility= View.VISIBLE
               itemName.text= "Name: ${item.product_name}"
               itemPrice.text= "Price: ${item.price}"
               itemTax.text= "Tax: ${item.tax}"
               itemType.text= "Type: ${item.product_type}"
           }
        }
    }
}