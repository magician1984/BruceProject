package idv.bruce.radio.data_flow

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Pipeline<T> : CoroutineScope {
    private var node : Module<T>? = null

    override val coroutineContext : CoroutineContext
        get() = Dispatchers.IO

    fun addModule(module : Module<T>) : Pipeline<T> {
        if (node == null)
            node = module
        else
            node!!.add(module)
        return this
    }

    fun removeModule(module : Module<T>) : Pipeline<T> {
        node?.remove(module)
        return this
    }

    fun execute(data : T) {
        launch {
            node?.onTransform(data)
        }
    }

    abstract class Module<T>(var tag : String) {
        private var input : Module<T>? = null

        private var output : Module<T>? = null

        private var count : Int = 0

        private var isDropdown : Boolean = false

        val linkCount : Int
            get() = count + 1

        abstract fun release()

        protected abstract fun process(data : T?) : Boolean

        fun dropdown() {
            isDropdown = true
        }

        fun onTransform(data : T?) : Boolean {
            val res : Boolean = process(data)
            if (isDropdown) {
                isDropdown = false
                return res
            }
            return output?.onTransform(data) ?: res
        }

        fun add(module : Module<T>) : Boolean {
            return if (module == this)
                false
            else if (output == null) {
                output = module
                count++
                true
            } else {
                val r : Boolean = output!!.add(module)
                if (r) count++
                r
            }
        }

        fun remove(module : Module<T>) : Boolean {
            return if (module == this) {
                input?.output = output
                output?.input = input
                count = -1
                true
            } else if (output == null) {
                false
            } else {
                val r : Boolean = output!!.remove(module)
                if (r) count--
                r
            }
        }

        fun indexOf(module : Module<T>) : Int {
            return mIndexOf(module, 0)
        }

        private fun mIndexOf(module : Module<T>, index : Int) : Int {
            return if (module == this)
                index
            else if (output == null) {
                -1
            } else {
                output!!.mIndexOf(module, index + 1)
            }
        }

        override fun equals(other : Any?) : Boolean {
            if (this === other) return true
            if (other !is Module<*>) return false

            if (tag != other.tag) return false

            return true
        }

        override fun hashCode() : Int {
            return tag.hashCode()
        }
    }
}