package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.ItemSupplierBinding

class AdapterSupplier(
    private val onItemClick: (Supplier) -> Unit
) : ListAdapter<Supplier, AdapterSupplier.SupplierViewHolder>(SupplierDiffCallback()) {

    // ViewHolder untuk menghubungkan tampilan dari item supplierbinding yg berupa elemen, nma nik no hp
    inner class SupplierViewHolder(val binding: ItemSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nama = binding.supplierName
        val nik = binding.supplierNik
        val no_hp = binding.supplierPhone

        init {
            itemView.setOnClickListener {
                onItemClick(getItem(adapterPosition))
            }
        }
    }

    // Membuat ViewHolder baru dengan layout item_supplier. untuk menghubungkan layout item supplier,xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val binding = ItemSupplierBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SupplierViewHolder(binding)
    }

    // Mengikat data ke view dalam RecyclerView
    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val dataSupplier = getItem(position)
        holder.nama.text = dataSupplier.nama_supplier
        holder.no_hp.text = dataSupplier.no_hp_supplier.toString()
        holder.nik.text = dataSupplier.nik_supplier.toString()

        // Menangani klik pada item
        holder.itemView.setOnClickListener {
            onItemClick(dataSupplier)
        }
    }

    // DiffUtil implementation for Supplier. untuk membandingkan dua item berdasarkan nik.
    class SupplierDiffCallback : DiffUtil.ItemCallback<Supplier>() {
        override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            return oldItem.nik_supplier == newItem.nik_supplier
        }

        override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            return oldItem == newItem
        }
    }
}

