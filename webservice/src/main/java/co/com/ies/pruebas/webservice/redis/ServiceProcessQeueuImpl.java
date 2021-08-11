package co.com.ies.pruebas.webservice.redis;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ServiceProcessQeueuImpl  implements ServiceProcessQeueu{

    @Async
    @Override
    public void processQeueu() {
        System.out.println("ServiceProcessQeueuImpl.processQeueu");
       
    }
}
