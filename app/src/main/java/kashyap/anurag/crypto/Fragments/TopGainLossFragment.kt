package kashyap.anurag.crypto.Fragments

import android.os.Bundle
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
import kashyap.anurag.crypto.databinding.FragmentTopGainLossBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections


class TopGainLossFragment : Fragment() {

    lateinit var binding: FragmentTopGainLossBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopGainLossBinding.inflate(layoutInflater)

        getMarketData();

        return binding.root
    }

    private fun getMarketData() {

        val position = requireArguments().getInt("position")

        lifecycleScope.launch(Dispatchers.IO){
            val res = ApisUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){
                withContext(Dispatchers.Main){
                    val  dataItem = res.body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem){
                        o1, o2 -> (o2.quotes[0].percentChange24h.toInt())
                        .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    val list = ArrayList<CryptoCurrency>()

                    binding.spinKitView.visibility = GONE

                    if (position == 0){

                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )

                    }else{

                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem[dataItem.size-1-i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )

                    }


                }
            }
        }
    }

}