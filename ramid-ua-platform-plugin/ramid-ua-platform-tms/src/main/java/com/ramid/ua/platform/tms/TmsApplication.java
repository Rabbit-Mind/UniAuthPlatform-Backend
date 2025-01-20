package com.ramid.ua.platform.tms;

import com.ramid.framework.security.configuration.client.annotation.EnableOAuth2Client;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;

/**
 * @author Levin
 */
@Slf4j
@EnableOAuth2Client
@EnableDiscoveryClient
@EnableFeignClients("com.ramid")
@MapperScan(value = "com.ramid.**.mapper", annotationClass = Repository.class)
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
public class TmsApplication {

    @SneakyThrows
    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(TmsApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        final String appName = env.getProperty("spring.application.name");
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        log.info("""

                        ----------------------------------------------------------
                        \tApplication '{}' is running! Access URLs:
                        \tDoc: \thttp://{}:{}/doc.html
                        ----------------------------------------------------------""",
                appName, host, port);
    }
}
