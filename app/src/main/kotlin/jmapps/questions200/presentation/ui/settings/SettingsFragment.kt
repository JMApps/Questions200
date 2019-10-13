package jmapps.questions200.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.questions200.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener,
    RadioGroup.OnCheckedChangeListener {

    private lateinit var rootSettings: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.fragment_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        rootSettings.rbWhiteMode.isChecked = preferences.getBoolean("key_white_state", true)
        rootSettings.rbSepiaMode.isChecked = preferences.getBoolean("key_sepia_state", false)
        rootSettings.rbNightMode.isChecked = preferences.getBoolean("key_night_mode_state", false)

        rootSettings.rbFontOne.isChecked = preferences.getBoolean("key_font_one_state", true)
        rootSettings.rbFontTwo.isChecked = preferences.getBoolean("key_font_two_state", false)
        rootSettings.rbFontThree.isChecked = preferences.getBoolean("key_font_three_state", false)

        rootSettings.sbTextSize.progress = preferences.getInt("key_text_size_progress", 2)

        rootSettings.rgMode.setOnCheckedChangeListener(this)
        rootSettings.rgFont.setOnCheckedChangeListener(this)
        rootSettings.sbTextSize.setOnSeekBarChangeListener(this)

        return rootSettings
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (group?.id) {

            R.id.rgMode -> {
                editor.putBoolean("key_white_state", rbWhiteMode.isChecked).apply()
                editor.putBoolean("key_sepia_state", rbSepiaMode.isChecked).apply()
                editor.putBoolean("key_night_mode_state", rbNightMode.isChecked).apply()
            }

            R.id.rgFont -> {
                editor.putBoolean("key_font_one_state", rbFontOne.isChecked).apply()
                editor.putBoolean("key_font_two_state", rbFontTwo.isChecked).apply()
                editor.putBoolean("key_font_three_state", rbFontThree.isChecked).apply()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        editor.putInt("key_text_size_progress", progress).apply()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}