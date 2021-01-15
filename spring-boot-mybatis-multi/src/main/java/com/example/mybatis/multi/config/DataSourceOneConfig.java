package com.example.mybatis.multi.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 将创建的 SqlSessionTemplate 注入到对应的 Mapper 包路径下，这样这个包下面的 Mapper 都会使用第一个数据源来进行数据库操作
 *
 * @author jingLv
 * @date 2021/01/15
 */
@Configuration
@MapperScan(basePackages = "com.example.mybatis.multi.mapper.one", sqlSessionTemplateRef = "oneSqlSessionTemplate")
public class DataSourceOneConfig {
    /**
     * 加载配置的数据源
     *
     * @return
     */
    @Bean(name = "oneDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.one")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 根据创建的数据源，构建对应的 SqlSessionFactory
     * 代码中需要指明需要加载的 Mapper xml 文件。
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "oneSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("oneDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/one/*.xml"));
        return bean.getObject();
    }

    /**
     * 将数据源添加到事务中
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "oneTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("oneDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 将上面创建的 SqlSessionFactory 注入，创建我们在 Mapper 中需要使用的 SqlSessionTemplate
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "oneSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("oneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
