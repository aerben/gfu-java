package digital.erben;


import digital.erben.h2.DataSourceConfiguration;
import digital.erben.tomcat.TomcatConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        DataSource ds = ctx.getBean(DataSource.class);
        boolean isValid = ds.getConnection().isValid(1000);
        System.out.println("Connection is valid: " + isValid);
    }

    @Import({TomcatConfiguration.class, DataSourceConfiguration.class})
    @PropertySources(
        {
            @PropertySource(value="classpath:application.properties"),
            @PropertySource(value="classpath:application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
        }
    )
    public static class AppConfig {

    }

}
