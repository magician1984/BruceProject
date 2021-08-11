package idv.bruce.code1922.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import idv.bruce.code1922.database.DatabaseHelper
import idv.bruce.code1922.structure.SavedData
import idv.bruce.code1922.structure.SmsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class Repositories private constructor(context : Context) {
    companion object {
        @Volatile
        private var instance : Repositories? = null

        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK) {
            instance ?: Repositories(context).also { instance = it }
        }
    }

    private val contextRef : WeakReference<Context> = WeakReference(context)

    private val databaseHelper : DatabaseHelper = DatabaseHelper(context)

    val smsDataList : MutableLiveData<List<SmsData>> = MutableLiveData()

    val savedDataList : MutableLiveData<List<SavedData>> = MutableLiveData()

    init {
        smsDataList.postValue(databaseHelper.smsDataDao.getAll())

        savedDataList.postValue(databaseHelper.savedDataDao.getAll())
    }

    suspend fun addSmsHistory(data : SmsData) = withContext(Dispatchers.IO) {
        databaseHelper.smsDataDao.insert(data)
        smsDataList.postValue(databaseHelper.smsDataDao.getAll())
    }

    suspend fun addSavedData(data : SavedData) = withContext(Dispatchers.IO) {
        databaseHelper.savedDataDao.insert(data)
        savedDataList.postValue(databaseHelper.savedDataDao.getAll())
    }

    suspend fun sendIntent(data : SmsData) = withContext(Dispatchers.IO) {
        val context : Context = contextRef.get() ?: return@withContext

        val intent : Intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${1922}")).apply {
            putExtra("sms_body", data.content)
        }

        context.startActivity(intent)
    }

    suspend fun sendIntent(data : SavedData) = withContext(Dispatchers.IO) {
        databaseHelper.smsDataDao.find(data.code).apply {
            if (size > 0) {
                val smsData : SmsData = get(0)
                sendIntent(smsData)
            }
        }
    }
}