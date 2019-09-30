package jmapps.questions200.presentation.ui.favorites

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.questions200.R

class ViewHolderFavorites(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvFavoriteNumber: TextView = itemView.findViewById(R.id.tvFavoriteNumber)
    val tvFavoriteTitle: TextView = itemView.findViewById(R.id.tvFavoriteTitle)

    fun findItemClick(onItemFavoriteClick: AdapterFavorites.OnItemFavoriteClick, idPosition: Int) {
        itemView.setOnClickListener {
            onItemFavoriteClick.onItemClick(idPosition)
        }
    }
}