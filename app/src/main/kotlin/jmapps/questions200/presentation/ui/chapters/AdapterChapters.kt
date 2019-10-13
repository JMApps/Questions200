package jmapps.questions200.presentation.ui.chapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.questions200.R

class AdapterChapters(
    private var modelChapters: MutableList<ModelChapters>,
    private val onItemChapterClick: OnItemChapterClick) :
    RecyclerView.Adapter<ViewHolderChapters>(), Filterable {

    private var mainChapters: List<ModelChapters>? = null

    init {
        this.mainChapters = modelChapters
    }

    interface OnItemChapterClick {
        fun onItemClick(idPosition: Int)
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

        val strIdPosition = modelChapters[position].strIdPosition
        val strChapterNumber = modelChapters[position].strChapterNumber
        val strChapterTitle = modelChapters[position].strChapterTitle

        holder.tvChapterNumber.text = strChapterNumber
        holder.tvChapterTitle.text = Html.fromHtml(strChapterTitle)
        holder.findItemClick(onItemChapterClick, strIdPosition!!.toInt() - 1)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                modelChapters = if (charString.isEmpty()) {
                    mainChapters as MutableList<ModelChapters>
                } else {
                    val filteredList = ArrayList<ModelChapters>()
                    for (row in mainChapters!!) {
                        if (row.strChapterTitle!!.toLowerCase().contains(charString.toLowerCase()) ||
                            row.strChapterNumber!!.contains(
                                charSequence
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = modelChapters
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                modelChapters = filterResults.values as ArrayList<ModelChapters>
                notifyDataSetChanged()
            }
        }
    }
}