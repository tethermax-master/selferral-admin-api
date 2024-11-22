package io.selferral.admin.api.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value = "io.selferral.admin.api.mapper")
public class MybatisConfig {

    @Bean
    @Primary
    SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, ApplicationContext ctx) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setTypeAliasesPackage("io.selferral.admin.api.model.entity");
        factoryBean.setMapperLocations(ctx.getResources("classpath:/mappers/*.xml"));
        factoryBean.setConfigLocation(ctx.getResource("classpath:/config/mybatis-config.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix="spring.datasource.hikari")
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
}