package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.ItemSupplierBinding

class AdapterSupplier(
    private var supplierList: List<Supplier>,
    private val onItemClick: (Supplier) -> Unit
) : RecyclerView.Adapter<AdapterSupplier.SupplierViewHolder>() {

    // ViewHolder yang mengikat item dengan binding
    inner class SupplierViewHolder(val binding: ItemSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nama = binding.supplierName
        val nik = binding.supplierNik
        val no_hp = binding.supplierPhone

        init {
            itemView.setOnClickListener {
                onItemClick(supplierList[adapterPosition])
            }
        }
    }


    // Membuat ViewHolder baru dengan layout item_supplier.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val binding = ItemSupplierBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SupplierViewHolder(binding)
    }

    // Mengikat data ke view dalam RecyclerView
    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val dataSupplier = supplierList[position]
        holder.nama.text = dataSupplier.nama_supplier
        holder.no_hp.text = dataSupplier.no_hp_supplier.toString()
        holder.nik.text = dataSupplier.nik_supplier.toString()

        // Menangani klik pada item
        holder.itemView.setOnClickListener {
            onItemClick(dataSupplier)
        }
    }

    // Menghitung jumlah item di list
    override fun getItemCount(): Int = supplierList.size

    // Memperbarui data dalam RecyclerView
    @SuppressLint("NotifyDataSetChanged")
    fun setDataSupplier(suppliers: List<Supplier>) {
        this.supplierList = suppliers
        notifyDataSetChanged()
    }
}
