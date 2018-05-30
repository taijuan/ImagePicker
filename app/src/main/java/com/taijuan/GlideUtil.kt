package com.taijuan

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideUtil : AppGlideModule() {
    override fun isManifestParsingEnabled() = true
}