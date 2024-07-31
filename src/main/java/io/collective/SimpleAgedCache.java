package io.collective;

import java.time.Clock;
import java.util.Arrays;

public class SimpleAgedCache {
    private Clock clock;
    private ExpirableEntry[] cache;
    private int size;

    public SimpleAgedCache(Clock clock) {
      this.clock = clock;
      this.cache = new ExpirableEntry[100];
      System.out.println(Arrays.toString(this.cache));
    }

    public SimpleAgedCache() {

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
        return 0;
    }

    public Object get(Object key) {
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
    }
}
