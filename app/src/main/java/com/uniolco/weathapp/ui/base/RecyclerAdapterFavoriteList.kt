package com.uniolco.weathapp.ui.base

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.internal.inflate
import com.uniolco.weathapp.ui.favorite.list.FavoriteListWeatherFragmentDirections
import kotlinx.android.synthetic.main.item_future_weather.view.textView_cityName
import java.util.*

class RecyclerAdapterFavoriteList(private val favorites: MutableList<Locations>):
    RecyclerView.Adapter<RecyclerAdapterFavoriteList.ItemLocationHolder>(), Filterable {

    var locationFilterList = mutableListOf<Locations>()

    init {
        locationFilterList = favorites
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterFavoriteList.ItemLocationHolder {
        val inflatedView = parent.inflate(R.layout.item_favorite_list_weather, false)
        return ItemLocationHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterFavoriteList.ItemLocationHolder, position: Int) {
        val itemFavorite = locationFilterList[position]
        holder.bindFavorite(itemFavorite)
    }

    override fun getItemCount(): Int {
        return locationFilterList.size
    }


    inner class ItemLocationHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, View.OnLongClickListener {

        private var view: View = v
        private var favorite: Locations? = null

        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
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

        override fun onLongClick(v: View?): Boolean {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Today in ${favorite?.location?.name.toString()}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(view.context, shareIntent, null)
            return true
        }


    }

    fun removeAt(position: Int): Locations{
        val ret = favorites[position]
        favorites.removeAt(position)
        notifyItemRemoved(position)
        return ret
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    locationFilterList = favorites
                }
                else{
                    val resList = mutableListOf<Locations>()
                    for (item in favorites){
                        if(item.location.name.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))){
                            resList.add(item)
                        }
                    }
                    locationFilterList = resList
                }
                val filterRes = FilterResults()
                filterRes.values = locationFilterList
                return filterRes
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                locationFilterList = results?.values as MutableList<Locations>
                notifyDataSetChanged()
            }


        }
    }

}