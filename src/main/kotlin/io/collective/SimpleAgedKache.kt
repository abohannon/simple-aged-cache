package io.collective

import java.time.Clock

class SimpleAgedKache {
    private val clock: Clock
    private val cache = arrayOfNulls<ExpirableEntry>(100)
    private var size = 0
    constructor(clock: Clock?) {
      this.clock = clock ?: Clock.systemUTC()
    }

    constructor() : this(Clock.systemUTC())

    fun put(key: Any?, value: Any?, retentionInMillis: Int) {
      if (key == null || value == null) {
        throw IllegalArgumentException("key and value must not be null")
      }

      val retentionTime = clock.millis() + retentionInMillis
      val entry = ExpirableEntry(key, value, retentionTime)

      cache[size++] = entry
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }

    fun size(): Int {
        val currentTime = clock.millis()

        var i = 0
        while (i < size) {
          val entry = cache[i]
          if (entry?.isExpired(currentTime) == true) {
            removeEntry(i)
          } else {
            i++
          }
        }

        return size
    }

    fun get(key: Any?): Any? {
      if (key == null) return null

      val currentTime = clock.millis()
      var foundEntry: Any? = null

      for (i in 0 until size) {
        val entry = cache[i]

        if (entry?.key == key && !entry.isExpired(currentTime)) {
          foundEntry = entry.value
        } else if (entry?.isExpired(currentTime) == true) {
            removeEntry(i)
        }
      }

      return foundEntry
    }

    fun removeEntry(index: Int) {
      cache[index] = cache[size - 1]
      cache[size - 1] = null
      size--
    }

    private class ExpirableEntry(val key: Any, val value: Any, val retentionTime: Long) {
      fun isExpired(currentTime: Long): Boolean {
        return currentTime > retentionTime
      }
    }
}
