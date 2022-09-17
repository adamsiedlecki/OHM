package pl.adamsiedlecki.ohm.config.spring;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final FileHttpMessageConverter fileHttpMessageConverter;

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        var messageConvertersExceptDefaultJackson = restTemplate.getMessageConverters()
                .stream()
                .filter(c-> !(c instanceof MappingJackson2HttpMessageConverter))
                .collect(Collectors.toList());

        messageConvertersExceptDefaultJackson.add(createMappingJacksonHttpMessageConverter());
        messageConvertersExceptDefaultJackson.add(byteArrayHttpMessageConverter());
        messageConvertersExceptDefaultJackson.add(fileHttpMessageConverter);

        restTemplate.setMessageConverters(messageConvertersExceptDefaultJackson);
        return restTemplate;
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                           .registerModule(new JavaTimeModule())
                           .findAndRegisterModules();
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());
        return arrayHttpMessageConverter;
    }

    private List<MediaType> getSupportedMediaTypes() {
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        return list;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(getObjectMapper());
        return converter;
    }
}
