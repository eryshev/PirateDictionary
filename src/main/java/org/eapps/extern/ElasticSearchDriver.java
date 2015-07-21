package org.eapps.extern;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.Map;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Created by eryshev-alexey on 12/07/15.
 */
//TODO add logging
public class ElasticSearchDriver {
    private static final String CLUSTER_NAME = "eapps_es_cluster";
    private static final String INDEX = "pirate_dictionary";
    private static final String TYPE = "dict";
    private static final String USER_TYPE = "user";
    private static final String SECRET_TYPE = "secret";

    private Node node;
    private Client client;

    public ElasticSearchDriver() {
        node = nodeBuilder()
                .clusterName(CLUSTER_NAME)
                .client(true)
                .node();
        client = node.client();
    }

    private SearchResponse getResponseByName(String name) {
        SearchRequestBuilder query = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setFetchSource(null, "name_na")
                .setQuery(QueryBuilders.filteredQuery(null, FilterBuilders.termFilter("name_na", name)));

        return query
                .execute()
                .actionGet();
    }

    public String getByNameAsString(String name) {
        SearchResponse response = getResponseByName(name);
        if (response == null)
            return "[]";
        else {
            String resString = "";
            for(SearchHit hit : response.getHits().getHits()) {
                resString = resString + hit.getSourceAsString() + ",";
            }
            return resString.length() == 0 ? "[]" : "[" + resString.substring(0, resString.length() - 1) + "]";
        }
    }

    @Deprecated
    public ArrayList<Map<String, Object>> getByNameAsObjectsList(String name) {
        SearchResponse response = getResponseByName(name);
        if (response == null)
            return null;
        else {
            ArrayList<Map<String, Object>> resJsonArray = new ArrayList<Map<String, Object>>();
            for(SearchHit hit : response.getHits().getHits()) {
                resJsonArray.add(hit.getSource());
            }
            return resJsonArray;
        }
    }

    public void put(String termString) {
        IndexRequestBuilder query = client.prepareIndex(INDEX, TYPE).setSource(termString);
        IndexResponse response = query.execute().actionGet();
    }

    @Deprecated
    public void putList(ArrayList<String> termsAsJson) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for(String termAsJson: termsAsJson) {
            bulkRequest.add(client.prepareIndex(INDEX, TYPE)
                            .setSource(termAsJson)
            );
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            //TODO handle this case;
        }
    }

    public void delete(String name, String author) {
        SearchRequestBuilder query = client.prepareSearch(INDEX)
                .setTypes(TYPE)
                .setFetchSource(false)
                .setQuery(QueryBuilders.filteredQuery(null,
                        FilterBuilders.andFilter(
                                FilterBuilders.termFilter("name_na", name),
                                FilterBuilders.termFilter("author", author)
                        )));

        SearchResponse response = query.execute().actionGet();

        if (response == null || response.getHits().getTotalHits() == 0) {
            //TODO handle this case
        }
        else {
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            for(SearchHit hit : response.getHits().getHits())
                bulkRequest.add(client.prepareDelete(INDEX, TYPE, hit.getId()));

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        }
    }

    public String[] getSecretById(String id) {
        GetResponse response = client.prepareGet(INDEX, SECRET_TYPE, id)
                .execute()
                .actionGet();

        try {
            String[] responseArray = new String[2];
            responseArray[0] = response.getSource().get("salt").toString();
            responseArray[1] = response.getSource().get("secret").toString();
            return responseArray;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public String getSecretString(String[] secret) {
        return "{\"salt\":\"" + secret[0] + "\"," +
                "\"secret\":\"" + secret[1] + "\"}";
    }

    public void putUserPD(String userString, String userId, String[] secret) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        bulkRequest.add(client.prepareIndex(INDEX, USER_TYPE, userId).setSource(userString))
                .add(client.prepareIndex(INDEX, SECRET_TYPE, userId).setSource(getSecretString(secret)));
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if (bulkResponse.hasFailures()) {
            //TODO handle this case;
        }
    }

    public byte[] getUserById(String userId) {
        GetResponse response = client.prepareGet(INDEX, USER_TYPE, userId)
                .execute()
                .actionGet();

        if (response.isExists()) {
            return response.getSourceAsBytes();
        }
        else return null;
    }

    public void close() {
        node.close();
    }
}
