package client.model;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class JsonConverterNTransporter {

    private final String data = "http://127.0.0.1:8080/calc/";
    private final String getID = "http://127.0.0.1:8080/calc/lastid";
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClientBuilder.create().build();


        public List<HistoryUnit> loadHistory(){
            try {
                URL url = new URL(data);
                return mapper.readValue(url, new TypeReference<List<HistoryUnit>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        }

        public void uploadHistory(List<HistoryUnit> historyUnits) {
            try {
                String json = mapper.writeValueAsString(historyUnits);
                HttpPost request = new HttpPost(data);
                StringEntity stringForSent = new StringEntity(json);
                request.addHeader("Content-Type", "application/json");
                request.setEntity(stringForSent);
                HttpResponse response = httpClient.execute(request);
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean dbIsntEmpty() {
            try {
                URL url = new URL(getID);
                return mapper.readValue(url, Long.class) > 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
}
