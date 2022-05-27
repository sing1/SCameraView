package sing.scameraview.demo

import android.content.Context
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

object BitMapUtil {
    fun saveImg(context: Context, bitmap: Bitmap, pathName: String?): Boolean {
        try {
            val mFile = File(pathName) //将要保存的图片文件
            val outputStream = FileOutputStream(mFile) //构建输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) //compress到输出outputStream
            val uri = Uri.fromFile(mFile) //获得图片的uri
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)) //发送广播通知更新图库，这样系统图库可以找到这张图片
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return false
    }
}