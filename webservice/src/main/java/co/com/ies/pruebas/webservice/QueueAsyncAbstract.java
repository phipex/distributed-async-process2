package co.com.ies.pruebas.webservice;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public abstract class QueueAsyncAbstract<TasckType> {

    protected abstract void offer(TasckType element);
    protected abstract void publishNewElementsAdded();
    protected abstract Queue<TasckType> getQueue();
    protected abstract void processElement(TasckType element);

    public void offerTascks(List<TasckType> elements){
        //agregar a la cola
        elements.forEach(this::offer);
        //publicar nuevos elementos
        publishNewElementsAdded();
    }

    public void offerTasck(TasckType element){
        //agregar a la cola
        offer(element);
        //publicar nuevos elementos
        publishNewElementsAdded();
    }

    public void processQueue(){
        Queue<TasckType> elements = getQueue();
        System.out.println("QueueAsyncAbstract.processQueue elements = " + elements);
        System.out.println("QueueAsyncAbstract.processQueue elements = " + elements.size());
        // use un iterator en eves del for y un poll por que me quedaban completas las tareas
        final Iterator<TasckType> iterator = elements.iterator();
        while (iterator.hasNext()){
            final TasckType tasckType = iterator.next();
            try {
                processElement(tasckType);
                iterator.remove();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al procesar la tarea, se agrega de nuevo en la cola");
                offer(tasckType);
            }
        }

    }




}
