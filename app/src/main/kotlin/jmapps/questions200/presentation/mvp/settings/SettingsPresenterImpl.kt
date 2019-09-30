package jmapps.questions200.presentation.mvp.settings

import android.graphics.Color

class SettingsPresenterImpl(private val settingsView: SettingsContract.SettingsView?) :
    SettingsContract.SettingsPresenter {

    override fun backgroundMode(numberMode: Int) {
        when (numberMode) {
            1 -> {
                settingsView?.whiteMode(
                    Color.argb(255, 244, 244, 244),
                    Color.argb(255, 87, 87, 87)
                )
            }
            2 -> {
                settingsView?.sepiaMode(
                    Color.argb(255, 242, 238, 167),
                    Color.argb(255, 112, 112, 112)
                )
            }
            3 -> {
                settingsView?.nightMode(
                    Color.argb(255, 44, 44, 44),
                    Color.argb(255, 184, 184, 184)
                )
            }
        }
    }
}