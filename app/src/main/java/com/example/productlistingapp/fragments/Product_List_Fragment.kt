package com.example.productlistingapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlistingapp.Adapter.ProductAdapter
import com.example.productlistingapp.R
import com.example.productlistingapp.ViewModel.Product_Viewmodel
import com.example.productlistingapp.databinding.FragmentProductListBinding
import com.example.productlistingapp.models.ProductDetail.product_detail_Model
import com.example.productlistingapp.models.ProductDetail.product_detail_ModelItem
import com.example.productlistingapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Product_List_Fragment : Fragment() {

    private var _binding: FragmentProductListBinding?= null
    private val binding get() = _binding!!
    private val productViewmodel: Product_Viewmodel by activityViewModels()
    private lateinit var adapter: ProductAdapter
    private lateinit var dataList: product_detail_Model

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentProductListBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            bindObservers()
            productViewmodel.getData()
            setHandlers()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setHandlers() {

        try {
            binding.addNote.setOnClickListener(View.OnClickListener {
                findNavController().navigate(R.id.action_product_List_Fragment_to_product_Detail_Fragment)
            })

            binding.edtTextForSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    productViewmodel.getData()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun bindObservers() {

        try {
            productViewmodel.responseLiveData.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = View.INVISIBLE
                when (it) {
                    is NetworkResult.Success -> {

                        if(binding.edtTextForSearch.text.isEmpty()) {
                            adapter = ProductAdapter(context!!, it.data!!)
                            binding.ListForProducts.layoutManager = LinearLayoutManager(context)
                            binding.ListForProducts.adapter = adapter
                        }
                        else{
                            dataList= it.data!!
                            var filteredList= dataList.filter {
                                it.product_name.contains(binding.edtTextForSearch.text.toString(),ignoreCase = true)
                            }
                            dataList.clear()
                            dataList.addAll(filteredList)
                            adapter.setData(dataList)

                        }

                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(requireContext(),"Error Occured",Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}