package idv.bruce.appuitester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import idv.bruce.appuitester.databinding.ActivityMainBinding
import idv.bruce.appuitester.input.Keyboard
import idv.bruce.appuitester.input.KeyboardView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.keyboard.apply {
            keyboard = Keyboard(this@MainActivity, R.xml.keyboard_spell)
        }
    }
}