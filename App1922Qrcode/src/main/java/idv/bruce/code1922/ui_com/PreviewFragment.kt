package idv.bruce.code1922.ui_com

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.TimeUnit

abstract class PreviewFragment : Fragment() {
    companion object {
        private const val TAG = "PreviewFragment"
    }

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    protected lateinit var cameraProvider : ProcessCameraProvider

    protected lateinit var preview : Preview

    protected var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    protected var isInit : Boolean = false

    @SuppressLint("RestrictedApi")
    protected fun flipCamera() {
        cameraSelector = if (cameraSelector.lensFacing!! == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else CameraSelector.DEFAULT_BACK_CAMERA

        setupUseCase()
    }

    protected open fun setupUseCase() {
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                .createPoint(.5f, .5f)

            val autoFocusAction = FocusMeteringAction.Builder(
                autoFocusPoint,
                FocusMeteringAction.FLAG_AF
            ).apply {
                //start auto-focusing after 2 seconds
                setAutoCancelDuration(2, TimeUnit.SECONDS)
            }.build()

            // Bind use cases to camera
            val camera = cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview)

            camera.cameraControl.startFocusAndMetering(autoFocusAction)
        } catch (exc : Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    protected open fun initCamera() {
        isInit = false

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        val c = ProcessCameraProvider.getInstance(requireContext())

        Log.d("Trace", "${c == cameraProviderFuture}")

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            setupUseCase()

            isInit = true
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    protected fun startPreview() {
        if (!cameraProvider.isBound(preview))
            cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview)
    }

    protected fun stopPreview() {
        if (cameraProvider.isBound(preview))
            cameraProvider.unbind(preview)
    }
}