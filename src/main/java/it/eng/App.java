package it.eng;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import com.fasterxml.jackson.databind.*;

/**
 * Created by sam on 23/09/16.
 */
public class App {



    public static void main(String[] args) throws Exception {

        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "demo").build();
        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.27.41"), 9300));

        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

// generate json
        byte[] json = mapper.writeValueAsBytes(new Person("sam","rgh"," vai da qui"));
        System.out.println(new String(json));
        IndexResponse response = client.prepareIndex("people", "person")
                .setSource(json)
                .get();

        System.out.println(response.isCreated() +  " " + response.getIndex());



        client.close();
    }

}
