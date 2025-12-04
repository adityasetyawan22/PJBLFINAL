package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Formactivity : AppCompatActivity() {

    // Deklarasi View Components
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHP: EditText
    private lateinit var spinnerAgama: Spinner
    private lateinit var rgJenisKelamin: RadioGroup
    private lateinit var rbLakiLaki: RadioButton
    private lateinit var rbPerempuan: RadioButton
    private lateinit var cbMembaca: CheckBox
    private lateinit var cbMakan: CheckBox
    private lateinit var cbTidur: CheckBox
    private lateinit var cbOlahraga: CheckBox
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formactivity)

        // Inisialisasi Views
        initViews()

        // Set Button Click Listener
        btnSimpan.setOnClickListener {
            simpanData()
        }
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etNoHP = findViewById(R.id.etNoHP)
        spinnerAgama = findViewById(R.id.spinnerAgama)
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin)
        rbLakiLaki = findViewById(R.id.rbLakiLaki)
        rbPerempuan = findViewById(R.id.rbPerempuan)
        cbMembaca = findViewById(R.id.cbMembaca)
        cbMakan = findViewById(R.id.cbMakan)
        cbTidur = findViewById(R.id.cbTidur)
        cbOlahraga = findViewById(R.id.cbOlahraga)
        btnSimpan = findViewById(R.id.btnSimpan)
    }

    private fun simpanData() {
        // Validasi Input
        if (!validateInput()) {
            return
        }

        // Ambil data dari form
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val noHP = etNoHP.text.toString().trim()
        val agama = spinnerAgama.selectedItem.toString()
        val jenisKelamin = getJenisKelamin()
        val hobi = getHobiList()

        // Intent untuk pindah ke Hasilform
        val intent = Intent(this, Hasilform::class.java)
        intent.putExtra("NAMA", nama)
        intent.putExtra("ALAMAT", alamat)
        intent.putExtra("NO_HP", noHP)
        intent.putExtra("AGAMA", agama)
        intent.putExtra("JENIS_KELAMIN", jenisKelamin)
        intent.putExtra("HOBI", hobi.joinToString(", "))

        startActivity(intent)

        // Reset form setelah simpan
        resetForm()
    }

    private fun validateInput(): Boolean {
        // Validasi Nama
        if (etNama.text.toString().trim().isEmpty()) {
            etNama.error = "Nama tidak boleh kosong"
            etNama.requestFocus()
            return false
        }

        // Validasi Alamat
        if (etAlamat.text.toString().trim().isEmpty()) {
            etAlamat.error = "Alamat tidak boleh kosong"
            etAlamat.requestFocus()
            return false
        }

        // Validasi No HP
        if (etNoHP.text.toString().trim().isEmpty()) {
            etNoHP.error = "Nomor HP tidak boleh kosong"
            etNoHP.requestFocus()
            return false
        }

        // Validasi Jenis Kelamin
        if (rgJenisKelamin.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validasi Hobi (minimal 1 hobi dipilih)
        if (!cbMembaca.isChecked && !cbMakan.isChecked &&
            !cbTidur.isChecked && !cbOlahraga.isChecked) {
            Toast.makeText(this, "Pilih minimal 1 hobi", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun getJenisKelamin(): String {
        return when (rgJenisKelamin.checkedRadioButtonId) {
            R.id.rbLakiLaki -> "Laki-laki"
            R.id.rbPerempuan -> "Perempuan"
            else -> "Tidak dipilih"
        }
    }

    private fun getHobiList(): List<String> {
        val hobiList = mutableListOf<String>()

        if (cbMembaca.isChecked) hobiList.add("Membaca")
        if (cbMakan.isChecked) hobiList.add("Makan")
        if (cbTidur.isChecked) hobiList.add("Tidur")
        if (cbOlahraga.isChecked) hobiList.add("Olahraga")

        return hobiList
    }

    private fun resetForm() {
        etNama.text.clear()
        etAlamat.text.clear()
        etNoHP.text.clear()
        spinnerAgama.setSelection(0)
        rgJenisKelamin.clearCheck()
        cbMembaca.isChecked = false
        cbMakan.isChecked = false
        cbTidur.isChecked = false
        cbOlahraga.isChecked = false
    }
}