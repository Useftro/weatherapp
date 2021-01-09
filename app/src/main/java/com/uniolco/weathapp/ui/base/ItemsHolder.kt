package com.uniolco.weathapp.ui.base

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.ui.favorite.list.FavoriteListItem

class ItemsHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

    private var view: View = v
    private var favorite: FavoriteListItem? = null

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.d("RecyclerView", "CLICK!")
    }
}