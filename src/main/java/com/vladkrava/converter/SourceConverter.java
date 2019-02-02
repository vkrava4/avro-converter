package com.vladkrava.converter;

import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Converting data between 2 types where T is target type and E is a source
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see DataSerializationException
 * @since 0.1-SNAPSHOT
 */
public interface SourceConverter<T, E> {

    /**
     * Data converting specification
     *
     * @param e - source data
     * @return T - converted data in a target datatype
     * @throws DataSerializationException - on any exception with serialization from source datatype to a byte array
     */
    T convert(E e) throws DataSerializationException;
}
