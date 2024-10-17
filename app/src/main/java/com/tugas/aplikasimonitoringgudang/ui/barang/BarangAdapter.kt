package com.tugas.aplikasimonitoringgudang.ui.barang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.R

class BarangAdapter(private val barangList: List<Barang>) :
    RecyclerView.Adapter<BarangAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.textView4)
        // Tambahkan TextView untuk kategori, harga, stok, dan ukuran
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_barang, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = barangList[position]
        holder.namaBarang.text = currentItem.nama
        // Atur teks untuk kategori, harga, stok, dan ukuran
    }

    override fun getItemCount(): Int {
        return barangList.size
    }
}