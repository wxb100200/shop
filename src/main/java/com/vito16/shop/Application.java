package com.vito16.shop;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author 木鱼 muyu@yiji.com
 * @version 2016/03/14
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class Application {
	
	@Value("${jdbc.driverClassName}")
	private String driverClassName;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${test.jdbc.driverClassName}")
	private String testDriverClassName;

	@Value("${test.jdbc.url}")
	private String testUrl;

	@Value("${test.jdbc.username}")
	private String testUsername;

	@Value("${test.jdbc.password}")
	private String testPassword;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	@Profile("test")
	public DataSource testDataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(testDriverClassName);
		druidDataSource.setUrl(testUrl);
		druidDataSource.setUsername(testUsername);
		druidDataSource.setPassword(testPassword);

		druidDataSource.setMaxActive(20);
		druidDataSource.setInitialSize(1);
		druidDataSource.setMaxWait(60000);
		druidDataSource.setMinIdle(1);

		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
		druidDataSource.setMinEvictableIdleTimeMillis(300000);

		druidDataSource.setValidationQuery("SELECT 'x'");
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(false);
		druidDataSource.setTestOnReturn(false);
		druidDataSource.setPoolPreparedStatements(true);
		druidDataSource.setMaxOpenPreparedStatements(20);
		return druidDataSource;
	}

	@Bean
	@Profile("production")
	public DataSource dataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		
		druidDataSource.setMaxActive(20);
		druidDataSource.setInitialSize(1);
		druidDataSource.setMaxWait(60000);
		druidDataSource.setMinIdle(1);
		
		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
		druidDataSource.setMinEvictableIdleTimeMillis(300000);
		
		druidDataSource.setValidationQuery("SELECT 'x'");
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(false);
		druidDataSource.setTestOnReturn(false);
		druidDataSource.setPoolPreparedStatements(true);
		druidDataSource.setMaxOpenPreparedStatements(20);
		return druidDataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan("com.vito16.shop.model");
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		jpaProperties.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
		
		entityManagerFactoryBean.setJpaProperties(jpaProperties);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		return entityManagerFactoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}

}
