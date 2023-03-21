package com.hardik.vyom

import android.widget.ImageView
import com.hardik.vyom.cache.ImageCache
import com.hardik.vyom.cache.MemoryCache
import com.hardik.vyom.network.Downloader
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.Collections.synchronizedMap
import java.util.WeakHashMap

class Vyom private constructor() {
    private val fetch = Downloader()
    private val scope = CoroutineScope(
        Job() + Dispatchers.Main
    )
    private lateinit var url: String
    private lateinit var imageViewReference: WeakReference<ImageView>
    private var cache: ImageCache = MemoryCache()
    private var imageAndUrlMap = synchronizedMap(WeakHashMap(HashMap<ImageView,String>()))

    fun url(url: String) = apply {
        this.url = url
    }

    fun target(imageView: ImageView) = apply {
        imageViewReference = WeakReference(imageView)
    }

    fun load() {
        var bitmap = cache.get(url)

        if (bitmap != null) {
            imageViewReference.get()?.setImageBitmap(bitmap)
            return
        }
        imageAndUrlMap[imageViewReference.get()] = url

        scope.launch {
            if (isImageViewAskedToLoadDifferentResource(imageViewReference.get())) {
                return@launch
            }
            bitmap = fetch.execute(url)
            if (bitmap == null) {
                return@launch
            }
            if (isImageViewAskedToLoadDifferentResource(imageViewReference.get())) {
                return@launch
            }
            imageViewReference.get()?.setImageBitmap(bitmap)
            withContext(Dispatchers.IO) {
                cache.put(url, bitmap!!)
            }
        }
    }

    /**
     * We need to know if a new url request is hit for an ongoing imageView request, If so we can skip the old request
     */
    private fun isImageViewAskedToLoadDifferentResource(imageView: ImageView?): Boolean {
        if(imageView == null){
            return false
        }
        val tag = imageAndUrlMap[imageView]
        return tag == null || tag != url
    }

    companion object {
        @Volatile
        private var INSTANCE: Vyom? = null

        @Synchronized
        fun get(): Vyom {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Vyom().also { //double lock checking
                    INSTANCE = it
                }
            }
            return INSTANCE!!
        }
    }
}