package com.tigerspike.landmark.util

/**
 * Class uses for single event LiveData
 **/

class Event<out T>(private val _content: T) {

    var handled = false
        private set

    val content
        get() = if (handled) {
            null
        } else {
            handled = true
            _content
        }
    
    fun peek() = _content

    class Observer<T>(private val observer: (T) -> Unit) :
        androidx.lifecycle.Observer<Event<T>> {
        override fun onChanged(event: Event<T>?) {
            event?.content?.let(observer)
        }
    }
}

fun event() = Event(Unit)