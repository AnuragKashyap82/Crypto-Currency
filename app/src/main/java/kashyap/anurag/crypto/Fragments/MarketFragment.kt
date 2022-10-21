package kashyap.anurag.crypto.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kashyap.anurag.crypto.Adapter.MarketAdapter
import kashyap.anurag.crypto.Apis.ApiInterface
import kashyap.anurag.crypto.Apis.ApisUtilities
import kashyap.anurag.crypto.Models.CryptoCurrency
import kashyap.anurag.crypto.databinding.FragmentMarketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MarketFragment : Fragment() {

    lateinit var binding: FragmentMarketBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter : MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)

        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApisUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList

                    adapter.updateData(list)
                    binding.spinKitView.visibility  = GONE
                }
            }
        }

        searchCoin()


        return binding.root
    }

    lateinit var searchText : String
    private fun searchCoin() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                searchText = p0.toString().toLowerCase()
                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()
        for (item in list){

            var coinName = item.name.lowercase(Locale.getDefault())
            var coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }
        }
        adapter.updateData(data)
    }

}