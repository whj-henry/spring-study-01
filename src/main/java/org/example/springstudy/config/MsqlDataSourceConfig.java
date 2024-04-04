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
 * @ClassName MsqlDataSourceConfig.java
 * @Description mysql 数据库配置
 * @createTime 2021年01月15日 09:02:00
 */
@Configuration
@Slf4j
@MapperScan(basePackages = "org.example.springstudy.mapper.mysql",
        sqlSessionFactoryRef = "mysqlDataSourceFactory")
public class MsqlDataSourceConfig {
    /**
     * @description mysql DataSource Properties 配置
     * @updateTime  2021/1/15 9:04
     */
    @Bean(name = "mysqlDataSourceProperties")
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * @description   mysql DataSource 源配置
     * @updateTime  2021/1/15 9:11
     */
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties("spring.datasource.mysql.configuration")
    public javax.sql.DataSource mysqlDataSource() {
        DataSourceProperties dataSourceProperties = mysqlDataSourceProperties();
        log.info(dataSourceProperties.getUrl());
        // 数据库连接池设置为 Hikari
        return mysqlDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }


    // 以下内容和 mybatis 相关
    /**
     * @description mysql 工厂配置
     * @updateTime  2021/1/15 9:18
     */
    @Bean("mysqlDataSourceFactory")
    @DependsOn("mysqlDataSource")
    public SqlSessionFactory dataSourceFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(mysqlDataSource());
        return factoryBean.getObject();
    }

    /**
     * @description mysql session 模板配置
     * @updateTime  2021/1/15 9:22
     */

    @Bean("mysqlSqlSessionTemplate")
    @DependsOn("mysqlDataSourceFactory")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mysqlDataSourceFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    /**
     * @description mysql  事务配置
     * @updateTime  2021/1/15 9:22
     */
    @Bean(name = "mysqlTransactionManager")
    @DependsOn("mysqlDataSource")
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}