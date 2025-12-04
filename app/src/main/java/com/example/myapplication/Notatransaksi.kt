package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class Notatransaksi : AppCompatActivity() {

    // Deklarasi View Components
    private lateinit var tvTitle: TextView
    private lateinit var tvWarung: TextView
    private lateinit var tvNamaPemesan: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var containerItems: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var btnKembali: Button

    companion object {
        private const val TAG = "Notatransaksi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_nota_transaksi)

            // Inisialisasi Views
            initViews()

            // Ambil data dari Intent
            val nama = intent.getStringExtra("EXTRA_NAMA") ?: "Tidak diketahui"
            val tanggal = intent.getStringExtra("EXTRA_TANGGAL") ?: "Tidak diketahui"
            val grandTotal = intent.getIntExtra("EXTRA_GRAND_TOTAL", 0)

            // Log untuk debugging
            Log.d(TAG, "Nama: $nama")
            Log.d(TAG, "Tanggal: $tanggal")
            Log.d(TAG, "Grand Total: $grandTotal")

            // Ambil list pesanan dengan cara yang kompatibel untuk semua versi Android
            val listPesanan = try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    @Suppress("UNCHECKED_CAST")
                    intent.getSerializableExtra("EXTRA_LIST_PESANAN", ArrayList::class.java) as? ArrayList<ItemPesanan>
                } else {
                    @Suppress("DEPRECATION", "UNCHECKED_CAST")
                    intent.getSerializableExtra("EXTRA_LIST_PESANAN") as? ArrayList<ItemPesanan>
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error mengambil list pesanan: ${e.message}")
                null
            } ?: arrayListOf()

            Log.d(TAG, "Jumlah pesanan: ${listPesanan.size}")

            // Tampilkan data nota
            displayNotaData(nama, tanggal, listPesanan, grandTotal)

            // Setup button kembali
            btnKembali.setOnClickListener {
                finish() // Kembali ke activity sebelumnya
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error di onCreate: ${e.message}", e)
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            tvTitle = findViewById(R.id.tvTitle)
            tvWarung = findViewById(R.id.tvWarung)
            tvNamaPemesan = findViewById(R.id.tvNamaPemesan)
            tvTanggal = findViewById(R.id.tvTanggal)
            containerItems = findViewById(R.id.containerItems)
            tvTotal = findViewById(R.id.tvTotal)
            btnKembali = findViewById(R.id.btnKembali)

            Log.d(TAG, "Semua views berhasil diinisialisasi")
        } catch (e: Exception) {
            Log.e(TAG, "Error inisialisasi views: ${e.message}", e)
            throw e
        }
    }

    private fun displayNotaData(
        nama: String,
        tanggal: String,
        listPesanan: List<ItemPesanan>,
        grandTotal: Int
    ) {
        try {
            // Set nama warung
            tvWarung.text = "war oeng pay i joe"

            // Set nama pemesan dan tanggal
            tvNamaPemesan.text = "Pemesan: $nama"
            tvTanggal.text = "Tanggal: $tanggal"

            // Bersihkan container sebelum menambahkan items baru
            containerItems.removeAllViews()

            // Cek apakah ada pesanan
            if (listPesanan.isEmpty()) {
                // Tampilkan pesan jika tidak ada pesanan
                val emptyTextView = TextView(this).apply {
                    text = "Tidak ada pesanan"
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    gravity = android.view.Gravity.CENTER
                    setPadding(0, dpToPx(16), 0, dpToPx(16))
                }
                containerItems.addView(emptyTextView)
                Log.d(TAG, "Tidak ada pesanan untuk ditampilkan")
            } else {
                // Tampilkan daftar items secara dinamis
                for ((_, item) in listPesanan.withIndex()) {
                    addItemToContainer(item)
                }
                Log.d(TAG, "Berhasil menampilkan ${listPesanan.size} item")
            }

            // Set total dengan format yang lebih baik
            tvTotal.text = "TOTAL: ${formatRupiah(grandTotal)}"

        } catch (e: Exception) {
            Log.e(TAG, "Error menampilkan data nota: ${e.message}", e)
        }
    }

    private fun addItemToContainer(item: ItemPesanan) {
        try {
            // Buat LinearLayout untuk setiap item dengan spacing yang lebih baik
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dpToPx(8) // Spacing antar item
                }
                setPadding(0, dpToPx(4), 0, dpToPx(4))
            }

            // TextView untuk nama item (dengan wrapping untuk nama panjang)
            val tvNamaItem = TextView(this).apply {
                text = item.namaItem
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.black, theme))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    2f
                )
                // Agar teks panjang bisa wrap ke baris baru
                maxLines = 3
                ellipsize = android.text.TextUtils.TruncateAt.END
            }

            // TextView untuk quantity
            val tvQuantity = TextView(this).apply {
                text = item.quantity.toString()
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.black, theme))
                gravity = android.view.Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.8f
                )
            }

            // TextView untuk subtotal
            val tvSubtotal = TextView(this).apply {
                text = formatRupiah(item.subtotal)
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.black, theme))
                gravity = android.view.Gravity.END
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.5f
                )
                // Agar angka panjang bisa wrap
                maxLines = 2
                ellipsize = android.text.TextUtils.TruncateAt.END
            }

            // Tambahkan TextViews ke itemLayout
            itemLayout.addView(tvNamaItem)
            itemLayout.addView(tvQuantity)
            itemLayout.addView(tvSubtotal)

            // Tambahkan itemLayout ke container
            containerItems.addView(itemLayout)

        } catch (e: Exception) {
            Log.e(TAG, "Error menambahkan item ke container: ${e.message}", e)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp. ${"%,d".format(amount).replace(',', '.')}"
    }

    // Data class untuk item pesanan dengan Serializable
    data class ItemPesanan(
        val namaItem: String = "",
        val harga: Int = 0,
        val quantity: Int = 0,
        val subtotal: Int = 0
    ) : Serializable
}