package my.edu.umk.smart.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class MysqlDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        return getMysqlBasicDataSource();
    }

    private BasicDataSource getMysqlBasicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(env.getProperty("db.mysql.username"));
        dataSource.setPassword(env.getProperty("db.mysql.password"));
        dataSource.setUrl(env.getProperty("db.mysql.url"));
        dataSource.setDriverClassName(env.getProperty("db.mysql.driver"));
        dataSource.setMaxIdle(10);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(-1);
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }

    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate() {
        return new JdbcTemplate(getMysqlBasicDataSource());
    }
}
