package jmapps.questions200.presentation.mvp.other

interface OtherContract {

    interface OtherView {

        fun showFavoriteList()

        fun showListChapters()

        fun showSettings()

        fun showAboutUs()
    }

    interface OtherPresenter {

        fun getFavoriteList()

        fun getListChapters()

        fun getSettings()

        fun getAboutUs()

        fun shareLink()

        fun rateApp()
    }
}