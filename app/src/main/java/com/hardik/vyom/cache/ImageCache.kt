package com.hardik.vyom.cache

import android.graphics.Bitmap

interface ImageCache {
    fun get(url : String): Bitmap?
    fun put(url : String, bitmap: Bitmap)
    fun clear()
}

sealed class ImageCacheStrategy {
    object MemoryCache : ImageCacheStrategy()
    object DiskCache : ImageCacheStrategy()
}