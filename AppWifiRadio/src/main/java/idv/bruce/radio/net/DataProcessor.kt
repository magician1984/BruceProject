package idv.bruce.radio.net

abstract class DataProcessor(var tag : String) {
    private var preProcessor : DataProcessor? = null

    private var nextProcessor : DataProcessor? = null

    private var count : Int = 0

    val linkCount : Int
        get() = count + 1

    protected abstract fun process(data : ByteArray?) : ByteArray?

    abstract fun release()

    fun onPassing(data : ByteArray?) {
        process(data)
        nextProcessor?.onPassing(data)
    }

    fun add(processor : DataProcessor) : Boolean {
        return if (processor == this)
            false
        else if (nextProcessor == null) {
            nextProcessor = processor
            count++
            true
        } else {
            val r : Boolean = nextProcessor!!.add(processor)
            if (r) count++
            r
        }
    }

    fun remove(processor : DataProcessor) : Boolean {
        return if (processor == this) {
            preProcessor?.nextProcessor = nextProcessor
            nextProcessor?.preProcessor = preProcessor
            count = -1
            true
        } else if (nextProcessor == null) {
            false
        } else {
            val r : Boolean = nextProcessor!!.remove(processor)
            if (r) count--
            r
        }
    }

    fun indexOf(processor : DataProcessor) : Int {
        return mIndexOf(processor, 0)
    }

    private fun mIndexOf(processor : DataProcessor, index : Int) : Int {
        return if (processor == this)
            index
        else if (nextProcessor == null) {
            -1
        } else {
            nextProcessor!!.mIndexOf(processor, index + 1)
        }
    }
}