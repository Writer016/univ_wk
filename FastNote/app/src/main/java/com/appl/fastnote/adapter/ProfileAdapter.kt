package com.appl.fastnote.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fastnote.R
import com.appl.fastnote.WriteActivityProfile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Date

class ProfileAdapter(private var fileList: ArrayList<File>, private val context: Context): RecyclerView.Adapter<ProfileAdapter.ItemViewHolder>() {
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var str: String?
        try{
            val bufferedReader = BufferedReader(FileReader("${context.filesDir}/${fileList[position].name}"))
            str = bufferedReader.readLine()
            bufferedReader.close()
            if(str=="def"){
                Glide.with(context).load(R.mipmap.ic_default).into(holder.fileImageView)
            }
            else{
                Glide.with(context).load(str).into(holder.fileImageView)
            }
        }catch(e: Exception){
        }

        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
        holder.titleView.text = fileList[position].name.subSequence(0, fileList[position].name.length-5)
        holder.dateView.text = sdf.format(Date(fileList[position].lastModified()))

        holder.layoutView.setOnClickListener {
            val intent = Intent(view.context, WriteActivityProfile::class.java)
            intent.putExtra("pathData", fileList[position].name)
            ContextCompat.startActivity(view.context, intent, null)
        }

        holder.layoutView.setOnLongClickListener {
            AlertDialog.Builder(view.context)
                .setMessage("Are you want to delete this Note?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
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
        var layoutView = view.findViewById<ConstraintLayout>(R.id.itemProfile_layout)
        var titleView = view.findViewById<TextView>(R.id.itemProfile_title)
        var dateView = view.findViewById<TextView>(R.id.itemProfile_date)
        var fileImageView = view.findViewById<ImageView>(R.id.itemProfile_image)
    }

    fun saveChanges() {
        //<< 메모 리스트 가져오기 LOGIC >>
        var fileArr = context.filesDir.listFiles {file->
            file.name.endsWith("p.txt")
        } //가져온 메모 리스트를 fileArr로 초기화

        //<< 가져온 리스트 정렬 LOGIC >>
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