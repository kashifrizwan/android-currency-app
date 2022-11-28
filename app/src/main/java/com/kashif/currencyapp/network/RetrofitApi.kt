package com.kashif.currencyapp.network

import com.haroldadmin.cnradapter.NetworkResponse
import com.kashif.currencyapp.model.CurrenciesApiModel
import com.kashif.currencyapp.model.GenericApiErrorModel
import retrofit2.http.GET

interface RetrofitApi {

    @GET(Url.ENDPOINT_CURRENCIES)
    suspend fun getCurrencies(): NetworkResponse<CurrenciesApiModel, GenericApiErrorModel>
}
