package com.users.api.mapper;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import java.io.IOException;

@Component
public class JsonPatchConverter extends AbstractHttpMessageConverter<JsonPatch> {

    public JsonPatchConverter() {
        super(MediaType.valueOf("application/json-patch+json"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JsonPatch.class.isAssignableFrom(clazz);
    }

    @Override
    protected JsonPatch readInternal(Class<? extends JsonPatch> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        try (JsonReader reader = Json.createReader(inputMessage.getBody())) {
            return Json.createPatch(reader.readArray());
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getMessage(), inputMessage);
        }
    }

    @Override
    //for serialization
    protected void writeInternal(JsonPatch jsonPatch, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new NotImplementedException("The write Json patch is not implemented");
    }
}
