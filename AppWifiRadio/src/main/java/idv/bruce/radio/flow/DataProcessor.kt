package idv.bruce.radio.flow

abstract class DataProcessor(var tag : String) {
    private var input : DataProcessor? = null

    private var output : DataProcessor? = null

    private var count : Int = 0

    private var isDropdown : Boolean = false

    val linkCount : Int
        get() = count + 1

    protected abstract fun preTransform(data : ByteArray?) : ByteArray?

    abstract fun release()

    fun dropdown() {
        isDropdown = true
    }

    fun onTransform(data : ByteArray?) {
        preTransform(data)
        if (isDropdown) {
            isDropdown = false
            return
        }
        output?.onTransform(data)
    }

    fun add(processor : DataProcessor) : Boolean {
        return if (processor == this)
            false
        else if (output == null) {
            output = processor
            count++
            true
        } else {
            val r : Boolean = output!!.add(processor)
            if (r) count++
            r
        }
    }

    fun remove(processor : DataProcessor) : Boolean {
        return if (processor == this) {
            input?.output = output
            output?.input = input
            count = -1
            true
        } else if (output == null) {
            false
        } else {
            val r : Boolean = output!!.remove(processor)
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
        else if (output == null) {
            -1
        } else {
            output!!.mIndexOf(processor, index + 1)
        }
    }

    override fun equals(other : Any?) : Boolean {
        if (this === other) return true
        if (other !is DataProcessor) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode() : Int {
        return tag.hashCode()
    }
}