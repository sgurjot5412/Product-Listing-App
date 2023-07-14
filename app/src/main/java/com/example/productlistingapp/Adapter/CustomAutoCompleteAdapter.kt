package com.example.productlistingapp.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.productlistingapp.R

class CustomAutoCompleteAdapter(
    private val context: Context,
    private val dataList: List<String>
) : BaseAdapter(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var filteredDataList: List<String> = dataList

    override fun getCount(): Int {
        return filteredDataList.size
    }

    override fun getItem(position: Int): String {
        return filteredDataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.simple_text_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.itemTextView.text = filteredDataList[position]
        viewHolder.itemTextView.setTextColor(Color.WHITE)

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val filteredList = ArrayList<String>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(dataList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (item in dataList) {
                        if (item.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredDataList = results?.values as? List<String> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    private class ViewHolder(view: View) {
        val itemTextView: TextView = view.findViewById(android.R.id.text1)
    }
}
