package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.ItemBarangBinding
import java.text.NumberFormat
import java.util.Locale

class AdapterBarang(private var barangList: List<Barang>,
                    private val onDetailClick: (Barang) -> Unit) :
    RecyclerView.Adapter<AdapterBarang.BarangViewHolder>() {

    inner class BarangViewHolder(private val binding: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nama = binding.namaBarang
        val harga = binding.hargaBarang
        val stok = binding.stokBarang

        init {
            itemView.setOnClickListener {
                onDetailClick(barangList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = ItemBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val barang = barangList[position]
        holder.nama.text = barang.nama_barang
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        holder.harga.text = numberFormat.format(barang.harga_barang)
        holder.stok.text = barang.stok_barang.toString()
    }

    override fun getItemCount(): Int {
        return barangList.size
    }

    // Fungsi untuk mengatur/mengupdate data di adapter
    @SuppressLint("NotifyDataSetChanged")
    fun setBarangList(newList: List<Barang>) {
        barangList = newList
        notifyDataSetChanged() // Memastikan RecyclerView diperbarui
    }
}