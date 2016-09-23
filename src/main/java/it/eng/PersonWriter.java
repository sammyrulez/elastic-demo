package it.eng;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;

/**
 * Created by sam on 23/09/16.
 */
public class PersonWriter implements Runnable {


    TransportClient client;

    Person person;

    final ObjectMapper mapper = new ObjectMapper();

    private boolean runned = false;

    public PersonWriter(TransportClient client, Person person) {
        this.client = client;
        this.person = person;
    }

    public void run() {
        if(!runned) {

            try {
                final byte[] json = mapper.writeValueAsBytes(person);
                IndexResponse response = client.prepareIndex("people", "person")
                        .setSource(json)
                        .get();
                if(!response.isCreated()){
                    System.err.println("Errore creando " + new String(json) + " \n " + response.getId() );
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        runned = true;

    }
}
