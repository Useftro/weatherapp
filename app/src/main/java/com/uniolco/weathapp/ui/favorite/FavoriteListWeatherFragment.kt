package com.uniolco.weathapp.ui.favorite

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.ui.base.ScopeFragment
import com.uniolco.weathapp.ui.favorite.FavoriteListWeatherFragmentDirections.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.*
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.recyclerView
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.android.synthetic.main.item_favorite_list_weather.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FavoriteListWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: FavoriteListWeatherViewModelFactory by instance()
    private lateinit var viewModel: FavoriteListWeatherViewModel

    companion object {
        fun newInstance() = FavoriteListWeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteListWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        bindUI()
    }

    private fun bindUI() = launch {
        val favorites = viewModel.favorites.await()
//            favorites.observe(viewLifecycleOwner, Observer { favorite ->
//                if (favorite == null) return@Observer
//                Log.d("FAVORITE", favorite.toString())
//                favorite_text_View.text = favorite.toString()
//            })
        initRecyclerView(favorites.toItems())
        updateTitle()
    }

    private fun updateTitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Favorite"
    }

    private fun initRecyclerView(items: List<FavoriteListItem>){
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }


        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoriteListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FavoriteListItem)?.let {
                navigateToFavoriteDetail(it.locations.location.name, view)
            }
            Log.d("ITEM", item.id.toString() + "||")
        }


    }


    private fun List<Locations>.toItems(): List<FavoriteListItem>{
        return this.map {
            FavoriteListItem(it)
        }
    }

    private fun deleteLocation(locations: Locations){
        launch {
            viewModel.deleteLocation(locations)
        }
    }

    private fun navigateToFavoriteDetail(locationName: String, view: View){
        val actionDetail = FavoriteListWeatherFragmentDirections.actionFavoriteListWeatherFragmentToFavoriteDetailWeatherFragment(locationName)
        Navigation.findNavController(view).navigate(actionDetail)
    }

}