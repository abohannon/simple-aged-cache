package io.collective;

import java.time.Clock;

public class SimpleAgedCache {
    private Clock clock;
    private ExpirableEntry[] cache;
    private int size;

    public SimpleAgedCache(Clock clock) {
      this.clock = clock;
      this.cache = new ExpirableEntry[100];
      this.size = 0;
    }

    public SimpleAgedCache() {
      this(Clock.systemUTC());
    }

    public void put(Object key, Object value, int retentionInMillis) {
      long retentionTime = clock.millis() + retentionInMillis;

      ExpirableEntry entry = new ExpirableEntry(key, value, retentionTime);

      cache[size++] = entry;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
      long currentTime = clock.millis();

      for (int i = 0; i < size; i++) {
        ExpirableEntry entry = cache[i];

        if (entry.isExpired(currentTime)) {
          cache[i] = cache[size - 1];
          cache[size - 1] = null;
          size--;
        }
      }

      return size;
    }

    public Object get(Object key) {
        long currentTime = clock.millis();

        for (int i = 0; i < size; i++) {
          ExpirableEntry entry = cache[i];

          if (entry.key.equals(key) && entry.retentionTime > currentTime) {
            return entry.value;
          }
        }

        return null;
    }

    private class ExpirableEntry {
        private Object key;
        private Object value;
        private long retentionTime;

        public ExpirableEntry(Object key, Object value, long retentionTime) {
            this.key = key;
            this.value = value;
            this.retentionTime = retentionTime;
        }

        boolean isExpired(long currentTime) {
          return currentTime > retentionTime;
        }
    }
}
