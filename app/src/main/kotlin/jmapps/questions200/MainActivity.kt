package jmapps.questions200

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import jmapps.questions200.data.database.DatabaseAsset
import jmapps.questions200.data.database.DatabaseLists
import jmapps.questions200.presentation.mvp.other.OtherContract
import jmapps.questions200.presentation.mvp.other.OtherPresenterImpl
import jmapps.questions200.presentation.ui.about.AboutUsBottomSheet
import jmapps.questions200.presentation.ui.chapters.ChaptersBottomSheet
import jmapps.questions200.presentation.ui.chapters.ModelChapters
import jmapps.questions200.presentation.ui.favorites.FavoritesBottomSheet
import jmapps.questions200.presentation.ui.main.SectionsPagerAdapter
import jmapps.questions200.presentation.ui.main.StringBuilders
import jmapps.questions200.presentation.ui.settings.SettingsBottomSheet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OtherContract.OtherView, ChaptersBottomSheet.GetChapterItem,
    FavoritesBottomSheet.GetFavoriteItem, View.OnClickListener, ViewPager.OnPageChangeListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var database: SQLiteDatabase? = null
    private lateinit var otherPresenter: OtherPresenterImpl

    private lateinit var stringBuilders: StringBuilders
    private lateinit var chapterList: MutableList<ModelChapters>

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        openDatabase()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        otherPresenter = OtherPresenterImpl(this, this)
        stringBuilders = StringBuilders(this, database)

        chapterList = DatabaseLists(this).getChapterList

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = sectionsPagerAdapter

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < 0) {
                fabChapters.hide()
                fabFavorites.hide()
            } else {
                fabChapters.show()
                fabFavorites.show()
            }
        })

        fabChapters.setOnClickListener(this)
        fabFavorites.setOnClickListener(this)

        mainViewPager.addOnPageChangeListener(this)

        setTextContent(preferences.getInt("key_pager_position", 0))
        loadPosition()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.settings -> {
                otherPresenter.getSettings()
            }

            R.id.aboutUs -> {
                otherPresenter.getAboutUs()
            }

            R.id.rateApp -> {
                otherPresenter.rateApp()
            }

            R.id.shareLink -> {
                otherPresenter.shareLink()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabChapters -> otherPresenter.getListChapters()

            R.id.fabFavorites -> otherPresenter.getFavoriteList()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        setTextContent(position)
    }

    override fun getItemPosition(position: Int) {
        mainViewPager.currentItem = position
    }

    override fun showFavoriteList() {
        val favoritesBottomSheet = FavoritesBottomSheet()
        favoritesBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        favoritesBottomSheet.show(supportFragmentManager, "favorites")
    }

    override fun showListChapters() {
        val chaptersBottomSheet = ChaptersBottomSheet()
        chaptersBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        chaptersBottomSheet.show(supportFragmentManager, "chapters")
    }

    override fun showSettings() {
        val settingsBottomSheet = SettingsBottomSheet()
        settingsBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        settingsBottomSheet.show(supportFragmentManager, "settings")
    }

    override fun showAboutUs() {
        val aboutUsBottomSheet = AboutUsBottomSheet()
        aboutUsBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
        aboutUsBottomSheet.show(supportFragmentManager, "about")
    }

    override fun onStop() {
        super.onStop()
        savePosition()
        closeDatabase()
    }

    override fun onDestroy() {
        super.onDestroy()
        savePosition()
        closeDatabase()
    }

    private fun savePosition() {
        editor.putInt("key_pager_position", mainViewPager.currentItem).apply()
    }

    private fun loadPosition() {
        mainViewPager.currentItem = preferences.getInt("key_pager_position", 0)
    }

    private fun openDatabase() {
        database = DatabaseAsset(this).readableDatabase
    }

    private fun closeDatabase() {
        database?.close()
    }

    private fun setTextContent(position: Int) {
        tvQuestionNumber.text = chapterList[position].strChapterNumber
        tvQuestionContent.movementMethod = LinkMovementMethod.getInstance()
        tvQuestionContent.setText(stringBuilders.stringBuilder(
            chapterList[position].strChapterTitle!!), TextView.BufferType.SPANNABLE )
    }
}