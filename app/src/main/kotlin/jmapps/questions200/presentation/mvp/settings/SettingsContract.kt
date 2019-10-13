package jmapps.questions200.presentation.mvp.settings

interface SettingsContract {

    interface SettingsView {

        fun colorMode(backgroundColor: Int, textColor: Int)

        fun typeFace(nameTypeFace: String)

        fun textSize(textSize: Float)

    }

    interface SettingsPresenter {

        fun backgroundMode(numberMode: Int)

        fun typeFaceMode(numberMode: Int)

        fun textSizeMode(numberMode: Int)
    }
}