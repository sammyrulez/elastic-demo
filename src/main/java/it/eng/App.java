package it.eng;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Stack;

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

        Integer numOfP = Integer.parseInt(args[0]);

        ArrayList<Thread> tList = new ArrayList<Thread>();

        for (int i = 0; i < numOfP ; i++){

            final Person p = generatePerson();
            final PersonWriter pw = new PersonWriter(client,p);
            final Thread t = new Thread(pw);
            t.run();
            tList.add(t);

        }


        for(Thread t:tList){
            t.join();
        }


        client.close();
    }

    private static Person generatePerson() {
        return null;
    }

}
