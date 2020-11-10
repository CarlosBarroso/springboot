package es.test.springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PersistenceConfiguration {

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;



    @Bean
    public DataSource dataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();

        builder.url(databaseUrl);
		System.out.println("Base de datos: " + databaseUrl);
        builder.username(username);
        builder.password(password);
        System.out.println("Mi DataSource es inicializado");
        return builder.build();
    }

}
