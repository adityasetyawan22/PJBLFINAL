package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class Hasilform : AppCompatActivity() {

    // Deklarasi View Components
    private lateinit var tvHasilNama: TextView
    private lateinit var tvHasilAlamat: TextView
    private lateinit var tvHasilNoHP: TextView
    private lateinit var tvHasilAgama: TextView
    private lateinit var tvHasilJenisKelamin: TextView
    private lateinit var tvHasilHobi: TextView
    private lateinit var btnKembali: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasilform)

        // Inisialisasi Views
        initViews()

        // Ambil data dari Intent
        receiveData()

        // Setup Button Kembali
        btnKembali.setOnClickListener {
            finish() // Kembali ke Activity sebelumnya
        }

        // Handle hardware back button dengan OnBackPressedCallback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun initViews() {
        tvHasilNama = findViewById(R.id.tvHasilNama)
        tvHasilAlamat = findViewById(R.id.tvHasilAlamat)
        tvHasilNoHP = findViewById(R.id.tvHasilNoHP)
        tvHasilAgama = findViewById(R.id.tvHasilAgama)
        tvHasilJenisKelamin = findViewById(R.id.tvHasilJenisKelamin)
        tvHasilHobi = findViewById(R.id.tvHasilHobi)
        btnKembali = findViewById(R.id.btnKembali)
    }

    private fun receiveData() {
        // Ambil data dari Intent yang dikirim dari FormActivity
        val nama = intent.getStringExtra("NAMA") ?: "-"
        val alamat = intent.getStringExtra("ALAMAT") ?: "-"
        val noHP = intent.getStringExtra("NO_HP") ?: "-"
        val agama = intent.getStringExtra("AGAMA") ?: "-"
        val jenisKelamin = intent.getStringExtra("JENIS_KELAMIN") ?: "-"
        val hobi = intent.getStringExtra("HOBI") ?: "-"

        // Tampilkan data ke TextView menggunakan getString dengan placeholder
        tvHasilNama.text = getString(R.string.hasil_nama, nama)
        tvHasilAlamat.text = getString(R.string.hasil_alamat, alamat)
        tvHasilNoHP.text = getString(R.string.hasil_nohp, noHP)
        tvHasilAgama.text = getString(R.string.hasil_agama, agama)
        tvHasilJenisKelamin.text = getString(R.string.hasil_jenis_kelamin, jenisKelamin)
        tvHasilHobi.text = getString(R.string.hasil_hobi, hobi)
    }
}