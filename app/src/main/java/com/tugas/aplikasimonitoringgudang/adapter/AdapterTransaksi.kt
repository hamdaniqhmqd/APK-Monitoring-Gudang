package com.tugas.aplikasimonitoringgudang.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.ItemHeaderBulanTransaksiBinding
import com.tugas.aplikasimonitoringgudang.databinding.ItemTransaksiBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


class AdapterTransaksi(
//    private var transaksiList: List<Transaksi>,
    private val onItemClick: (Transaksi) -> Unit
) : ListAdapter<Any, RecyclerView.ViewHolder>(TransaksiDiffCallback()) {

    enum class ItemType {
        HEADER,
        TRANSAKSI
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Transaksi -> ItemType.TRANSAKSI.ordinal
            else -> ItemType.HEADER.ordinal
        }
    }

    class TransaksiViewHolder private constructor(val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): TransaksiViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTransaksiBinding.inflate(layoutInflater, parent, false)
                return TransaksiViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder private constructor(val binding: ItemHeaderBulanTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHeaderBulanTransaksiBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ItemType.TRANSAKSI.ordinal -> {
                TransaksiViewHolder.from(parent)
            }

            else -> {
                HeaderViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransaksiViewHolder -> {
                val transaksi = getItem(position) as Transaksi
                val formatTanggal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatTanggalDetail = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
                val date = formatTanggal.parse(transaksi.tanggal)
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                val context = holder.itemView.context
                var status = transaksi.status
                if (status == 1) {
                    holder.binding.headerCard.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            com.tugas.aplikasimonitoringgudang.R.color.merah_keluar
                        )
                    )
                    holder.binding.labelCard.text = "Barang Keluar"
                } else if (status == 2) {
                    holder.binding.headerCard.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            com.tugas.aplikasimonitoringgudang.R.color.hijau_masuk
                        )
                    )
                    holder.binding.labelCard.text = "Barang Masuk"
                } else if (status == 3) {
                    holder.binding.headerCard.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            com.tugas.aplikasimonitoringgudang.R.color.putih_smooth
                        )
                    )
                    holder.binding.labelCard.text = "Batal Transaksi"
                }
                holder.binding.NamaAdmin.text = transaksi.user_nama
                holder.binding.namaBarang.text = transaksi.barang_nama
                holder.binding.NamaSupplier.text = transaksi.supplier_nama
                holder.binding.HargaBarang.text = numberFormat.format(transaksi.harga_barang)
                holder.binding.JumlahBarang.text = transaksi.jumlah_barang.toString()
                holder.binding.TotalHargaBarang.text = numberFormat.format(transaksi.total_harga_barang)

                val tanggal = formatTanggalDetail.format(date!!)
                holder.binding.tanggalTransaksi.text = tanggal

                holder.itemView.setOnClickListener {
                    onItemClick(transaksi)
                }
            }

            is HeaderViewHolder -> {
                val header = getItem(position) as String
                val formatTanggal = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val formatBulan = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val date = formatTanggal.parse(header)
                holder.binding.headerText.text = formatBulan.format(date!!)
            }
        }
    }


    class TransaksiDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Transaksi -> {
                    if (newItem is Transaksi) {
                        oldItem.id_transaksi == newItem.id_transaksi
                    } else {
                        false
                    }
                }

                else -> {
                    if (newItem is Transaksi) {
                        false
                    } else {
                        newItem == oldItem
                    }
                }
            }
        }
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Transaksi -> {
                    if (newItem is Transaksi) {
                        (oldItem) == (newItem)
                    } else {
                        false
                    }
                }

                else -> {
                    if (newItem is Transaksi) {
                        false
                    } else {
                        (oldItem) == (newItem)
                    }
                }
            }
        }
    }
}
