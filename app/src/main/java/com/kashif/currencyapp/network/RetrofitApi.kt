package com.kashif.currencyapp.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.kashif.currencyapp.model.CurrenciesApiModel
import com.kashif.currencyapp.model.ExchangeRateApiModel
import com.kashif.currencyapp.model.GenericApiErrorModel
import retrofit2.http.GET
import retrofit2.http.Query

const val QUERY_PARAM_BASE = "base"
const val QUERY_PARAM_SYMBOLS = "symbols"

interface RetrofitApi {

    @GET(Url.ENDPOINT_CURRENCIES)
    suspend fun getCurrencies(): NetworkResponse<CurrenciesApiModel, GenericApiErrorModel>

    @GET(Url.ENDPOINT_EXCHANGE)
    suspend fun getExchangeRate(
        @Query(QUERY_PARAM_BASE) base: String,
        @Query(QUERY_PARAM_SYMBOLS) symbols: String
    ): NetworkResponse<ExchangeRateApiModel, GenericApiErrorModel>
}
