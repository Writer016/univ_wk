package com.appl.fastnote.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fastnote.R
import com.appl.fastnote.WriteActivityNormal
import java.io.File
import java.util.Date

class NormalAdapter(private var fileList: ArrayList<File>, val context: Context): RecyclerView.Adapter<NormalAdapter.ItemViewHolder>() {
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_normal, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")

        holder.titleView?.text = fileList[position].name.subSequence(0, fileList[position].name.length-5)
        holder.dateView.text = sdf.format(Date(fileList[position].lastModified()))
        holder.layoutView.setOnClickListener {
            val intent = Intent(view.context, WriteActivityNormal::class.java)
            intent.putExtra("pathData", fileList[position].name)
            startActivity(view.context, intent, null)
        }

        holder.layoutView.setOnLongClickListener {
            AlertDialog.Builder(view.context)
                .setMessage("Are you want to delete this Note?")
                .setPositiveButton("Yes",  DialogInterface.OnClickListener { _, _ ->
                    fileList[position].delete()
                    fileList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeRemoved(position, itemCount)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { _, _ ->  })
                .show()

            true
        }
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val layoutView = view.findViewById<ConstraintLayout>(R.id.itemNormal_layout)
        var titleView: TextView? = view.findViewById(R.id.itemNormal_title)
        var dateView = view.findViewById<TextView>(R.id.itemNormal_date)
    }

    fun saveChanges() {
        var fileArr = context.filesDir.listFiles {file->
            file.name.endsWith("n.txt")
        }
        fileArr = if(fileArr==null){
            arrayOf()
        } else{
            val comparator: Comparator<File> = compareBy { it.lastModified() }
            fileArr.sortedWith(comparator).reversed().toTypedArray()
        }

        this.fileList= fileArr.toCollection(ArrayList())
        notifyDataSetChanged()
    }
}