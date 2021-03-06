package jmapps.questions200.presentation.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jmapps.questions200.R
import kotlinx.android.synthetic.main.dialog_footnote.view.*

class StringBuilders(val context: Context?, val database: SQLiteDatabase?) {

    private lateinit var strFootnoteId: String
    private lateinit var strFootnoteContent: String

    fun stringBuilder(str: String): SpannableStringBuilder {

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
                        val cursor = database?.query(
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
                        Toast.makeText(context, "Ошибка = $e", Toast.LENGTH_LONG).show()
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
}