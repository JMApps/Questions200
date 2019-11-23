package jmapps.questions200.presentation.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.questions200.R
import jmapps.questions200.data.database.DatabaseLists
import kotlinx.android.synthetic.main.fragment_favorites.view.*

class FavoritesFragment : BottomSheetDialogFragment(), AdapterFavorites.OnItemFavoriteClick {

    private lateinit var rootFavorites: View
    private lateinit var favorites: MutableList<ModelFavorites>
    private lateinit var adapterFavorites: AdapterFavorites
    private lateinit var getFavoriteItem: GetFavoriteItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootFavorites = inflater.inflate(R.layout.fragment_favorites, container, false)

        favorites = DatabaseLists(context!!).getFavoriteList

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootFavorites.rvListFavorites.layoutManager = verticalLayout

        adapterFavorites = AdapterFavorites(favorites, this)
        rootFavorites.rvListFavorites.adapter = adapterFavorites

        if (favorites.size < 1) {
            rootFavorites.tvIsFavoritesEmpty.visibility = View.VISIBLE
        } else {
            rootFavorites.tvIsFavoritesEmpty.visibility = View.GONE
        }
        return rootFavorites
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GetFavoriteItem) {
            getFavoriteItem = context
        } else {
            throw RuntimeException("$context must implement SetCurrentPageChapter")
        }
    }

    override fun onItemClick(idPosition: Int) {
        getFavoriteItem.getItemPosition(idPosition)
        dialog?.dismiss()
    }

    interface GetFavoriteItem {
        fun getItemPosition(position: Int)
    }
}