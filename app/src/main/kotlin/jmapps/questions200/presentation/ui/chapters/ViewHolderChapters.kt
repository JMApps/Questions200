package jmapps.questions200.presentation.ui.chapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.questions200.R

class ViewHolderChapters(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
    val tvChapterTitle: TextView = itemView.findViewById(R.id.tvChapterTitle)

    fun findItemClick(onItemChapterClick: AdapterChapters.OnItemChapterClick, position: Int) {
        itemView.setOnClickListener {
            onItemChapterClick.onItemClick(position)
        }
    }
}