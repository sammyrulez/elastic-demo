package it.eng;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.*;

/**
 * Created by sam on 23/09/16.
 */
public class DataGenerator {


    private static List<String> nomi;

    private static List<String> cognomi;

    private static List<String> indirizzi;


    public static void main(String[] args) throws Exception {

        cognomi = loadList("cognomi_italiani.txt");
        nomi = loadList("nomi_italiani.txt");
        indirizzi = loadList("vie_italiane.txt");

        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "demo").build();
        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.27.41"), 9300));

        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        Integer numOfP = Integer.parseInt(args[0]);

        ArrayList<Thread> tList = new ArrayList<Thread>();

        for (int i = 0; i < numOfP; i++) {

            final Person p = generatePerson();
            System.out.println(" indexing " + i + " " + p.toString());
            final PersonWriter pw = new PersonWriter(client, p);
            final Thread t = new Thread(pw);
            t.run();
            tList.add(t);

        }


        for (Thread t : tList) {
            t.join();
            System.out.println(t.getId() + " joined ");
        }


        client.close();
    }

    private static List loadList(String fileName) throws IOException {
        return IOUtils.readLines(DataGenerator.class.getResourceAsStream("/" + fileName), "UTF-8");
    }

    private static String pickFromList(List<String> data){
        final Random random = new Random();
        return data.get(random.nextInt(data.size()-1));
    }

    private static Person generatePerson() {
        return new Person(pickFromList(nomi), pickFromList(cognomi), pickFromList(indirizzi));
    }

}
