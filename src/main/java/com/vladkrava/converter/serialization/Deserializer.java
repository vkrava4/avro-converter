package com.vladkrava.converter.serialization;

/**
 * Provides low level interface for deserialization from the byte array
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public interface Deserializer<T> {

    /**
     * Object deserialization
     *
     * @param data   - source of the data
     * @param aClass - target class
     * @return T generic object
     * @throws DataSerializationException - signals that some exception of some sort has occurred
     *                                    during the data deserialization
     */
    T deserialize(byte[] data, Class<? extends T> aClass) throws DataSerializationException;

}
