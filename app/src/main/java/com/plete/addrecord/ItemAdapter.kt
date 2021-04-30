package com.plete.addrecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<EMPModel>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.llmain
        val tvNama = view.tvNama
        val tvEmail = view.tvEmail
        val ivEdit = view.ivEdit
        val ivDelete = view.ivDelete
        val ivExpands = view.ivExpands
    }

    /**
     * method untuk membuat viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
            R.layout.item_row, parent, false
            )
        )
    }

    /**
     * hitung jumlah list model di emp
     */
    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = items.get(position)
        holder.tvNama.text = items.nama
        holder.tvEmail.text = items.email

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.lightOrange))
        } else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.darkOrange))
        }

        holder.ivDelete.setOnClickListener {
            if(context is MainActivity){
                context.deleteRecordAlertDialog(items)
            }
        }
        holder.ivEdit.setOnClickListener {
            if (context is MainActivity){
                context.updateRecordDialog(items)
            }
        }

        holder.ivExpands.setOnClickListener {
            if (context is MainActivity){
                context.Expands(items)
            }
        }
    }
}