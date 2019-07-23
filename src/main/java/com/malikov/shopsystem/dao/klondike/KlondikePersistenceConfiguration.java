package com.malikov.shopsystem.dao.klondike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "klondikeEntityManagerFactory",
        transactionManagerRef = "klondikeTransactionManager",
        basePackages = {
                "com.malikov.shopsystem.dao.klondike.repository"
        }
)
public class KlondikePersistenceConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource klondikeDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.klondike-datasource.url"));
        dataSource.setUsername(env.getProperty("spring.klondike-datasource.username"));
        dataSource.setPassword(env.getProperty("spring.klondike-datasource.password"));
        dataSource.setDriverClassName(env.getProperty("spring.klondike-datasource.driver-class-name"));

        return dataSource;
    }

    @Bean(name = "klondikeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean klondikeEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("klondikeDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.malikov.shopsystem.dao.klondike")
                .persistenceUnit("db1")
                .build();
    }

    @Bean(name = "klondikeTransactionManager")
    public PlatformTransactionManager klondikeTransactionManager(
            @Qualifier("klondikeEntityManagerFactory") EntityManagerFactory klondikeEntityManagerFactory) {
        return new JpaTransactionManager(klondikeEntityManagerFactory);
    }

}