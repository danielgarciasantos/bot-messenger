package br.com.scopus.bot.messenger.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"br.com.scopus.messenger.core", "br.com.scopus.bot.messenger.banking"})
public class MessengerBankingApplication {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    } 

    /**
     * Entry point of the application. Run this method to start the sample bots,
     * but don't forget to add the correct tokens in application.properties file.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MessengerBankingApplication.class, args);
    }
}
