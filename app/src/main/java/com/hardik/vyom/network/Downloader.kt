package com.hardik.vyom.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class Downloader {
    private val scope = CoroutineScope(
        Job() + Dispatchers.IO
    )

    suspend fun execute(url : String): Bitmap? {
        return withContext(scope.coroutineContext) { getData(url) }
    }

    private fun getData(urlParam: String): Bitmap? {
        val url = URL(urlParam)
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        val bitmap = BitmapFactory.decodeStream(conn.inputStream)
        conn.disconnect()
        return bitmap
    }
}