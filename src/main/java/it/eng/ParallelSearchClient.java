package it.eng;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.util.Iterator;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

/**
 * Created by sam on 23/09/16.
 */
public class ParallelSearchClient {

    public static void main(String[] args) throws Exception {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "demo").build();
        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.27.41"), 9300));

        SearchResponse response = client.prepareSearch("people")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("name", "Abele"))                 // Query
               // .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
               // .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        Iterator<SearchHit> iterator = response.getHits().iterator();
        while (iterator.hasNext()){
            SearchHit s = iterator.next();
            Iterator<SearchHitField> searchHitFieldIterator = s.iterator();
            while (searchHitFieldIterator.hasNext()){
                SearchHitField f = searchHitFieldIterator.next();
                System.out.println(f.getName() + " " + f.getValue());
            }
            System.out.println("____________________________________________");
        }
    }

}
