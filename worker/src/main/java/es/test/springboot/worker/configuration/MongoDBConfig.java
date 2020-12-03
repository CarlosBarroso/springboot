package es.test.springboot.worker.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.integration.mongodb.store.MongoDbChannelMessageStore;
import org.springframework.integration.store.MessageGroupStore;

import java.util.Arrays;

@Configuration
public class MongoDBConfig {


    /*
    @Bean
    public MessageGroupStore debugMessageStore(
            MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter
    ){
        return new MongoDbChannelMessageStore(
                mongoDbFactory, mappingMongoConverter, "sesiones"  );
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new MediaTypeToStringConverter(), new StringToMediaTypeConverter(),
                new OffsetDteTimeToStringConverter(), new StringToOffsetDateTimeConverter()));

    }

     */
}
