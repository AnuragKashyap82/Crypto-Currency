package kashyap.anurag.crypto.Apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApisUtilities {

    fun getInstance() :Retrofit{
        return  Retrofit.Builder()
            .baseUrl("https://api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}