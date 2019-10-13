package jmapps.questions200.presentation.mvp.main

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import jmapps.questions200.R
import kotlinx.android.synthetic.main.dialog_footnote.view.*

class MainPresenterImpl(
    private val context: Context?,
    private val sectionNumber: Int?,
    private val mainView: MainContract.MainView?,
    private val database: SQLiteDatabase) : MainContract.MainPresenter {

    private lateinit var strQuestionNumber: String
    private lateinit var strQuestionContent: String
    private lateinit var strAnswerContent: String

    private lateinit var strFootnoteId: String
    private lateinit var strFootnoteContent: String

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun showContentFromDatabase() {
        try {
            val mainCursor: Cursor = database.query(
                "Table_question",
                arrayOf("Question_number", "Question_content", "Answer_content"),
                "_id = ?",
                arrayOf("$sectionNumber"),
                null, null, null
            )

            if (!mainCursor.isClosed && mainCursor.moveToFirst()) {

                strQuestionNumber = mainCursor.getString(0)
                strQuestionContent = mainCursor.getString(1)
                strAnswerContent = mainCursor.getString(2)

                mainView?.showQuestionNumber(strQuestionNumber)
                mainView?.showQuestionContent(strQuestionContent)
                mainView?.showAnswerContent(strAnswerContent)
            }

            if (mainCursor.isClosed) {
                mainCursor.close()
            }

        } catch (e: Exception) {
            mainView?.showDatabaseExceptions(e.toString())
        }
    }

    override fun stringBuilder(str: String): SpannableStringBuilder {

        var str = str
        val ssb = SpannableStringBuilder(Html.fromHtml(str))
        str = ssb.toString()

        var indexOne = str.indexOf("[")
        val indexTwo = intArrayOf(0)

        while (indexOne != -1) {
            indexTwo[0] = str.indexOf("]", indexOne) + 1

            var clickString = str.substring(indexOne, indexTwo[0])
            clickString = clickString.substring(1, clickString.length - 1)
            val finalClickString = clickString

            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {

                    try {
                        @SuppressLint("Recycle")
                        val cursor = database.query(
                            "Table_footnote",
                            arrayOf("_id", "Footnote_content"),
                            "_id = ?",
                            arrayOf(finalClickString), null, null, null
                        )

                        if (cursor != null && cursor.moveToFirst()) {
                            strFootnoteId = cursor.getString(0)
                            strFootnoteContent = cursor.getString(1)
                        }
                        if (cursor != null && !cursor.isClosed) {
                            cursor.close()
                        }
                    } catch (e: Exception) {
                        mainView?.showDatabaseExceptions(e.toString())
                    }

                    dialogFootnote(strFootnoteId, strFootnoteContent)

                }
            }, indexOne, indexTwo[0], 0)
            indexOne = str.indexOf("[", indexTwo[0])
        }
        return ssb
    }

    @SuppressLint("SetTextI18n")
    private fun dialogFootnote(strFootnoteId: String?, strFootnoteContent: String?) {
        @SuppressLint("InflateParams")
        val footnoteView = LayoutInflater.from(context).inflate(R.layout.dialog_footnote, null)
        val footnoteDialog = AlertDialog.Builder(context!!)
        footnoteDialog.setView(footnoteView)

        val footnoteIdNumber = footnoteView.tvFootnoteNumber
        val footnoteContent = footnoteView.tvFootnoteContent

        footnoteIdNumber.text = "Сноска [$strFootnoteId]"
        footnoteContent.text = Html.fromHtml(strFootnoteContent)

        footnoteDialog.create().show()
    }

    override fun addRemoveFavorite(isChecked: Boolean) {
        val favorite = ContentValues()
        favorite.put("Favorite", isChecked)

        mainView?.showFavoriteState(isChecked)
        mainView?.saveFavoriteNumber("key_main_favorite_$sectionNumber", isChecked)

        try {
            database.update(
                "Table_question",
                favorite,
                "_id = ?",
                arrayOf("$sectionNumber")
            )
        } catch (e: Exception) {
            mainView?.showDatabaseExceptions(e.toString())
        }
    }

    override fun copyContent() {
        myClipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        myClip = newPlainText(
            "", Html.fromHtml(
                "$strQuestionNumber<br/>$strQuestionContent<p/>ОТВЕТ<br/>$strAnswerContent"
            )
        )

        myClipboard?.setPrimaryClip(myClip!!)
        mainView?.showCopyToast()
    }
}