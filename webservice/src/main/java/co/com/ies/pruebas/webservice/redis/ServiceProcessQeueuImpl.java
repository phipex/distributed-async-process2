package co.com.ies.pruebas.webservice.redis;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ServiceProcessQeueuImpl  implements ServiceProcessQeueu{

    @Async
    @Override
    public void processQeueu() {
        System.out.println("ServiceProcessQeueuImpl.processQeueu");
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ServiceProcessQeueuImpl.processQeueu :: " + hostAddress);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }

    @Async
    @Override
    public void processQeueu(int valor) {
        System.out.println("ServiceProcessQeueuImpl.processQeueu:: valor ="+valor);
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ServiceProcessQeueuImpl.processQeueu :: valor ="+valor+" host " + hostAddress);
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }
}
