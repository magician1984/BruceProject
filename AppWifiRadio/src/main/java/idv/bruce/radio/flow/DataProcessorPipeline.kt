package idv.bruce.radio.flow

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DataProcessorPipeline : CoroutineScope {
    private var node : DataProcessor? = null

    override val coroutineContext : CoroutineContext
        get() = Dispatchers.IO

    fun addProcessor(processor : DataProcessor) : DataProcessorPipeline {
        if (node == null)
            node = processor
        else
            node!!.add(processor)
        return this
    }

    fun removeProcessor(processor : DataProcessor) : DataProcessorPipeline {
        node?.remove(processor)
        return this
    }

    fun execute(data : ByteArray) {
        launch {
            node?.onTransform(data)
        }
    }
}