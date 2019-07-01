package com.nativemovieapp.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory

actual typealias Image = Bitmap

actual fun ByteArray.toNativeImage(): Image? =
    BitmapFactory.decodeByteArray(this, 0, this.size)