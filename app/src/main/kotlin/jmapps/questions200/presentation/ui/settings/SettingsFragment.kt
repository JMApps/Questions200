package jmapps.questions200.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.questions200.R
import jmapps.questions200.presentation.mvp.settings.SettingsContract
import jmapps.questions200.presentation.mvp.settings.SettingsPresenterImpl
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : BottomSheetDialogFragment(), SettingsContract.SettingsView,
    CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private lateinit var rootSettings: View
    private lateinit var settingsPresenterImpl: SettingsPresenterImpl

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.fragment_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        settingsPresenterImpl = SettingsPresenterImpl(this)

        rootSettings.rbWhiteMode.isChecked = preferences.getBoolean("key_white_state", true)
        rootSettings.rbSepiaMode.isChecked = preferences.getBoolean("key_sepia_state", false)
        rootSettings.rbNightMode.isChecked = preferences.getBoolean("key_night_mode", false)

        rootSettings.rbWhiteMode.setOnCheckedChangeListener(this)
        rootSettings.rbSepiaMode.setOnCheckedChangeListener(this)
        rootSettings.rbNightMode.setOnCheckedChangeListener(this)

        rootSettings.rbFontOne.isChecked = preferences.getBoolean("key_font_one", true)
        rootSettings.rbFontTwo.isChecked = preferences.getBoolean("key_font_two", false)
        rootSettings.rbFontThree.isChecked = preferences.getBoolean("key_font_three", false)

        rootSettings.rbFontOne.setOnCheckedChangeListener(this)
        rootSettings.rbFontTwo.setOnCheckedChangeListener(this)
        rootSettings.rbFontThree.setOnCheckedChangeListener(this)

        rootSettings.sbTextSize.progress = preferences.getInt("key_text_size_progress", 2)
        rootSettings.sbTextSize.setOnSeekBarChangeListener(this)

        return rootSettings
    }

    override fun whiteMode(backgroundColor: Int, textColor: Int) {
        editor.putInt("key_background_white", backgroundColor).apply()
        editor.putInt("key_text_white", textColor).apply()
    }

    override fun sepiaMode(backgroundColor: Int, textColor: Int) {
        editor.putInt("key_background_sepia", backgroundColor).apply()
        editor.putInt("key_text_sepia", textColor).apply()
    }

    override fun nightMode(backgroundColor: Int, textColor: Int) {
        editor.putInt("key_background_night", backgroundColor).apply()
        editor.putInt("key_text_night", textColor).apply()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.rbWhiteMode -> {
                if (isChecked) settingsPresenterImpl.backgroundMode(1)
                editor.putBoolean("key_white_state", isChecked).apply()
            }
            R.id.rbSepiaMode -> {
                if (isChecked) settingsPresenterImpl.backgroundMode(2)
                editor.putBoolean("key_sepia_state", isChecked).apply()
            }
            R.id.rbNightMode -> {
                if (isChecked) settingsPresenterImpl.backgroundMode(3)
                editor.putBoolean("key_night_mode", isChecked).apply()
            }

            R.id.rbFontOne -> {
                editor.putBoolean("key_font_one", isChecked).apply()
            }
            R.id.rbFontTwo -> {
                editor.putBoolean("key_font_two", isChecked).apply()
            }
            R.id.rbFontThree -> {
                editor.putBoolean("key_font_three", isChecked).apply()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        editor.putInt("key_text_size_progress", progress).apply()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}