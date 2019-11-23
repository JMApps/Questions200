package jmapps.questions200.presentation.ui.chapters

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.questions200.R
import jmapps.questions200.data.database.DatabaseLists
import kotlinx.android.synthetic.main.fragment_chapters.view.*

class ChaptersBottomSheet : BottomSheetDialogFragment(), AdapterChapters.OnItemChapterClick,
    TextWatcher {

    private lateinit var rootChapters: View
    private lateinit var chapters: MutableList<ModelChapters>
    private lateinit var adapterChapters: AdapterChapters
    private lateinit var getChapterItem: GetChapterItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootChapters = inflater.inflate(R.layout.fragment_chapters, container, false)

        chapters = DatabaseLists(context!!).getChapterList

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootChapters.rvListChapters.layoutManager = verticalLayout

        adapterChapters = AdapterChapters(chapters, this)
        rootChapters.rvListChapters.adapter = adapterChapters

        rootChapters.etSearchChapter.addTextChangedListener(this)
        return rootChapters
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        adapterChapters.filter.filter(s)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GetChapterItem) {
            getChapterItem = context
        } else {
            throw RuntimeException("$context must implement SetCurrentPageChapter")
        }
    }

    override fun onItemClick(idPosition: Int) {
        getChapterItem.getItemPosition(idPosition)
        dialog?.dismiss()
    }

    interface GetChapterItem {
        fun getItemPosition(position: Int)
    }
}