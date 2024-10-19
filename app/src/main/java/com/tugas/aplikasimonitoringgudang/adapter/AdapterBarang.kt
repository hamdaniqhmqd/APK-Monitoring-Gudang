package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.ItemBarangBinding

class AdapterBarang(private var onItemClick: (Barang) -> Unit) :
    RecyclerView.Adapter<AdapterBarang.BarangViewHolder>() {

    private var barang = listOf<Barang>()

    inner class BarangViewHolder(private val binding: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val id = binding.idBarang
        val nama = binding.namaBarang
//        fun bind(barang: Barang) {
//            onItemClick(barang)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = ItemBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val dataBarang = barang[position]
        holder.id.text = dataBarang.id_barang.toString()
        holder.nama.text = dataBarang.nama_barang

//        holder.itemView.setOnClickListener {
//            holder.bind(dataBarang)
//            val data = holder.itemView.context
//            val intent = Intent(data, DetailBarangActivity::class.java)
//            intent.putExtra("EXTRA_ID", dataBarang.id)
//            intent.putExtra("EXTRA_NAME", dataBarang.name)
//            intent.putExtra("EXTRA_QUANTITY", dataBarang.quantity)
//            data.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int = barang.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDataBarang(barang: List<Barang>) {
        this.barang = barang
        notifyDataSetChanged()
    }
}