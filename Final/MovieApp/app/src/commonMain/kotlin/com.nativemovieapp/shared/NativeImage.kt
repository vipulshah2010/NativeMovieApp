package com.nativemovieapp.shared

expect class Image

expect fun ByteArray.toNativeImage(): Image?
