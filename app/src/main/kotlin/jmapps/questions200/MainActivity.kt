package jmapps.questions200

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import jmapps.questions200.data.database.DatabaseAsset
import jmapps.questions200.presentation.mvp.other.OtherContract
import jmapps.questions200.presentation.mvp.other.OtherPresenterImpl
import jmapps.questions200.presentation.ui.about.AboutUs
import jmapps.questions200.presentation.ui.chapters.ChaptersFragment
import jmapps.questions200.presentation.ui.favorites.FavoritesFragment
import jmapps.questions200.presentation.ui.main.SectionsPagerAdapter
import jmapps.questions200.presentation.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OtherContract.OtherView, ChaptersFragment.GetChapterItem,
    FavoritesFragment.GetFavoriteItem {

    private lateinit var database: SQLiteDatabase
    private lateinit var otherPresenter: OtherPresenterImpl

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        otherPresenter = OtherPresenterImpl(this, this)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mainViewPager.adapter = sectionsPagerAdapter

        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < 0) {
                fabChapters.hide()
            } else {
                fabChapters.show()
            }
        })

        fabChapters.setOnClickListener {
            otherPresenter.getListChapters()
        }

        loadPosition()
        openDatabase()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.favoriteList -> {
                otherPresenter.getFavoriteList()
            }

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

    override fun getItemPosition(position: Int) {
        mainViewPager.currentItem = position
    }

    override fun showFavoriteList() {
        FavoritesFragment().show(supportFragmentManager, "favorites")
    }

    override fun showListChapters() {
        ChaptersFragment().show(supportFragmentManager, "chapters")
    }

    override fun showSettings() {
        SettingsFragment().show(supportFragmentManager, "settings")
    }

    override fun showAboutUs() {
        AboutUs().show(supportFragmentManager, "about")
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
        database.close()
    }
}