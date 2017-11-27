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
public class OraDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "oraDataSource")
    public DataSource oraDataSource() {
        return getOraBasicDataSource();
    }

    private BasicDataSource getOraBasicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUsername(env.getProperty("db.umksmart.username"));
        dataSource.setPassword(env.getProperty("db.umksmart.password"));
        dataSource.setUrl(env.getProperty("db.umksmart.url"));
        dataSource.setDriverClassName(env.getProperty("db.umksmart.driver"));
        dataSource.setMaxIdle(10);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(-1);
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }

    @Bean(name = "oraJdbcTemplate")
    public JdbcTemplate oraJdbcTemplate() {
        return new JdbcTemplate(getOraBasicDataSource());
    }
}
