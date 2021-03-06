package com.vv.personal.prom.dbi;

import com.vv.personal.prom.dbi.config.DbiConfig;
import com.vv.personal.prom.dbi.controller.DbiCacheController;
import com.vv.personal.prom.dbi.interactor.ref.RefDbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.vv.personal.prom.dbi.constants.Constants.*;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class PromDbiServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromDbiServer.class);
    @Autowired
    private Environment environment;

    @Autowired
    private DbiCacheController dbiCacheController;

    @Autowired
    private DbiConfig dbiConfig;

    public static void main(String[] args) {
        SpringApplication.run(PromDbiServer.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.vv.personal.prom.dbi"))
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void firedUpAllCylinders() {
        String host = LOCALHOST;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("Failed to obtain ip address. ", e);
        }
        String port = environment.getProperty(LOCAL_SPRING_PORT);
        String herokuHost = environment.getProperty(SPRING_APPLICATION_HEROKU);
        LOGGER.info("DBI activation is complete! Expected Heroku Swagger running on: {}, exact url: {}",
                String.format(HEROKU_SWAGGER_UI_URL, herokuHost),
                String.format(SWAGGER_UI_URL, host, port));

        dbiCacheController.populateAllRefCache();
        LOGGER.info("Prepped overall cache => {}", dbiCacheController.displayAllRefTableCache());

        if (dbiConfig.isCreateTablesOnStartup()) {
            LOGGER.info("Creating tables on startup if non-existent!");
            dbiConfig.getRefDbis().forEach(RefDbi::createTableIfNotExists);
        }
    }

}
