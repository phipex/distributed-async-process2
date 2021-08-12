package co.com.ies.pruebas.webservice.redis;

import co.com.ies.pruebas.webservice.Greeting;
import co.com.ies.pruebas.webservice.QueueAsyncAbstract;
import org.redisson.api.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Set;

@Component
public class QeueuAsyncRedis  extends QueueAsyncAbstract<Greeting> {

    private static final String KEY_QEUEU = "TaskTest_Qeueu";

    private final RedissonClient redissonClient;
    private final ProcessorDelayedRedis processorDelayed;


    public QeueuAsyncRedis(RedissonClient redissonClient, ProcessorDelayedRedis processorDelayed) {
        this.redissonClient = redissonClient;
        this.processorDelayed = processorDelayed;
    }

    @Override
    protected void offer(Greeting element) {

        Set<Greeting> queue = getQueue();
        queue.add(element);
/*        final Long value = element.getId();
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();

        System.out.println("QeueuAsyncRedis.offer "+timeInMillis+" value = " + value +" queue = " + queue);
        RQueue<Greeting> queue2 = redissonClient.getQueue(KEY_QEUEU);
        System.out.println("QeueuAsyncRedis.offer  "+timeInMillis+" value = " + value +" queue2 = " + queue2);*/
    }

    @Async
    @Override
    protected void publishNewElementsAdded() {
        System.out.println("QeueuAsyncRedis.publishNewElementsAdded");
        publishEvents();
    }

    @Async
    void publishEvents() {
        System.out.println("Nueva tarea se ha agregado");
        // TODO llamar el servicio remoto para que procese la lista
        // TODO crear la tarea runable para que pueda ejecutar
        //serviceProcessQeueu.process();
    }

    @Override
    protected Set<Greeting> getQueue() {
        return redissonClient.getSet(KEY_QEUEU);
        //TODO mirar el tema de los listener

    }

    @Override
    protected void processElement(Greeting element) {
        System.out.println("QeueuAsyncRedis.processElement "+ element.getId());
        final RSet<Greeting> queue = redissonClient.getSet(KEY_QEUEU + "_1");
        queue.add(element);
        processorDelayed.processElement(element);

    }
}
