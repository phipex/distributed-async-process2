package co.com.ies.pruebas.webservice.redis;

import co.com.ies.pruebas.webservice.Greeting;
import co.com.ies.pruebas.webservice.QueueAsyncAbstract;
import co.com.ies.pruebas.webservice.RedisConfig;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Queue;

@Component
public class QeueuAsyncRedis  extends QueueAsyncAbstract<Greeting> {

    private static final String KEY_QEUEU = "TaskTest_Qeueu";

    @Autowired
    @Qualifier(RedisConfig.REMOTE_PROCESS_QUEUE)
    private ServiceProcess serviceProcessQeueu;

    private final RedissonClient redissonClient;
    private final ProcessorDelayedRedis processorDelayed;


    public QeueuAsyncRedis(RedissonClient redissonClient, ProcessorDelayedRedis processorDelayed) {
        this.redissonClient = redissonClient;
        this.processorDelayed = processorDelayed;
    }

    @Override
    protected void offer(Greeting element) {

        RQueue<Greeting> queue = redissonClient.getQueue(KEY_QEUEU);
        queue.add(element);
        final Long value = element.getId();
        final long timeInMillis = Calendar.getInstance().getTimeInMillis();

        System.out.println("QeueuAsyncRedis.offer "+timeInMillis+" value = " + value +" queue = " + queue);
        RQueue<Greeting> queue2 = redissonClient.getQueue(KEY_QEUEU);
        System.out.println("QeueuAsyncRedis.offer  "+timeInMillis+" value = " + value +" queue2 = " + queue2);
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
        serviceProcessQeueu.process();
    }

    @Override
    protected Queue<Greeting> getQueue() {
        final RQueue<Greeting> queue = redissonClient.getQueue(KEY_QEUEU);
        //TODO mirar el tema de los listener
        return queue;
    }

    @Override
    protected void processElement(Greeting element) {
        System.out.println("QeueuAsyncRedis.processElement "+ element.getId());
        final RQueue<Greeting> queue = redissonClient.getQueue(KEY_QEUEU + "_1");
        queue.add(element);
        processorDelayed.processElement(element);

    }
}
