package idv.bruce.code1922.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import idv.bruce.code1922.structure.SavedData
import idv.bruce.code1922.structure.SmsData

class DataViewModel:ViewModel() {
    val smsDataList:MutableLiveData<List<SmsData>> = MutableLiveData()

    val savedDataList:MutableLiveData<List<SavedData>> = MutableLiveData()
}