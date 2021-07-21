package idv.bruce.camera

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import idv.bruce.camera.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 0x33

        private val PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    private lateinit var binding : ActivityHostBinding


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHostBinding.inflate(layoutInflater)
        if (checkPermissions()) {
            setContentView(binding.root)
        } else
            requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode : Int, permissions : Array<String>, grantResults :
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissions()) {
                setContentView(binding.root)
            } else {
                finish()
            }
        }
    }

    private fun checkPermissions() : Boolean {
        for (permission in PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }
}