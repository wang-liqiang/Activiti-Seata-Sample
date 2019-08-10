package cn.xxxx.activiti.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.seata.rm.datasource.DataSourceProxy;

@Configuration
public class DataSourceConfig {

	public class MyHikariConfig extends HikariConfig {

		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public MyHikariConfig hikariConfig() {
		return new MyHikariConfig();
	}

	/**
	 * 需要将 DataSourceProxy 设置为主数据源，否则事务无法回滚
	 *
	 * @return The default datasource
	 */
	@Primary
	@Bean("dataSource")
	public DataSource dataSource() {
		MyHikariConfig hikariConfig = hikariConfig();
		hikariConfig.setJdbcUrl(hikariConfig.getUrl());
		DataSourceProxy dataSource = new DataSourceProxy(new HikariDataSource(hikariConfig));
		System.out.println("Initialized DataSourceProxy: " + dataSource);
		return dataSource;
	}

}