package com.uniolco.weathapp.ui.favorite.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniolco.weathapp.R
import com.uniolco.weathapp.data.db.entity.favorite.Locations
import com.uniolco.weathapp.ui.base.RecyclerAdapter
import com.uniolco.weathapp.ui.base.ScopeFragment
import com.uniolco.weathapp.ui.base.SharedViewModel
import com.uniolco.weathapp.ui.base.SwipeToDeleteCallback
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.*
import kotlinx.android.synthetic.main.favorite_list_weather_fragment.recyclerView
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.android.synthetic.main.item_favorite_list_weather.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

class FavoriteListWeatherFragment : ScopeFragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var viewModel: FavoriteListWeatherViewModel
    private val viewModelFactoryInstanceFactory:
            ((String) -> FavoriteListWeatherViewModelFactory) by factory()
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val email = model.email.value.toString()
        viewModel = ViewModelProvider(this, viewModelFactoryInstanceFactory(email)).get(FavoriteListWeatherViewModel::class.java)
        // TODO: Use the ViewModel
        bindUI()
    }

    private fun bindUI() = launch {
        val favorites = viewModel.favorites.await()

        favorites.observe(viewLifecycleOwner, Observer { favorits ->
            if(favorits == null) return@Observer
            initRecyclerView(favorits/*.toItems()*/)
        })
        updateTitle()
        updateSubtitle()
        deleteAll()
    }

    private fun updateTitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Favorite"
    }

    private fun updateSubtitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
    }

    private suspend fun deleteAll(){
        clearAll_button.setOnClickListener {
            launch {
                viewModel.deleteAllLocations()
            }
            Toast.makeText(clearAll_button.context, "Deleted all favorite places", Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var adapterRec: RecyclerAdapter

    private fun initRecyclerView(items: List<Locations>){
/*        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }*/

        recyclerView.layoutManager = LinearLayoutManager(this@FavoriteListWeatherFragment.context)
        adapterRec = RecyclerAdapter(items.toMutableList())
        recyclerView.adapter = adapterRec


/*        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoriteListWeatherFragment.context)
            adapter = groupAdapter
        }*/

        val swipeHandler = object: SwipeToDeleteCallback(layoutInflater.context){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as RecyclerAdapter
                val loc = adapter.removeAt(viewHolder.adapterPosition)
                deleteLocation(loc)
                Log.d("ADADADADADDADAD", viewHolder.adapterPosition.toString())
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

/*        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FavoriteListItem)?.let {
                navigateToFavoriteDetail(it.locations.location.name, view)
            }
            Log.d("ITEM", item.id.toString() + "||")
        }*/
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