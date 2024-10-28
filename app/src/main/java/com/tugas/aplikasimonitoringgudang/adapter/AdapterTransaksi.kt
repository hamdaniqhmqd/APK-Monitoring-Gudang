package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.R
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.ItemTransaksiBinding


class AdapterTransaksi(
    private var transaksiList: List<Transaksi>,
    private var onItemClick: (Transaksi) -> Unit
) : RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>() {

    inner class TransaksiViewHolder(private val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val status = binding.headerCard
        val namaStatus = binding.labelCard
        val namaBarang = binding.namaBarang
        val hargaBarang = binding.HargaBarang
        val jumlahTransaksi = binding.JumlahBarang
        val totalHargaTransaksi = binding.TotalHargaBarang
        val namaSupplier = binding.NamaSupplier
        val namaAdmin = binding.NamaAdmin

        init {
            itemView.setOnClickListener {
                onItemClick(transaksiList[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val binding =
            ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransaksiViewHolder(binding)
    }
    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val dataTransaksi = transaksiList[position]
        val context = holder.itemView.context
        var status = dataTransaksi.status
        if (status == 1) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, com.tugas.aplikasimonitoringgudang.R.color.merah_keluar))
            holder.namaStatus.text = "Barang Keluar"
        } else if (status == 2) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, com.tugas.aplikasimonitoringgudang.R.color.hijau_masuk))
            holder.namaStatus.text = "Barang Masuk"
        } else if (status == 3) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, com.tugas.aplikasimonitoringgudang.R.color.putih_smooth))
            holder.namaStatus.text = "Batal Transaksi"
        }
        holder.namaAdmin.text = dataTransaksi.user_nama
        holder.namaBarang.text = dataTransaksi.barang_nama
        holder.namaSupplier.text = dataTransaksi.supplier_nama
        holder.hargaBarang.text = dataTransaksi.harga_barang.toString()
        holder.jumlahTransaksi.text = dataTransaksi.jumlah_barang.toString()
        holder.totalHargaTransaksi.text = dataTransaksi.total_harga_barang.toString()
    }

    override fun getItemCount(): Int = transaksiList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDataTransaksi(transaksiList: List<Transaksi>) {
        this.transaksiList = transaksiList
        notifyDataSetChanged()
    }
}