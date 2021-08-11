package co.com.ies.pruebas.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.ies.pruebas.webservice.redis.ServiceProcessQeueu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello Docker, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    @Qualifier("remoteProcessQeueu")
    private ServiceProcessQeueu serviceProcessQeueu;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name",
        defaultValue="World") String name) {
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }    
        
        String format = String.format(template, name);
        String ip = "No found";

        // Local address
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress() ;
            String hostName = InetAddress.getLocalHost().getHostName();
            ip = hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new Greeting(counter.incrementAndGet(),
                format, ip);
    }

    @GetMapping("/servicio")
    public String llamadoServicio(){
        
        String resultado = "LLamado: ";
        Random random = new SecureRandom();
        try {
            int ran = random.nextInt();
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            resultado = resultado + hostAddress + " ran = " + ran;
            System.out.println(resultado);
            serviceProcessQeueu.processQeueu(ran);    
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        
        return resultado;
    }
}