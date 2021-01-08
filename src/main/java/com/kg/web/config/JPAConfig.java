package com.kg.web.config;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories("com.kg.web.*")
public class JPAConfig {

	@Value("${db.driverClassName:#{null}}")
	private String dbClassName;

	@Value("${db.url:#{null}}")
	private String dbUrl;

	@Value("${db.username:#{null}}")
	private String dbUsername;

	@Value("${db.password:#{null}}")
	private String dbPassword;
	
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
      return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setDatabase(Database.MYSQL);
      jpaVendorAdapter.setGenerateDdl(false);
      return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
      lemfb.setDataSource(dataSource());
      lemfb.setJpaVendorAdapter(jpaVendorAdapter());
      lemfb.setPackagesToScan("com.kg.web.model");
      return lemfb;
    }

}
