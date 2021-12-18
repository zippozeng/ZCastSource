package com.github.zippozeng.zcast.source

import android.Manifest
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.github.zippozeng.zcast.source.databinding.ActivityMainBinding
import com.google.android.gms.cast.Cast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        binding.startCast.setOnClickListener {
            val result = PermissionChecker.checkSelfPermission(
                applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (result == PermissionChecker.PERMISSION_GRANTED) {
                startRecord()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MainActivity.CODE_FOR_WRITE_PERMISSION
                )
            }
        }
        binding.stopCast.setOnClickListener { }
    }

    private fun startRecord() {
        val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val intent = manager.createScreenCaptureIntent()
        startActivityForResult(intent, REQUEST_MIRROR_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MIRROR_PERMISSION) {
            }
        }
    }

    /**
     * A native method that is implemented by the 'source' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        private val CODE_FOR_WRITE_PERMISSION = 0
        private val REQUEST_MIRROR_PERMISSION = 0

        // Used to load the 'source' library on application startup.
        init {
            System.loadLibrary("source")
        }
    }

}