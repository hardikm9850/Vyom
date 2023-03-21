package com.hardik.vyom.cache

import android.graphics.Bitmap

class DiskCache : ImageCache {
    private val cacheSize: Int = 0
    //TODO implement
    override fun get(url: String): Bitmap? {
        return null
    }

    override fun put(url: String, bitmap: Bitmap) {

    }

    override fun clear() {

    }
}