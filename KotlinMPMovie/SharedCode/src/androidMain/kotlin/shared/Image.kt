package shared

actual typealias Image = ByteArray

actual fun ByteArray.toNativeImage(): Image? = this