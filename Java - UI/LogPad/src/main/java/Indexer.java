import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
public class Indexer {
    static {
        System.loadLibrary("Indexer");
    }
    private  int    Category;
    public void setCategory(int category) { Category = category; }
    private String CategoryString;
    public void setCategoryString(String categoryString) { CategoryString = categoryString; }
    private String ComputerName;
    public void setComputerName(String computerName) { ComputerName = computerName; }
    private int    EventCode;
    public void setEventCode(int eventCode) { EventCode = eventCode; }
    private int    EventIdentifier;
    public void setEventIdentifier(int eventIdentifier) { EventIdentifier = eventIdentifier; }
    private int    EventType;
    public void setEventType(int eventType) { EventType = eventType; }
    private String Logfile;
    public void setLogfile(String logfile) { Logfile = logfile; }
    private String Message;
    public void setMessage(String message) { Message = message; }
    private int    RecordNumber;
    public void setRecordNumber(int recordNumber) { RecordNumber = recordNumber; }
    private String SourceName;
    public void setSourceName(String sourceName) { SourceName = sourceName; }
    private String TimeGenerated;
    public void setTimeGenerated(String timeGenerated) { TimeGenerated = timeGenerated; }
    private String TimeWritten;
    public void setTimeWritten(String timeWritten) { TimeWritten = timeWritten; }
    private String Type;
    public void setType(String type) { Type = type; }
    private String User;
    public void setUser(String user) { User = user; }
    private int    interrupt = 0;
    public int isInterrupted() {
        return interrupt;
    }
    private MainUI mainUI;
    public Indexer(MainUI mainUI) { this.mainUI = mainUI; }
    public void setInterrupt(int interrupt) {
        this.interrupt = interrupt;
    }
    public void ingest() throws IOException {
        DateTimeFormatter sysFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSSSS-000");
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        IndexRequest indexRequest = new IndexRequest("event-log");
        Map<String, Object> map = new HashMap<>();
        map.put("Category", Category);
        map.put("CategoryString", CategoryString);
        map.put("ComputerName", ComputerName);
        map.put("EventCode", EventCode);
        map.put("EventIdentifier", EventIdentifier);
        map.put("EventType", EventType);
        map.put("Logfile", Logfile);
        map.put("Message", Message);
        map.put("RecordNumber", RecordNumber);
        map.put("SourceName", SourceName);
        map.put("TimeGenerated", LocalDateTime.parse(TimeGenerated, sysFormat).plus(330, ChronoUnit.MINUTES));
        map.put("TimeWritten", LocalDateTime.parse(TimeWritten, sysFormat).plus(330, ChronoUnit.MINUTES));
        map.put("Type", Type);
        map.put("User", User);
        indexRequest.source(map);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        client.close();
        mainUI.incrementNC();
    }
    public static native void collect(Indexer indexer);
    public void start(Indexer indexer) {
        collect(indexer);
    }
}