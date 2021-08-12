package co.com.ies.pruebas.webservice.redis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import co.com.ies.pruebas.webservice.Greeting;
import co.com.ies.pruebas.webservice.GreetingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ServiceProcessImpl implements ServiceProcess {

    private final GreetingRepository greetingRepository;
    private final QeueuAsyncRedis qeueuAsyncRedis;

    public ServiceProcessImpl(GreetingRepository greetingRepository, QeueuAsyncRedis qeueuAsyncRedis) {
        this.greetingRepository = greetingRepository;
        this.qeueuAsyncRedis = qeueuAsyncRedis;
    }

    @Async
    @Deprecated
    public void process2() {
        System.out.println("ServiceProcessQeueuImpl.processQeueu");
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ServiceProcessQeueuImpl.processQeueu :: " + hostAddress);
            final List<Greeting> greetingsByIpTramitedIsNull = greetingRepository.getGreetingsByIpTramitedIsNull();
            for (Greeting greeting :greetingsByIpTramitedIsNull) {
                if(greeting.getIpTramited() != null){
                    System.out.println("ServiceProcessQeueuImpl.processQeueu ---------------------------------------");
                    continue;
                }

                greeting.setIpTramited(hostAddress);
                greetingRepository.save(greeting);
                final int timeSleep = 2000;

                try {
                    Thread.sleep(timeSleep);
                    //System.out.println("ProcessorDelayedRedis.processElement timeSleep = "+ timeSleep);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }

    @Async
    @Override
    public void process() {
        final List<Greeting> greetingsByIpTramitedIsNull = greetingRepository.getGreetingsByIpTramitedIsNull();

        qeueuAsyncRedis.offerTascks(greetingsByIpTramitedIsNull);

    }

    @Async
    @Override
    public void process(int valor) {
        System.out.println("ServiceProcessQeueuImpl.processQeueu:: valor ="+valor);
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("ServiceProcessQeueuImpl.processQeueu :: valor ="+valor+" host " + hostAddress);



        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
    }
}
