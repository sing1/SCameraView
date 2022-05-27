package sing.scameraview.demo

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions3.RxPermissions
import sing.camera.library.SCameraView
import sing.camera.library.listener.ErrorListener
import sing.camera.library.listener.SCameraListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private var TAG = MainActivity::class.java.simpleName

    private lateinit var sCameraView: SCameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        setContentView(R.layout.activity_main)

        RxPermissions(this@MainActivity)
            .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            .subscribe { granted: Boolean ->
                if (granted) {
                    initData()
                } else { // 没有权限
                    Toast.makeText(this@MainActivity,"请给予相应的权限",Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun initData(){
        sCameraView = findViewById(R.id.camera_view)
        val saveDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        sCameraView.setSaveVideoPath("$saveDirectory")
        //JCameraView监听
        sCameraView.setErrorListener(object : ErrorListener {
            override fun onError() {
                Log.e(TAG,"open camera error")
            }

            override fun AudioPermissionError() {
                Log.e(TAG,"AudioPermissionError")
            }
        })

        sCameraView.setSCameraListener(object : SCameraListener {
            override fun captureSuccess(bitmap: Bitmap) {
                //获取到拍照成功后返回的Bitmap
                val path = "${saveDirectory}/image_${Date().time}.png"
                BitMapUtil.saveImg(this@MainActivity,bitmap, path)
                Log.e(TAG,"图片保存在 $path")
            }

            override fun recordSuccess(url: String, firstFrame: Bitmap) {
                Log.e(TAG,"视频保存在 $url")
            }
        })

        sCameraView.setLeftClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        sCameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        sCameraView.onPause()
    }

    private fun fullscreen(){
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }
}