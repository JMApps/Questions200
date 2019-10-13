package jmapps.questions200.presentation.mvp.settings

import android.graphics.Color

class SettingsPresenterImpl(private val settingsView: SettingsContract.SettingsView?) :
    SettingsContract.SettingsPresenter {

    override fun backgroundMode(numberMode: Int) {
        when (numberMode) {

            1 -> {
                settingsView?.colorMode(
                    Color.argb(255, 244, 244, 244),
                    Color.argb(255, 87, 87, 87)
                )
            }

            2 -> {
                settingsView?.colorMode(
                    Color.argb(255, 242, 238, 167),
                    Color.argb(255, 112, 112, 112)
                )
            }

            3 -> {
                settingsView?.colorMode(
                    Color.argb(255, 44, 44, 44),
                    Color.argb(255, 184, 184, 184)
                )
            }
        }
    }

    override fun typeFaceMode(numberMode: Int) {
        when (numberMode) {

            1 -> settingsView?.typeFace("")

            2 -> settingsView?.typeFace("")

            3 -> settingsView?.typeFace("")
        }
    }

    override fun textSizeMode(numberMode: Int) {
        when (numberMode) {

            0 -> settingsView?.textSize(14f)

            1 -> settingsView?.textSize(16f)

            2 -> settingsView?.textSize(18f)

            3 -> settingsView?.textSize(20f)

            4 -> settingsView?.textSize(24f)

            5 -> settingsView?.textSize(30f)
        }
    }
}