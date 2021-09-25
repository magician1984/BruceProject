package idv.bruce.player.renderer

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.google.android.exoplayer2.util.GlUtil
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class StanderRenderer : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl : GL10?, config : EGLConfig?) {

    }

    override fun onSurfaceChanged(gl : GL10?, width : Int, height : Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl : GL10?) {

    }
}