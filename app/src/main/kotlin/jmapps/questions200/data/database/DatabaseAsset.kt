package jmapps.questions200.data.database

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

private var databaseVersion = 1

class DatabaseAsset(context: Context?) :
    SQLiteAssetHelper(context, "Questions200DB", null, databaseVersion) {
    init {
        setForcedUpgrade(databaseVersion)
    }
}