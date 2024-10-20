package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
//import com.tugas.aplikasimonitoringgudang.databinding.ItemTransaksiBinding
//import com.tugas.aplikasimonitoringgudang.ui.transaksi.DetailTransaksiActivity

//class AdapterTransaksi(private var onItemClick: (Transaksi) -> Unit) :
//    RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>() {
//
//    private var transaksi = listOf<Transaksi>()
//
//    inner class TransaksiViewHolder(private val binding: ItemTransaksiBinding) :
//        RecyclerView.ViewHolder(binding.root) {
////        val nama = binding.textViewNamaTransaksi
////        val quantity = binding.textViewJumlahTransaksi
//        fun bind(transaksi: Transaksi) {
//            onItemClick(transaksi)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
//        val binding = ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return TransaksiViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
//        val dataTransaksi = transaksi[position]
////        holder.nama.text = dataTransaksi.name
////        holder.quantity.text = dataTransaksi.quantity.toString()
//
//        holder.itemView.setOnClickListener {
//            holder.bind(dataTransaksi)
//            val data = holder.itemView.context
//            val intent = Intent(data, DetailTransaksiActivity::class.java)
////            intent.putExtra("EXTRA_ID", dataTransaksi.id)
////            intent.putExtra("EXTRA_NAME", dataTransaksi.name)
////            intent.putExtra("EXTRA_QUANTITY", dataTransaksi.quantity)
//            data.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount(): Int = transaksi.size
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setDataTransaksi(transaksi: List<Transaksi>) {
//        this.transaksi = transaksi
//        notifyDataSetChanged()
//    }
//}