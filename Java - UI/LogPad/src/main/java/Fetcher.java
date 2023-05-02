import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Fetcher {
    public int totalHits;
    public Object[][] tableData;
    public Object[][] detailData;
    private void fetch(String query) {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("event-log");
        searchRequest.preference("_local");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10_000);
        searchSourceBuilder.query(QueryBuilders.simpleQueryStringQuery(query));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        SearchHits hits = searchResponse.getHits();
        totalHits = (int) hits.getTotalHits().value;
        SearchHit[] searchHits = hits.getHits();
        Map<String, Object> sourceAsMap = new HashMap<>();
        int count = 0;
        tableData = new Object[totalHits][6];
        detailData = new Object[totalHits][14];
        for (SearchHit hit : searchHits) {
            sourceAsMap = hit.getSourceAsMap();
            tableData[count] = new Object[] {
                    (String) sourceAsMap.get("Type"),
                    (String) sourceAsMap.get("TimeWritten"),
                    (String) sourceAsMap.get("SourceName"),
                    (int) sourceAsMap.get("EventIdentifier"),
                    (String) sourceAsMap.get("CategoryString"),
                    count
            };
            detailData[count++] = new Object[]{
                    (int) sourceAsMap.get("Category"),
                    (String) sourceAsMap.get("CategoryString"),
                    (String) sourceAsMap.get("ComputerName"),
                    (int) sourceAsMap.get("EventCode"),
                    (int) sourceAsMap.get("EventIdentifier"),
                    (int) sourceAsMap.get("EventType"),
                    (String) sourceAsMap.get("Logfile"),
                    (String) sourceAsMap.get("Message"),
                    (int) sourceAsMap.get("RecordNumber"),
                    (String) sourceAsMap.get("SourceName"),
                    (String) sourceAsMap.get("TimeGenerated"),
                    (String) sourceAsMap.get("TimeWritten"),
                    (String) sourceAsMap.get("Type"),
                    (String) sourceAsMap.get("User")};
        }
        try {
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public Fetcher(String query) {
        fetch(query);
    }
}
