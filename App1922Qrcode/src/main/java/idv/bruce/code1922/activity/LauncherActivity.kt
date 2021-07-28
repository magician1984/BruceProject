package idv.bruce.code1922.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LauncherActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 0x33

        private val PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(
        requestCode : Int, permissions : Array<String>, grantResults :
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkPermissions()) {
                onReady()
            } else {
                onPermissionNotAllow()
            }
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPermissions()) {
            onReady()
        } else
            requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    private fun onReady(){
        val intent:Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onPermissionNotAllow(){

    }

    private fun checkPermissions() : Boolean {
        for (permission in PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }
}