package com.vladkrava.converter.http;

import static org.springframework.http.converter.StringHttpMessageConverter.DEFAULT_CHARSET;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.http.MediaType;

/**
 * This converter supports application/avro-json format with {@code DEFAULT_CHARSET} character set.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see AbstractAvroHttpMessageConverter
 * @see SpecificRecordBase
 * @since 0.1-SNAPSHOT
 */
public class AvroToJsonHttpMessageConverter<T extends SpecificRecordBase> extends AbstractAvroHttpMessageConverter<T> {
    private static final String MEDIA_TYPE = "application";
    private static final String MEDIA_SUB_TYPE = "avro-json";

    private static final MediaType AVRO_JSON_MEDIA_TYPE = new MediaType(MEDIA_TYPE, MEDIA_SUB_TYPE, DEFAULT_CHARSET);

    public AvroToJsonHttpMessageConverter() {
        super(AVRO_JSON_MEDIA_TYPE);
    }
}
