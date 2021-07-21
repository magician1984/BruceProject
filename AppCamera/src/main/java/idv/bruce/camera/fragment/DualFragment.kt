package idv.bruce.camera.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import idv.bruce.camera.databinding.FragmentDualBinding

@Deprecated("CameraX not support dual camera")
class DualFragment : PreviewFragment() {
    private lateinit var binding : FragmentDualBinding

    private lateinit var secondCameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private lateinit var secondCameraProvider : ProcessCameraProvider

    private lateinit var secondPreview : Preview

    private var secondCameraSelector : CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentDualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.mainPreview.surfaceProvider)
        }

        secondPreview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.secondPreview.surfaceProvider)
        }

        Toast.makeText(requireContext(), "CameraX lib may not support dual camera", Toast.LENGTH_LONG)
            .show()

        initCamera()
    }

    override fun initCamera() {
        super.initCamera()
        secondCameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        secondCameraProviderFuture.addListener({
            secondCameraProvider = secondCameraProviderFuture.get()

            setupSecondUseCase()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupSecondUseCase() {
        try {
            // Unbind use cases before rebinding
            secondCameraProvider.unbindAll()

            // Bind use cases to camera
            secondCameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                secondCameraSelector,
                secondPreview
            )
        } catch (exc : Exception) {
            Log.e("TAG", "Use case binding failed", exc)
        }
    }
}