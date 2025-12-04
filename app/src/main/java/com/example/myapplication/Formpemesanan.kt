package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Formpemesanan : AppCompatActivity() {

    // Deklarasi Views
    private lateinit var etNama: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etSotoAyam: EditText
    private lateinit var etNasiGoreng: EditText
    private lateinit var etSopDaging: EditText
    private lateinit var etEsTeh: EditText
    private lateinit var etEsJeruk: EditText
    private lateinit var etKopiClaveoka: EditText
    private lateinit var btnLanjutKeNota: Button

    // Data menu dan harga
    private val menuData = mapOf(
        "Soto Ayam" to 8000,
        "Nasi Goreng" to 15000,
        "Sop Daging" to 10000,
        "Es Teh" to 3000,
        "Es Soda Gembira" to 7000,
        "Americano" to 10000
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formpemesanan)

        // Inisialisasi Views
        initViews()

        // Setup DatePicker untuk input tanggal
        setupDatePicker()

        // Setup button submit
        btnLanjutKeNota.setOnClickListener {
            prosesFormPemesanan()
        }
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNama)
        etTanggal = findViewById(R.id.etTanggal)
        etSotoAyam = findViewById(R.id.etSotoAyam)
        etNasiGoreng = findViewById(R.id.etNasiGoreng)
        etSopDaging = findViewById(R.id.etSopDaging)
        etEsTeh = findViewById(R.id.etEsTeh)
        etEsJeruk = findViewById(R.id.etEsJeruk)
        etKopiClaveoka = findViewById(R.id.etKopiClaveoka)
        btnLanjutKeNota = findViewById(R.id.btnLanjutKeNota)
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Set tanggal hari ini sebagai default
        etTanggal.setText(dateFormat.format(calendar.time))

        etTanggal.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    etTanggal.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun prosesFormPemesanan() {
        // Validasi nama
        val nama = etNama.text.toString().trim()
        if (nama.isEmpty()) {
            Toast.makeText(this, "Nama harus diisi!", Toast.LENGTH_SHORT).show()
            etNama.requestFocus()
            return
        }

        // Ambil tanggal
        val tanggal = etTanggal.text.toString().trim()
        if (tanggal.isEmpty()) {
            Toast.makeText(this, "Tanggal harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil quantity dari setiap menu
        val qtySotoAyam = getQuantity(etSotoAyam)
        val qtyNasiGoreng = getQuantity(etNasiGoreng)
        val qtySopDaging = getQuantity(etSopDaging)
        val qtyEsTeh = getQuantity(etEsTeh)
        val qtyEsJeruk = getQuantity(etEsJeruk)
        val qtyKopiClaveoka = getQuantity(etKopiClaveoka)

        // Buat list pesanan
        val listPesanan = ArrayList<Notatransaksi.ItemPesanan>()
        var grandTotal = 0

        // Tambahkan item yang dipesan (quantity > 0)
        if (qtySotoAyam > 0) {
            val harga = menuData["Soto Ayam"] ?: 0
            val subtotal = harga * qtySotoAyam
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Soto Ayam",
                    harga = harga,
                    quantity = qtySotoAyam,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        if (qtyNasiGoreng > 0) {
            val harga = menuData["Nasi Goreng"] ?: 0
            val subtotal = harga * qtyNasiGoreng
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Nasi Goreng",
                    harga = harga,
                    quantity = qtyNasiGoreng,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        if (qtySopDaging > 0) {
            val harga = menuData["Sop Daging"] ?: 0
            val subtotal = harga * qtySopDaging
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Sop Daging",
                    harga = harga,
                    quantity = qtySopDaging,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        if (qtyEsTeh > 0) {
            val harga = menuData["Es Teh"] ?: 0
            val subtotal = harga * qtyEsTeh
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Es Teh",
                    harga = harga,
                    quantity = qtyEsTeh,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        if (qtyEsJeruk > 0) {
            val harga = menuData["Es Soda Gembira"] ?: 0
            val subtotal = harga * qtyEsJeruk
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Es Soda Gembira",
                    harga = harga,
                    quantity = qtyEsJeruk,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        if (qtyKopiClaveoka > 0) {
            val harga = menuData["Americano"] ?: 0
            val subtotal = harga * qtyKopiClaveoka
            listPesanan.add(
                Notatransaksi.ItemPesanan(
                    namaItem = "Americano",
                    harga = harga,
                    quantity = qtyKopiClaveoka,
                    subtotal = subtotal
                )
            )
            grandTotal += subtotal
        }

        // Validasi apakah ada pesanan
        if (listPesanan.isEmpty()) {
            Toast.makeText(this, "Pilih minimal 1 menu!", Toast.LENGTH_SHORT).show()
            return
        }

        // Kirim data ke Nota Transaksi
        val intent = Intent(this, Notatransaksi::class.java).apply {
            putExtra("EXTRA_NAMA", nama)
            putExtra("EXTRA_TANGGAL", tanggal)
            putExtra("EXTRA_GRAND_TOTAL", grandTotal)
            putExtra("EXTRA_LIST_PESANAN", listPesanan)
        }
        startActivity(intent)
    }

    private fun getQuantity(editText: EditText): Int {
        val text = editText.text.toString().trim()
        return if (text.isEmpty()) {
            0
        } else {
            try {
                text.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }
    }
}