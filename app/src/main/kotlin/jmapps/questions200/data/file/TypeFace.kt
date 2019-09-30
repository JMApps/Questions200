package jmapps.questions200.data.file

import android.content.Context
import android.graphics.Typeface
import java.util.*

class TypeFace {

    private val cache = Hashtable<String, Typeface>()

    operator fun get(c: Context, name: String): Typeface? {

        synchronized(cache) {
            if (!cache.containsKey(name)) {
                try {
                    val t = Typeface.createFromAsset(c.assets, name)
                    cache[name] = t
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return cache[name]
        }
    }
}