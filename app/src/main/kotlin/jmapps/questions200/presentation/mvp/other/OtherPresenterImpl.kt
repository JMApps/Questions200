package jmapps.questions200.presentation.mvp.other

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class OtherPresenterImpl(
    private val context: Context,
    private val otherView: OtherContract.OtherView?) :
    OtherContract.OtherPresenter {

    private val linkDescription = "200 вопросов по вероучению Ислама\n"
    private val linkApp = "https://play.google.com/store/apps/details?id=jmapps.questions200"

    override fun getFavoriteList() {
        otherView?.showFavoriteList()
    }

    override fun getSettings() {
        otherView?.showSettings()
    }

    override fun getAboutUs() {
        otherView?.showAboutUs()
    }

    override fun getListChapters() {
        otherView?.showListChapters()
    }

    override fun shareLink() {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, "$linkDescription$linkApp")
        context.startActivity(shareLink)
    }

    override fun rateApp() {
        val rate = Intent(Intent.ACTION_VIEW)
        rate.data = linkApp.toUri()
        context.startActivity(rate)
    }
}