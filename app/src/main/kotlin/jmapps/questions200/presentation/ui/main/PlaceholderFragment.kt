package jmapps.questions200.presentation.ui.main

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import jmapps.questions200.R
import jmapps.questions200.data.database.DatabaseAsset
import jmapps.questions200.data.file.TypeFace
import jmapps.questions200.presentation.mvp.main.MainContract
import jmapps.questions200.presentation.mvp.main.MainPresenterImpl
import jmapps.questions200.presentation.mvp.settings.SettingsContract
import jmapps.questions200.presentation.mvp.settings.SettingsPresenterImpl
import kotlinx.android.synthetic.main.fragment_main.view.*

class PlaceholderFragment : Fragment(), MainContract.MainView,
    CompoundButton.OnCheckedChangeListener, View.OnClickListener,
    SharedPreferences.OnSharedPreferenceChangeListener, SettingsContract.SettingsView {

    private lateinit var rootMain: View
    private var sectionNumber: Int? = null

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var settingsPresenterImpl: SettingsPresenterImpl

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootMain = inflater.inflate(R.layout.fragment_main, container, false)

        sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener(this)

        val database: SQLiteDatabase = DatabaseAsset(context).readableDatabase

        mainPresenterImpl = MainPresenterImpl(context, sectionNumber, this, database)
        settingsPresenterImpl = SettingsPresenterImpl(this)
        mainPresenterImpl.showContentFromDatabase()

        rootMain.tbFavorites.isChecked = preferences.getBoolean("key_main_favorite_$sectionNumber", false)
        rootMain.btnCopyContent.setOnClickListener(this)
        rootMain.tbFavorites.setOnCheckedChangeListener(this)

        backgroundMode()
        fontMode()
        sizeMode()

        return rootMain
    }

    override fun showAnswerContent(content: String) {
        rootMain.tvAnswerContent.movementMethod = LinkMovementMethod.getInstance()
        rootMain.tvAnswerContent.setText(mainPresenterImpl.stringBuilder(content),
            TextView.BufferType.SPANNABLE
        )
    }

    override fun onClick(v: View?) {
        mainPresenterImpl.copyContent()
    }

    override fun showCopyToast() {
        Toast.makeText(context, "Скопировано в буфер", Toast.LENGTH_LONG).show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        mainPresenterImpl.addRemoveFavorite(isChecked)
    }

    override fun showFavoriteState(state: Boolean) {
        if (state) {
            Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Удалено из избранного", Toast.LENGTH_LONG).show()
        }
    }

    override fun showDatabaseExceptions(e: String) {
        Toast.makeText(context, e, Toast.LENGTH_LONG).show()
    }

    override fun colorMode(backgroundColor: Int, textColor: Int) {
        rootMain.mainConstraint.setBackgroundColor(backgroundColor)
        rootMain.tvAnswerContent.setTextColor(textColor)
    }

    override fun typeFace(nameTypeFace: String) {
        rootMain.tvAnswerContent.typeface = TypeFace()[context, nameTypeFace]
    }

    override fun textSize(textSize: Float) {
        rootMain.tvAnswerContent.textSize = textSize
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        backgroundMode()
        fontMode()
        sizeMode()
    }

    override fun saveFavoriteNumber(keyFavorite: String, state: Boolean) {
        editor.putBoolean(keyFavorite, state).apply()
    }

    private fun backgroundMode() {
        val whiteMode = preferences.getBoolean("key_white_state", true)
        val sepiaMode = preferences.getBoolean("key_sepia_state", false)
        val nightMode = preferences.getBoolean("key_night_mode_state", false)

        when (true) {

            whiteMode -> settingsPresenterImpl.backgroundMode(1)

            sepiaMode -> settingsPresenterImpl.backgroundMode(2)

            nightMode -> settingsPresenterImpl.backgroundMode(3)
        }
    }

    private fun fontMode() {
        val fontOne = preferences.getBoolean("key_font_one_state", true)
        val fontTwo = preferences.getBoolean("key_font_two_state", false)
        val fontThree = preferences.getBoolean("key_font_three_state", false)

        when (true) {

            fontOne -> settingsPresenterImpl.typeFaceMode(1)

            fontTwo -> settingsPresenterImpl.typeFaceMode(2)

            fontThree -> settingsPresenterImpl.typeFaceMode(3)
        }
    }

    private fun sizeMode() {
        settingsPresenterImpl.textSizeMode(preferences.getInt("key_text_size_progress", 2))
    }
}