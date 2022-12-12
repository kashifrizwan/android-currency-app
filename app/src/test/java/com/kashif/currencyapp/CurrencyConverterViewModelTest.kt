package com.kashif.currencyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kashif.currencyapp.domain.CurrencyConverterRepository
import com.kashif.currencyapp.model.CurrenciesDataModel.Currencies
import com.kashif.currencyapp.model.CurrenciesDataModel.ExchangeRates
import com.kashif.currencyapp.presentation.CurrencyConverterViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given

private val mockCurrenciesList = Currencies(
    currencies = hashMapOf(
        "AED" to "United Arab Emirates Dirham",
        "AFN" to "Afghan Afghani",
        "ALL" to "Albanian Lek",
        "AMD" to "Armenian Dram"
    )
)

private val mockExchangeRates = ExchangeRates(
    rates = hashMapOf(
        "AED" to 3.2,
        "AFN" to 4.3,
        "ALL" to 4.32,
        "AMD" to 2.22
    )
)

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var classUnderTest: CurrencyConverterViewModel

    @Mock
    private lateinit var currencyConverterRepository: CurrencyConverterRepository

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        classUnderTest = CurrencyConverterViewModel(
            currencyConverterRepository = currencyConverterRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given successful execution of onFragmentCreated() Then update the Currencies List`() {
        // Given
        val expectedResult = listOf("AED", "AFN", "ALL", "AMD")
        runBlocking { given(currencyConverterRepository.fetchCurrencies()).willReturn(mockCurrenciesList) }

        // When
        classUnderTest.onFragmentCreated()
        val actualValue = classUnderTest.availableCurrenciesList.value

        // Then
        assertEquals(expectedResult, actualValue)
    }

    @Test
    fun `Given successful execution of onCurrencyChangeAction() Then update the view state`() {
        // Given
        val expectedResult = 4.3
        val baseCurrency = "AED"
        val toCurrency = "AFN"
        runBlocking {
            given(currencyConverterRepository.fetchExchangeRates(baseCurrency, toCurrency))
                .willReturn(mockExchangeRates)
        }

        // When
        classUnderTest.onCurrencyChangeAction(baseCurrency, toCurrency)
        val actualValue = classUnderTest.currentExchangeRate.value

        // Then
        assertEquals(expectedResult, actualValue)
    }
}
