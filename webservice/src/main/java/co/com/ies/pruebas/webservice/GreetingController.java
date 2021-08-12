package co.com.ies.pruebas.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.ies.pruebas.webservice.redis.ServiceProcess;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Random;

@RestController
public class GreetingController {

    private static final String template = "Hello Docker, %s!";

    private final String hostAddress;


    @Autowired
    @Qualifier("remoteProcessQeueu")
    private ServiceProcess serviceProcessQeueu;

    private final GreetingRepository greetingRepository;

    public GreetingController(GreetingRepository greetingRepository) {
        String hostAddress1;
        this.greetingRepository = greetingRepository;
        try {
            hostAddress1 = InetAddress.getLocalHost().getHostAddress() ;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            hostAddress1 = "no found";
        }
        this.hostAddress = hostAddress1;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name",
        defaultValue="World") String name) {
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        return getGreeting(name);
    }

    private Greeting getGreeting(String name) {
        String format = String.format(template, name);
        return new Greeting(format, hostAddress);
    }

    @GetMapping("/greeting/new")
    public ResponseEntity<Greeting> createGreeting(){
        //var that = this;
        //System.out.println("that = " + that);
        Random random = new SecureRandom();
        int ran = random.nextInt();

        final Greeting nuevo = getGreeting("Nuevo" + ran);
        greetingRepository.save(nuevo);
        serviceProcessQeueu.process();
        return ResponseEntity.ok(nuevo);
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
            serviceProcessQeueu.process();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        
        return resultado;
    }
}