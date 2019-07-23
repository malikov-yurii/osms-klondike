package com.malikov.shopsystem.dao.gilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "gilderEntityManagerFactory",
        transactionManagerRef = "gilderTransactionManager",
        basePackages = {
                "com.malikov.shopsystem.dao.gilder.repository"
        }
)
public class GilderPersistenceConfiguration {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "gilderDataSource")
    public DataSource gilderDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));

        return dataSource;
    }

    @Bean(name = "gilderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean gilderEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("gilderDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.malikov.shopsystem.dao.gilder")
                .persistenceUnit("db1")
                .build();
    }

    @Bean(name = "gilderTransactionManager")
    public PlatformTransactionManager gilderTransactionManager(
            @Qualifier("gilderEntityManagerFactory") EntityManagerFactory gilderEntityManagerFactory) {
        return new JpaTransactionManager(gilderEntityManagerFactory);
    }

}