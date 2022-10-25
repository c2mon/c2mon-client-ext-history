package cern.c2mon.client.ext.history.es_publisher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class MapToStringConverter implements AttributeConverter<Map<String, String>, String> {

  ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, String> data) {
    String value = "";
    try {
      value = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return value;
  }

  @Override
  public Map<String, String> convertToEntityAttribute(String data) {

    Map<String, String> mapValue = new HashMap<String, String>();
    TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
    };
    try {
      mapValue = mapper.readValue(data, typeRef);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mapValue;
  }
}
