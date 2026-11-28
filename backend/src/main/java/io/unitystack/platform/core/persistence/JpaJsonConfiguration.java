package io.unitystack.platform.core.persistence;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.Jackson3JsonFormatMapper;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

/**
 * Makes Hibernate read and write {@code jsonb} columns with the application's JsonMapper, so stored JSON
 * matches the API's JSON and unknown fields in older rows are ignored rather than failing the read (TD-12).
 * Without this, Hibernate would build its own strict mapper.
 */
@Configuration(proxyBeanMethods = false)
class JpaJsonConfiguration {

    @Bean
    HibernatePropertiesCustomizer jsonFormatMapper(JsonMapper jsonMapper) {
        return properties ->
                properties.put(AvailableSettings.JSON_FORMAT_MAPPER, new Jackson3JsonFormatMapper(jsonMapper));
    }
}
