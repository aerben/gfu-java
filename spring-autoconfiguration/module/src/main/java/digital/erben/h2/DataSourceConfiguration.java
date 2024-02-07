package digital.erben.h2;

import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Conditional(DriverNameIsSetCondition.class)
    public DataSource dataSource(Environment environment) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String driverClassName = environment.getProperty("spring.jdbc.driver");
        Driver driver = (Driver) Class.forName(driverClassName).newInstance();
        String url = environment.getProperty("spring.jdbc.url");
        return new SimpleDriverDataSource(driver, url);
    }
}
