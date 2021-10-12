package com.hfad.androidmaterialdesign.ui.recycler_activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hfad.androidmaterialdesign.ui.recycler_activity.model.Data

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: Pair<Data, Boolean>)
}