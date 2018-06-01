package com.taijuan

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideUtil : AppGlideModule() {
    override fun isManifestParsingEnabled() = true
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context).build()
        builder.setBitmapPool(LruBitmapPool(calculator.bitmapPoolSize.toLong()))
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, 500 * 1024 * 1024))
        builder.setDefaultRequestOptions(RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .encodeQuality(75)
                .disallowHardwareConfig())
        builder.setLogLevel(Log.DEBUG)
    }
}