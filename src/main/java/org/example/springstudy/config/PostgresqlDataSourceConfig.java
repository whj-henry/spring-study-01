package org.example.springstudy.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author senn
 * @version 1.0.0
 * @ClassName DataSourceConfig.java
 * @Description psotgresql 数据库配置
 * @createTime 2021年01月14日 15:56:00
 */
@Configuration
@Slf4j
@MapperScan(basePackages = "org.example.springstudy.mapper.postgresql",
        sqlSessionFactoryRef = "postgresqlDataSourceFactory")
public class PostgresqlDataSourceConfig {


    /**
     * @description postgresql DataSource Properties 配置
     * @updateTime  2021/1/15 9:04
    */

    @Bean(name = "postgresqlDataSourceProperties")
    @ConfigurationProperties("spring.datasource.postgresql")
    public DataSourceProperties postgresqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * @description   postgresql DataSource 源配置
     * @param
     * @updateTime  2021/1/15 9:11
     * @throws
    */
    @Bean(name = "postgresqlDataSource")
    @ConfigurationProperties("spring.datasource.postgresql.configuration")
    public DataSource postgresqlDataSource() {
        DataSourceProperties dataSourceProperties = postgresqlDataSourceProperties();
        log.info(dataSourceProperties.getUrl());
        // 数据库连接池设置为 Hikari
        return postgresqlDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    // 以下内容和 mybatis 相关

    /**
     * @description postgresql 工程配置
     * @updateTime  2021/1/15 9:18
    */
    @Bean("postgresqlDataSourceFactory")
    @DependsOn("postgresqlDataSource")
    public SqlSessionFactory dataSourceFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(postgresqlDataSource());
        return factoryBean.getObject();
    }

    /**
     * @description postgresql session 模板配置
     * @updateTime  2021/1/15 9:22
    */

    @Bean("postgresqlSqlSessionTemplate")
    @Primary
    @DependsOn("postgresqlDataSourceFactory")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("postgresqlDataSourceFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    /**
     * @description postgresql  事务配置
     * @updateTime  2021/1/15 9:22
     */
    @Bean(name = "postgresqlTransactionManager")
    @DependsOn("postgresqlDataSource")
    public DataSourceTransactionManager fawkesTransactionManager(@Qualifier("postgresqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}