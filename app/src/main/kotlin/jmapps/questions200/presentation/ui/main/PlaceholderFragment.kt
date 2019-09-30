package jmapps.questions200.presentation.ui.main

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import jmapps.questions200.R
import jmapps.questions200.data.database.DatabaseAsset
import jmapps.questions200.data.file.TypeFace
import jmapps.questions200.presentation.mvp.main.MainContract
import jmapps.questions200.presentation.mvp.main.MainPresenterImpl
import kotlinx.android.synthetic.main.fragment_main.view.*

class PlaceholderFragment : Fragment(), MainContract.MainView,
    CompoundButton.OnCheckedChangeListener, View.OnClickListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var rootMain: View
    private var sectionNumber: Int? = null

    private lateinit var mainPresenterImpl: MainPresenterImpl

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var backgroundColor: Int? = null
    private var textColor: Int? = null
    private var selectFont: String? = null
    private var textSize: Float? = null

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootMain = inflater.inflate(R.layout.fragment_main, container, false)

        sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)

        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        val database: SQLiteDatabase = DatabaseAsset(context).readableDatabase

        mainPresenterImpl = MainPresenterImpl(context, sectionNumber, this, database)
        mainPresenterImpl.showContentFromDatabase()

        rootMain.tbFavorites.isChecked =
            preferences.getBoolean("key_main_favorite_$sectionNumber", false)
        rootMain.btnCopyContent.setOnClickListener(this)
        rootMain.tbFavorites.setOnCheckedChangeListener(this)

        backgroundMode()
        fontMode()
        textSize()

        return rootMain
    }

    override fun onClick(v: View?) {
        mainPresenterImpl.copyContent()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mainPresenterImpl.addRemoveFavorite(isChecked)
    }

    override fun showDatabaseExceptions(e: String) {
        Toast.makeText(context, e, Toast.LENGTH_LONG).show()
    }

    override fun showQuestionNumber(number: String) {
        rootMain.tvQuestionNumber.text = number
    }

    override fun showQuestionContent(content: String) {
        rootMain.tvQuestionContent.movementMethod = LinkMovementMethod.getInstance()
        rootMain.tvQuestionContent.setText(
            mainPresenterImpl.stringBuilder(content),
            TextView.BufferType.SPANNABLE
        )
    }

    override fun showAnswerContent(content: String) {
        rootMain.tvAnswerContent.movementMethod = LinkMovementMethod.getInstance()
        rootMain.tvAnswerContent.setText(
            mainPresenterImpl.stringBuilder(content),
            TextView.BufferType.SPANNABLE
        )
    }

    override fun showFavoriteState(state: Boolean) {
        if (state) {
            Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Удалено из избранного", Toast.LENGTH_LONG).show()
        }
    }

    override fun saveFavoriteNumber(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    override fun showCopyToast() {
        Toast.makeText(context, "Скопировано в буфер", Toast.LENGTH_LONG).show()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        backgroundMode()
        fontMode()
        textSize()
    }

    private fun backgroundMode() {
        val whiteMode = preferences.getBoolean("key_white_state", true)
        val sepiaMode = preferences.getBoolean("key_sepia_state", false)
        val nightMode = preferences.getBoolean("key_night_mode", false)

        when (true) {
            whiteMode -> {
                backgroundColor = preferences.getInt(
                    "key_background_white",
                    Color.argb(255, 244, 244, 244)
                )
                textColor = preferences.getInt(
                    "key_text_white",
                    Color.argb(255, 87, 87, 87)
                )
            }
            sepiaMode -> {
                backgroundColor = preferences.getInt(
                    "key_background_sepia",
                    Color.argb(255, 222, 210, 112)
                )
                textColor = preferences.getInt(
                    "key_text_sepia",
                    Color.argb(255, 112, 112, 112)
                )
            }
            nightMode -> {
                backgroundColor = preferences.getInt(
                    "key_background_night",
                    Color.argb(255, 44, 44, 44)
                )
                textColor = preferences.getInt(
                    "key_text_night",
                    Color.argb(255, 184, 184, 184)
                )

            }
        }
        rootMain.mainConstraint.setBackgroundColor(backgroundColor!!)
        rootMain.tvQuestionContent.setTextColor(textColor!!)
        rootMain.tvAnswerContent.setTextColor(textColor!!)
    }

    private fun fontMode() {
        val fontOne = preferences.getBoolean("key_font_one", true)
        val fontTwo = preferences.getBoolean("key_font_two", false)
        val fontThree = preferences.getBoolean("key_font_three", false)

        when (true) {
            fontOne -> {
                selectFont = "fonts/gilroy.ttf"
            }
            fontTwo -> {
                selectFont = "fonts/serif.ttf"
            }
            fontThree -> {
                selectFont = "fonts/roboto.ttf"
            }
        }
        rootMain.tvQuestionContent.typeface = TypeFace()[context!!, selectFont!!]
        rootMain.tvAnswerContent.typeface = TypeFace()[context!!, selectFont!!]
    }

    private fun textSize() {

        when (preferences.getInt("key_text_size_progress", 2)) {
            0 -> {
                textSize = 14f
            }
            1 -> {
                textSize = 16f
            }
            2 -> {
                textSize = 18f
            }
            3 -> {
                textSize = 20f
            }
            4 -> {
                textSize = 24f
            }
            5 -> {
                textSize = 30f
            }
        }
        rootMain.tvQuestionContent.textSize = textSize!!
        rootMain.tvAnswerContent.textSize = textSize!!
    }
}