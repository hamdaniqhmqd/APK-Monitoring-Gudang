package com.tugas.aplikasimonitoringgudang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.ItemBarangBinding
import com.tugas.aplikasimonitoringgudang.databinding.ItemHeaderBarangBinding
import java.text.NumberFormat
import java.util.Locale

class AdapterBarang(
//    private var barangList: List<Barang>,
    private val onDetailClick: (Barang) -> Unit
) :
    ListAdapter<Any, RecyclerView.ViewHolder>(BarangDiffCallback()) {

    //jenis item yang ada di RecyclerView
    enum class ItemType {
        HEADER,
        BARANG
    }

    //menentukan tipe tampilan item berdasarkan data diposisi nya
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Barang -> ItemType.BARANG.ordinal
            else -> ItemType.HEADER.ordinal
        }
    }

    //menampilkan header dan membuat fungsi instance ViewHolder Header
    class HeaderViewHolder private constructor(val binding: ItemHeaderBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHeaderBarangBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    //menampilkan data barang dan membuat fungsi instance ViewHolder Barang
    class BarangViewHolder private constructor(val binding: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): BarangViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBarangBinding.inflate(layoutInflater, parent, false)
                return BarangViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ItemType.BARANG.ordinal -> {
                BarangViewHolder.from(parent)
            }

            else -> {
                HeaderViewHolder.from(parent)
            }
        }
    }

    //mengisi data ke ViewHolder berdasarkan tipe item
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BarangViewHolder -> {
                val barang = getItem(position) as Barang
                holder.binding.namaBarang.text = barang.nama_barang
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                holder.binding.hargaBarang.text = numberFormat.format(barang.harga_barang)
                holder.binding.stokBarang.text = barang.stok_barang.toString()

                holder.itemView.setOnClickListener {
                    onDetailClick(barang)
                }
            }

            is HeaderViewHolder -> {
                holder.binding.headerText.text = (getItem(position) as String)
            }
        }
    }

    //helper class untuk perubahan data
    class BarangDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Barang -> {
                    if (newItem is Barang) {
                        oldItem.id_barang == newItem.id_barang
                    } else {
                        false
                    }
                }

                else -> {
                    if (newItem is Barang) {
                        false
                    } else {
                        newItem == oldItem
                    }
                }
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Barang -> {
                    if (newItem is Barang) {
                        (oldItem) == (newItem)
                    } else {
                        false
                    }
                }

                else -> {
                    if (newItem is Barang) {
                        false
                    } else {
                        (oldItem) == (newItem)
                    }
                }
            }
        }
    }
}