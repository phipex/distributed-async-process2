package co.com.ies.pruebas.webservice;

import co.com.ies.pruebas.webservice.redis.ServiceProcess;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RedisConfig {

    public static final String REMOTE_PROCESS_QUEUE = "remoteProcessQeueu";

    @Autowired
    private Environment environment;

    @Autowired
    private ServiceProcess serviceProcessQeueu;

    @Bean
    public void registryRedisService(){
        RRemoteService remoteService = getRedisClient().getRemoteService();
        remoteService.register(ServiceProcess.class, serviceProcessQeueu);
    }

    @Bean
    @Qualifier(REMOTE_PROCESS_QUEUE)
    public ServiceProcess getRemoteProcessQeueu(){
        RRemoteService remoteService = getRedisClient().getRemoteService();
        return remoteService.get(ServiceProcess.class);

    }

    @Bean
    public RedissonClient getRedisClient(){
        System.out.println("Iniciando configuracion de redis");
        Config config = new Config();

        String host = getHost();
        final String stringConection = "redis://" + host + ":6379";
        System.out.println("stringConection = " + stringConection);
        config.useSingleServer()
                // use "rediss://" for SSL connection
                .setAddress(stringConection);

        final RedissonClient redissonClient = Redisson.create(config);

        System.out.println("finalizando la configuracion de redis");
        return redissonClient;
    }

    private String getHost() {
        String activeProfile = null;
        for (String profileName : environment.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
            if (activeProfile == null) {
                activeProfile = profileName;
            }
        }

        System.out.println("activeProfile = " + activeProfile);
        if("prod".equals(activeProfile)){
            return "redis";
        }
        return "127.0.0.1";
    }
}