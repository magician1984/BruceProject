package idv.bruce.code1922.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.Barcode
import idv.bruce.code1922.model.Repositories
import idv.bruce.code1922.structure.SavedData
import idv.bruce.code1922.structure.SmsData
import kotlinx.coroutines.launch

class DataViewModel : ViewModel() {

    var repositories : Repositories? = null
        set(value) {
            field = value
            if (field != null) {
                smsDataList = field!!.smsDataList
                savedDataList = field!!.savedDataList
            }
        }

    var smsDataList : MutableLiveData<List<SmsData>>? = null

    var savedDataList : MutableLiveData<List<SavedData>>? = null

    val toastMsg : MutableLiveData<Int> = MutableLiveData()

    fun onCodeScanned(message : String) {
        viewModelScope.launch {
            val smsData : SmsData = SmsData().apply {
                time = System.currentTimeMillis()
                content = message
            }

            repositories?.addSmsHistory(smsData)

            repositories?.sendIntent(smsData)
        }
    }

    fun onDataSave(smsData : SmsData, name : String) {
        viewModelScope.launch {
            val savedData : SavedData = SavedData().apply {
                updateTime = System.currentTimeMillis()
                tag = name
                code = smsData.code
            }
            repositories?.addSavedData(savedData)
        }
    }
}