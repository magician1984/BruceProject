package idv.bruce.camera.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import idv.bruce.camera.databinding.FragmentPictureBinding

class PictureFragment : Fragment() {
    private lateinit var binding : FragmentPictureBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        val bitmap : Bitmap = arguments?.getParcelable("picture") ?: return

        binding.root.setImageBitmap(bitmap)
    }
}