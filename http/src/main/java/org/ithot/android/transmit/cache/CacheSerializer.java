package org.ithot.android.transmit.cache;

/**
 * This cache interface describe the way an object should be serialized/deserialized into a
 * byte array.
 * @param <T> is the class of object to serialized/deserialized.
 */
public interface CacheSerializer<T> {
    /**
     * Deserialization of a String into an object.
     * @param data is the byte array representing the serialized data.
     * @return the deserialized data.
     */
    T fromString(String data);

    /**
     * Serialization of an object into String.
     * @param object is the object to serialize.
     * @return the result of the serialization into a byte array.
     */
    String toString(T object);
}
