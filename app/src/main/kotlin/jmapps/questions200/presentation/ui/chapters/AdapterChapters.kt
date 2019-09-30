package jmapps.questions200.presentation.ui.chapters

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.questions200.R

class AdapterChapters(
    private val modelChapters: MutableList<ModelChapters>,
    private val onItemChapterClick: OnItemChapterClick) :
    RecyclerView.Adapter<ViewHolderChapters>() {

    interface OnItemChapterClick {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChapters {
        return ViewHolderChapters(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chapter, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return modelChapters.size
    }

    override fun onBindViewHolder(holder: ViewHolderChapters, position: Int) {

        val strChapterNumber = modelChapters[position].strChapterNumber
        val strChapterTitle = modelChapters[position].strChapterTitle

        holder.tvChapterNumber.text = strChapterNumber
        holder.tvChapterTitle.text = Html.fromHtml(strChapterTitle)
        holder.findItemClick(onItemChapterClick, position)
    }
}