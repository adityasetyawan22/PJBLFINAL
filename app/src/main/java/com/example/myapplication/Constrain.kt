package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Constrain : AppCompatActivity() {

    private lateinit var tvHasil: TextView
    private lateinit var tvHasilMini: TextView

    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnTimes: Button
    private lateinit var btnDevide: Button
    private lateinit var btnequal: Button
    private lateinit var btnC: Button
    private lateinit var btnDel: Button
    private lateinit var btnKoma: Button

    private lateinit var angkaButtons: List<Button>

    private val ekspresi = mutableListOf<String>()
    private var angkaSekarang = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_constrain)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        klikSemuaTombol()
    }

    private fun init() {
        tvHasil = findViewById(R.id.tvHasil)
        tvHasilMini = findViewById(R.id.tvHasilMini)

        angkaButtons = listOf(
            findViewById(R.id.btn0),
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )

        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnminus)
        btnTimes = findViewById(R.id.btnTimes)
        btnDevide = findViewById(R.id.btnDevide)
        btnKoma = findViewById(R.id.btnKoma)
        btnequal = findViewById(R.id.btnequal)
        btnC = findViewById(R.id.btnC)
        btnDel = findViewById(R.id.btnDelate)
    }

    private fun klikSemuaTombol() {

        angkaButtons.forEach { btn ->
            btn.setOnClickListener {
                angkaSekarang += btn.text.toString()
                updateEkspresi()
            }
        }

        btnKoma.setOnClickListener {
            if (!angkaSekarang.contains(".")) {
                angkaSekarang = if (angkaSekarang.isEmpty()) "0." else angkaSekarang + "."
                updateEkspresi()
            }
        }

        btnPlus.setOnClickListener { tambahOperator("+") }
        btnMinus.setOnClickListener { tambahOperator("-") }
        btnTimes.setOnClickListener { tambahOperator("x") }
        btnDevide.setOnClickListener { tambahOperator("/") }

        btnC.setOnClickListener {
            angkaSekarang = ""
            ekspresi.clear()
            tvHasil.text = ""
            tvHasilMini.text = ""
        }

        btnDel.setOnClickListener {
            if (angkaSekarang.isNotEmpty()) {
                angkaSekarang = angkaSekarang.dropLast(1)
            } else if (ekspresi.isNotEmpty()) {
                ekspresi.removeAt(ekspresi.lastIndex)

            }
            updateDisplay()
        }

        btnequal.setOnClickListener {
            val hasil = hitungEkspresi()
            tvHasil.text = hasil
            tvHasilMini.text = ""
            angkaSekarang = hasil
            ekspresi.clear()
        }
    }

    private fun tambahOperator(op: String) {
        if (angkaSekarang.isNotEmpty()) {
            ekspresi.add(angkaSekarang)
            angkaSekarang = ""
        }
        if (ekspresi.isNotEmpty()) {
            ekspresi.add(op)
        }
        updateDisplay()
    }

    private fun updateEkspresi() {
        updateDisplay()
        tvHasilMini.text = hitungEkspresi()
    }

    private fun updateDisplay() {
        val gabungan = ekspresi.joinToString(" ") +
                (if (angkaSekarang.isNotEmpty()) " $angkaSekarang" else "")
        tvHasil.text = gabungan.trim()
    }

    private fun hitungEkspresi(): String {
        val listHitung = mutableListOf<String>()
        listHitung.addAll(ekspresi)
        if (angkaSekarang.isNotEmpty()) listHitung.add(angkaSekarang)

        if (listHitung.isEmpty()) return "0"

        try {
            var hasil = listHitung[0].toDouble()

            var index = 1
            while (index < listHitung.size) {
                val op = listHitung[index]
                val angka = listHitung[index + 1].toDouble()

                when (op) {
                    "+" -> hasil += angka
                    "-" -> hasil -= angka
                    "x" -> hasil *= angka
                    "/" -> if (angka != 0.0) hasil /= angka else return "Error"
                }

                index += 2
            }

            return if (hasil % 1 == 0.0) hasil.toInt().toString() else hasil.toString()

        } catch (e: Exception) {
            return "Error"
        }
    }
}
