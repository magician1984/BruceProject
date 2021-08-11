package idv.bruce.code1922.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import idv.bruce.code1922.R
import idv.bruce.code1922.databinding.ActivityMainBinding
import idv.bruce.code1922.model.Repositories
import idv.bruce.code1922.structure.SmsData
import idv.bruce.code1922.viewmodel.DataViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val dataViewModel:DataViewModel by viewModels()


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        dataViewModel.repositories = Repositories(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}