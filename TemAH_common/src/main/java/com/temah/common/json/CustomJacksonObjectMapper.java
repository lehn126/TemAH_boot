package com.temah.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CustomJacksonObjectMapper extends ObjectMapper {
    public CustomJacksonObjectMapper() {
        super();
        this.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        this.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // json中存在字段，但JAVA中没有的
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.getSerializerProvider().setNullValueSerializer(new NullSerializer());
    }

    static class NullSerializer extends JsonSerializer<Object> {
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeString("");
        }
    }
}
