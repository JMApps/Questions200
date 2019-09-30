package jmapps.questions200.presentation.mvp.settings

interface SettingsContract {

    interface SettingsView {

        fun whiteMode(backgroundColor: Int, textColor: Int)

        fun sepiaMode(backgroundColor: Int, textColor: Int)

        fun nightMode(backgroundColor: Int, textColor: Int)

    }

    interface SettingsPresenter {

        fun backgroundMode(numberMode: Int)
    }
}