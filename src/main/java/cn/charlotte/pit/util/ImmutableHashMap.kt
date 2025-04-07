package cn.charlotte.pit.util

class ImmutableHashMap<K, V>: HashMap<K, V>() {

    override fun put(key: K, value: V): V? {
        throw UnsupportedOperationException("ImmutableHashMap")
    }

    override fun putAll(from: Map<out K, V>) {
        throw UnsupportedOperationException("ImmutableHashMap")
    }

    override fun putIfAbsent(key: K, value: V): V? {
        throw UnsupportedOperationException("ImmutableHashMap")
    }

    fun forcePut(key: K, value: V) {
        super.put(key, value)
    }
}