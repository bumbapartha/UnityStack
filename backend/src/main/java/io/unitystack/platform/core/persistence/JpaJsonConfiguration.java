package io.unitystack.platform.core.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Makes Hibernate read and write {@code jsonb} columns with the application's ObjectMapper, so stored JSON
 * matches the API's JSON and unknown fields in older rows are ignored rather than failing the read (TD-12).
 */
@Configuration(proxyBeanMethods = false)
class JpaJsonConfiguration {

    @Bean
    HibernatePropertiesCustomizer jsonFormatMapper(ObjectMapper objectMapper) {
        return properties ->
                properties.put(AvailableSettings.JSON_FORMAT_MAPPER, new JacksonJsonFormatMapper(objectMapper));
    }
}
