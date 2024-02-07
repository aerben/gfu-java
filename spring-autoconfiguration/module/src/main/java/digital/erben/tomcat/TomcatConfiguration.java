package digital.erben.tomcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration {

    @Bean
    @Conditional(TomcatOnClasspathCondition.class)
    public TomcatService tomcatLauncher() {
        return new TomcatService();
    }
}
