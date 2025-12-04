package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class Temperatur : AppCompatActivity() {

    private lateinit var spSuhu: Spinner
    private lateinit var spSuhu1: Spinner
    private lateinit var input: EditText
    private lateinit var output: EditText
    private lateinit var btnKonverter: Button
    private lateinit var cardViewResult: MaterialCardView
    private lateinit var tvResultInput: TextView
    private lateinit var tvResultOutput: TextView
    private lateinit var tvResultInfo: TextView

    // Flag untuk mencegah loop infinite saat update
    private var isUpdating = false

    // Konstanta untuk nama unit (sesuai dengan array di strings.xml)
    companion object {
        const val UNIT_CELSIUS = "Celsius"
        const val UNIT_FAHRENHEIT = "Fahrenheit"
        const val UNIT_KELVIN = "Kelvin"
        const val UNIT_REAMUR = "Reamur"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperatur)

        // Inisialisasi views
        initViews()

        // Setup listeners untuk auto-convert
        setupAutoConversion()

        // Tombol konversi tetap berfungsi
        btnKonverter.setOnClickListener {
            performConversion()
        }
    }

    private fun initViews() {
        spSuhu = findViewById(R.id.spSuhu)
        spSuhu1 = findViewById(R.id.spSuhu1)
        input = findViewById(R.id.input)
        output = findViewById(R.id.Output)
        btnKonverter = findViewById(R.id.btnkonverter)
        cardViewResult = findViewById(R.id.CardViewResult)
        tvResultInput = findViewById(R.id.tvResultInput)
        tvResultOutput = findViewById(R.id.tvResultOutput)
        tvResultInfo = findViewById(R.id.tvResultInfo)
    }

    private fun setupAutoConversion() {
        // Auto convert saat input berubah
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating && !s.isNullOrEmpty()) {
                    performConversion()
                } else if (s.isNullOrEmpty()) {
                    clearResults()
                }
            }
        })

        // Auto convert saat spinner input berubah
        spSuhu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isUpdating && input.text.isNotEmpty()) {
                    performConversion()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Auto convert saat spinner output berubah
        spSuhu1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isUpdating && input.text.isNotEmpty()) {
                    performConversion()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun performConversion() {
        val inputValue = input.text.toString()

        if (inputValue.isEmpty()) {
            Toast.makeText(this, R.string.toast_masukkan_nilai, Toast.LENGTH_SHORT).show()
            return
        }

        try {
            isUpdating = true
            val temp = inputValue.toDouble()
            val fromUnit = spSuhu.selectedItem.toString()
            val toUnit = spSuhu1.selectedItem.toString()

            // Konversi suhu
            val result = convertTemperature(temp, fromUnit, toUnit)

            // Tampilkan hasil di output EditText
            output.setText(String.format("%.2f", result))

            // Update card hasil konversi
            updateResultCard(temp, fromUnit, result, toUnit)

            // Tampilkan card hasil
            cardViewResult.visibility = View.VISIBLE

        } catch (e: NumberFormatException) {
            Toast.makeText(this, R.string.toast_format_invalid, Toast.LENGTH_SHORT).show()
        } finally {
            isUpdating = false
        }
    }

    private fun convertTemperature(value: Double, from: String, to: String): Double {
        // Konversi ke Celsius dulu
        val celsius = when (from) {
            UNIT_CELSIUS -> value
            UNIT_FAHRENHEIT -> (value - 32) * 5 / 9
            UNIT_KELVIN -> value - 273.15
            UNIT_REAMUR -> value * 5 / 4
            else -> value
        }

        // Konversi dari Celsius ke satuan target
        return when (to) {
            UNIT_CELSIUS -> celsius
            UNIT_FAHRENHEIT -> celsius * 9 / 5 + 32
            UNIT_KELVIN -> celsius + 273.15
            UNIT_REAMUR -> celsius * 4 / 5
            else -> celsius
        }
    }

    private fun updateResultCard(inputTemp: Double, inputUnit: String, outputTemp: Double, outputUnit: String) {
        // Format tampilan suhu dengan simbol
        tvResultInput.text = String.format("%.2f °%s", inputTemp, getUnitSymbol(inputUnit))
        tvResultOutput.text = String.format("%.2f °%s", outputTemp, getUnitSymbol(outputUnit))

        // Tentukan info berdasarkan hasil konversi (gunakan string resources)
        val info = when {
            outputTemp <= 0 && outputUnit == UNIT_CELSIUS -> getString(R.string.info_titik_beku)
            outputTemp >= 100 && outputUnit == UNIT_CELSIUS -> getString(R.string.info_titik_didih)
            outputTemp in 20.0..30.0 && outputUnit == UNIT_CELSIUS -> getString(R.string.info_suhu_nyaman)
            outputTemp > 35 && outputUnit == UNIT_CELSIUS -> getString(R.string.info_sangat_panas)
            outputTemp < 10 && outputUnit == UNIT_CELSIUS -> getString(R.string.info_sangat_dingin)
            else -> getString(R.string.info_konversi_berhasil)
        }

        tvResultInfo.text = info
    }

    private fun getUnitSymbol(unit: String): String {
        return when (unit) {
            UNIT_CELSIUS -> getString(R.string.unit_celsius)
            UNIT_FAHRENHEIT -> getString(R.string.unit_fahrenheit)
            UNIT_KELVIN -> getString(R.string.unit_kelvin)
            UNIT_REAMUR -> getString(R.string.unit_reamur)
            else -> ""
        }
    }

    private fun clearResults() {
        output.setText("")
        cardViewResult.visibility = View.GONE
    }
}