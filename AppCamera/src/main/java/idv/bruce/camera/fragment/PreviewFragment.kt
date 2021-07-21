package idv.bruce.camera.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.common.util.concurrent.ListenableFuture
import idv.bruce.camera.databinding.FragmentPreviewBinding

open class PreviewFragment : Fragment() {
    companion object {
        private const val TAG = "PreviewFragment"
    }

    private lateinit var binding : FragmentPreviewBinding

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    protected lateinit var cameraProvider : ProcessCameraProvider

    protected lateinit var preview : Preview

    protected var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }

        binding.flip.setOnClickListener {
            flipCamera()
        }

        initCamera()
    }

    @SuppressLint("RestrictedApi")
    protected fun flipCamera() {
        cameraSelector = if (cameraSelector.lensFacing!! == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else CameraSelector.DEFAULT_BACK_CAMERA

        setupUseCase()
    }

    open fun setupUseCase() {
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview)
        } catch (exc : Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    protected fun initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            setupUseCase()

        }, ContextCompat.getMainExecutor(requireContext()))
    }
}