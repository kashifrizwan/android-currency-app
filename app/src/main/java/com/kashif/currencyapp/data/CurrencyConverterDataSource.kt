package com.kashif.currencyapp.data

import com.haroldadmin.cnradapter.NetworkResponse.NetworkError
import com.haroldadmin.cnradapter.NetworkResponse.ServerError
import com.haroldadmin.cnradapter.NetworkResponse.Success
import com.haroldadmin.cnradapter.NetworkResponse.UnknownError
import com.kashif.currencyapp.model.CurrenciesDataModel
import com.kashif.currencyapp.model.CurrenciesDataModel.ApiError
import com.kashif.currencyapp.model.CurrenciesDataModel.Currencies
import com.kashif.currencyapp.model.CurrenciesDataModel.ExchangeRates
import com.kashif.currencyapp.model.CurrenciesDataModel.NetworkException
import com.kashif.currencyapp.network.RetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyConverterDataSource @Inject constructor(
    private val api: RetrofitApi
) {
    suspend fun fetchCurrencies() = withContext(Dispatchers.IO) {
        try {
            when (val currenciesResponse = api.getCurrencies()) {
                is Success -> {
                    when {
                        currenciesResponse.body.success -> Currencies(
                            currencies = currenciesResponse.body.symbols
                        )
                        else -> ApiError(message = currenciesResponse.body.error.info)
                    }
                }
                is ServerError -> ApiError(message = currenciesResponse.body?.message ?: "Unknown Error")
                is NetworkError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
                is UnknownError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
            }
        } catch (exception: HttpException) {
            ApiError(message = exception.message())
        } catch (exception: IOException) {
            ApiError(message = exception.message ?: "Unknown Error")
        } catch (exception: Exception) {
            NetworkException
        }
    }

    suspend fun fetchExchangeRate(base: String, symbol: String) = withContext(Dispatchers.IO) {
        try {
            when (val currenciesResponse = api.getExchangeRate(base, symbol)) {
                is Success -> {
                    when {
                        currenciesResponse.body.success -> ExchangeRates(
                            rates = currenciesResponse.body.rates
                        )
                        else -> ApiError(message = currenciesResponse.body.error.info)
                    }
                }
                is ServerError -> ApiError(message = currenciesResponse.body?.message ?: "Unknown Error")
                is NetworkError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
                is UnknownError -> ApiError(message = currenciesResponse.error.message ?: "Unknown Error")
            }
        } catch (exception: HttpException) {
            ApiError(message = exception.message())
        } catch (exception: IOException) {
            ApiError(message = exception.message ?: "Unknown Error")
        } catch (exception: Exception) {
            NetworkException
        }
    }
}
