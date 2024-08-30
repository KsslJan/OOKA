package ooka.kessel.fluidanalysisms.kafka;

import ooka.kessel.fluidanalysisms.dto.AnalysisResult;
import ooka.kessel.fluidanalysisms.dto.ConfigurationRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;


    @Bean
    public ProducerFactory<String, AnalysisResult> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AnalysisResult> kafkaTemplate() {
        KafkaTemplate<String, AnalysisResult> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(producerListener());
        return kafkaTemplate;
    }

    @Bean
    public ProducerListener<String, AnalysisResult> producerListener() {
        return new ProducerListener<>() {
            @Override
            public void onSuccess(ProducerRecord<String, AnalysisResult> producerRecord, RecordMetadata recordMetadata) {
                logger.info("Message successfully produced to partition " + recordMetadata.partition() + " to topic " + recordMetadata.topic());
                logger.info("Produced record: " + producerRecord);
            }

            @Override
            public void onError(ProducerRecord<String, AnalysisResult> producerRecord, RecordMetadata recordMetadata,
                                Exception exception) {
                logger.error("Failed to produce to partition " + recordMetadata.partition() + " from topic " + recordMetadata.topic() + "\nRecord to be produced : " + recordMetadata);
            }
        };
    }

    @Bean
    public ConsumerFactory<String, ConfigurationRequest> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // automatically commits offset when receiving a message
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.TRUE);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        JsonDeserializer<ConfigurationRequest> deserializer = new JsonDeserializer<>(ConfigurationRequest.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(true);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConfigurationRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ConfigurationRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
