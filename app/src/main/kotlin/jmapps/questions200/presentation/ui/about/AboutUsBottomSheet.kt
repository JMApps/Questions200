package jmapps.questions200.presentation.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.questions200.R

class AboutUsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var rootAbout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootAbout = inflater.inflate(R.layout.fragment_about_us, container, false)
        return rootAbout
    }
}