package edu.refactor.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@Configuration
public class DemoApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);

//        ResponseEntity<String> responseEntity = new RestTemplate()
//                .getForEntity(VAL_CURS_FROM_CBR_URL, String.class);
//
//        String body = responseEntity.getBody();
//
//        XmlMapper xmlMapper = new XmlMapper();
//        ValCurs valCurs = xmlMapper.readValue(body, ValCurs.class);
//
//        Date date = valCurs.getDate();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}