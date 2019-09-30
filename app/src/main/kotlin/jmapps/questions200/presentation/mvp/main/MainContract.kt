package jmapps.questions200.presentation.mvp.main

import android.text.SpannableStringBuilder

interface MainContract {

    interface MainView {

        fun showDatabaseExceptions(e: String)

        fun showQuestionNumber(number: String)

        fun showQuestionContent(content: String)

        fun showAnswerContent(content: String)

        fun showFavoriteState(state: Boolean)

        fun saveFavoriteNumber(keyFavorite: String, state: Boolean)

        fun showCopyToast()
    }

    interface MainPresenter {

        fun showContentFromDatabase()

        fun stringBuilder(str: String): SpannableStringBuilder

        fun addRemoveFavorite(isChecked: Boolean)

        fun copyContent()
    }
}