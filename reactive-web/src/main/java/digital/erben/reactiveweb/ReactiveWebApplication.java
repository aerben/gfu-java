package digital.erben.reactiveweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class ReactiveWebApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(ReactiveWebApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
