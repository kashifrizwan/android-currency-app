package com.kashif.currencyapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kashif.currencyapp.common.showAlertDialog
import com.kashif.currencyapp.databinding.FragmentCurrencyConverterBinding
import com.kashif.currencyapp.presentation.CurrencyConverterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {

    private val viewModel: CurrencyConverterViewModel by viewModels()

    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFragmentCreated()

        viewModel.dialogCommand.observe(viewLifecycleOwner) { alertMessage ->
            showAlertDialog(message = alertMessage)
        }

        viewModel.availableCurrenciesList.observe(viewLifecycleOwner) { currencies ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencies)
            binding.spinnerBaseCurrency.adapter = adapter
            binding.spinnerToCurrency.adapter = adapter
        }

        viewModel.toAmount.observe(viewLifecycleOwner) { toAmount ->
            binding.editTextToCurrency.setText(toAmount.toString())
        }

        viewModel.currentExchangeRate.observe(viewLifecycleOwner) {
            viewModel.onBaseAmountChanged(
                baseAmount = binding.editTextBaseCurrency.text.toString().toDoubleOrNull() ?: 0.0
            )
        }

        setupClickListeners()
        setupOnItemChangeListeners()
        setupOnTextChangeListeners()
    }

    private fun setupClickListeners() {
        binding.btnSwap.setOnClickListener {
            val baseSpinnerSelectedPosition = binding.spinnerBaseCurrency.selectedItemPosition
            binding.spinnerBaseCurrency.setSelection(binding.spinnerToCurrency.selectedItemPosition)
            binding.spinnerToCurrency.setSelection(baseSpinnerSelectedPosition)
        }

        binding.btnDetails.setOnClickListener {

        }
    }

    private fun setupOnItemChangeListeners() {
        binding.spinnerBaseCurrency.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onCurrencyChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerToCurrency.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onCurrencyChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupOnTextChangeListeners() {
        binding.editTextBaseCurrency.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onBaseAmountChanged(
                    baseAmount = binding.editTextBaseCurrency.text.toString().toDoubleOrNull() ?: 0.0
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onCurrencyChanged() {
        viewModel.onCurrencyChangeAction(
            baseCurrency = binding.spinnerBaseCurrency.selectedItem.toString(),
            toCurrency = binding.spinnerToCurrency.selectedItem.toString()
        )
    }
}
