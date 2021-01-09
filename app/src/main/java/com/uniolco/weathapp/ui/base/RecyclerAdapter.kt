package com.uniolco.weathapp.ui.base

import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.internal.inflate
import com.uniolco.weathapp.ui.favorite.list.FavoriteListWeatherFragmentDirections
import kotlinx.android.synthetic.main.item_future_weather.view.textView_cityName

class RecyclerAdapter(private val favorites: MutableList<Locations>):
    RecyclerView.Adapter<RecyclerAdapter.ItemLocationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ItemLocationHolder {
        val inflatedView = parent.inflate(R.layout.item_favorite_list_weather, false)
        return ItemLocationHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ItemLocationHolder, position: Int) {
        val itemFavorite = favorites[position]
        holder.bindFavorite(itemFavorite)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }



    class ItemLocationHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var favorite: Locations? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            navigateToFavoriteDetail(favorite?.location?.name.toString(), view)
        }

        private fun navigateToFavoriteDetail(locationName: String, view: View){
            val actionDetail = FavoriteListWeatherFragmentDirections.actionFavoriteListWeatherFragmentToFavoriteDetailWeatherFragment(locationName)
            Navigation.findNavController(view).navigate(actionDetail)
        }

        fun bindFavorite(favorite: Locations){
            this.favorite = favorite
            view.textView_cityName.text = "${favorite.location.country}\n${favorite.location.name}"
        }
    }

    fun removeAt(position: Int): Locations{
        val ret = favorites[position]
        favorites.removeAt(position)
        notifyItemRemoved(position)
        return ret
    }

}