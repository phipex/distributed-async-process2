package co.com.ies.pruebas.webservice.redis;

import co.com.ies.pruebas.webservice.Greeting;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

@Component
public class FinishedTasckRedis {

    private static final String KEY_LIST = "TaskTest_List";

    private final RedissonClient redissonClient;

    public FinishedTasckRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void add(Greeting tastTest){
        RSet<Long> lista = redissonClient.getSet(KEY_LIST);
        try {
            final long value = tastTest.getId();
            lista.add(value);
            System.out.println("FinishedTasckRedis.add agregando value = " + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int size(){
        RSet<Integer> lista = redissonClient.getSet(KEY_LIST);
        return lista.size();
    }

    public boolean contains(Greeting tastTest){
        //sout();
        RSet<Integer> lista = redissonClient.getSet(KEY_LIST);
        return lista.contains(tastTest.getId());
    }

    public boolean contains(Long value) {
        final Greeting greeting = new Greeting();
        greeting.setId(value);
        return contains(greeting);
    }


}
