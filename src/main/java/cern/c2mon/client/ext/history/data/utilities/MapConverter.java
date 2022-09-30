package cern.c2mon.client.ext.history.data.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, Object> data) {
    String value = "";
    try {
      value = mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return value;
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String data) {

    Map<String, Object> mapValue = new HashMap<String, Object>();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
    };
    try {
      mapValue = mapper.readValue(data, typeRef);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return mapValue;
  }
}
