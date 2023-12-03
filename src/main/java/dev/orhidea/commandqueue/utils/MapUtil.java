package dev.orhidea.commandqueue.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class MapUtil {

    @SuppressWarnings({"SortedCollectionWithNonComparableKeys", "unchecked"})
    public <K, V> TreeMap<K, V> treeMap(K key1, V value1, Object... objects) {
        TreeMap<K, V> ret = new TreeMap<>();

        ret.put(key1, value1);

        Iterator<Object> iter = Arrays.asList(objects).iterator();
        while (iter.hasNext())
        {
            K key = (K) iter.next();
            V value = (V) iter.next();
            ret.put(key, value);
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public <K, V> LinkedHashMap<K, V> linkedMap(K key1, V value1, Object... objects) {
        LinkedHashMap<K, V> ret = new LinkedHashMap<>();

        ret.put(key1, value1);

        Iterator<Object> iter = Arrays.asList(objects).iterator();
        while (iter.hasNext())
        {
            K key = (K) iter.next();
            V value = (V) iter.next();
            ret.put(key, value);
        }

        return ret;
    }

}
