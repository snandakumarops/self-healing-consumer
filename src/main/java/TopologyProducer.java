import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import datamodels.example.ApbRuns;
import datamodels.example.Example;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/events")
@ApplicationScoped
public class TopologyProducer {

    @Inject
    KafkaStreams streams;

    @GET
    @Path("/txn-event/{hostName}")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String getCase(String json,@javax.ws.rs.PathParam("hostName") String host) {


        Object mapVal = getStringStringLinkedHashMap(host,"sensuEventMap");


        Example example = new Gson().fromJson(mapVal.toString(),Example.class);


        Map<String,Object> mapValues= new HashMap<>();
        mapValues.put("sensuEventMap", example);



        Object ruleFailed = getStringStringLinkedHashMap(example.getId(),"failedDecision");

        System.out.println("ruleFailed"+ruleFailed);

       if(null == ruleFailed) {

           ruleFailed = "Rule Success";

           Object ruleCheck = getStringStringLinkedHashMap(host, "ruleCheckMap");

           ApbRuns apbRuns = new Gson().fromJson(ruleCheck.toString(), ApbRuns.class);





           Object ansibleInvokeMap = getStringStringLinkedHashMap(host, "ansibleInvokeMap");
           int ansibleInvokeId = 0;

           if (null != ansibleInvokeMap) {
               Double d = Double.parseDouble(ansibleInvokeMap.toString());
               ansibleInvokeId = d.intValue();
           }



           Object ansibleStatusMap = getStringStringLinkedHashMap(host, "ansibleStatusMap");



           Object createIncidentMap = getStringStringLinkedHashMap(host, "createIncidentMap");



           mapValues.put("apbRuns", apbRuns);
           mapValues.put("ansibleInvokeMap", ansibleInvokeId);
           mapValues.put("ansibleStatusMap", ansibleStatusMap.toString().replace("\"", ""));
           if (createIncidentMap != null) {
               mapValues.put("createIncidentMap", ansibleStatusMap.toString().replace("\"", "").equals("failed") ? "Yes" : "No");
           }

           Date currentDate = new Date(apbRuns.getRunDate());
           mapValues.put("lastRunDate", currentDate);
       }
        Date eventDate = new Date(example.getCheck().getExecuted());
        mapValues.put("eventDate",eventDate);

        mapValues.put("sensuId",example.getId());

        mapValues.put("ruleCheckFailed",ruleFailed);


        return new Gson().toJson(mapValues);
    }

    private Object getStringStringLinkedHashMap(String hostName, String storeName) {
        System.out.println("storename"+storeName+hostName);
        Map<String,String> clusterKeyValueMap = new HashMap<>();
        ReadOnlyKeyValueStore view = streams.store(storeName, QueryableStoreTypes.keyValueStore());
        try (KeyValueIterator<String, String> clusterKeyValueIterator = view.all()) {
            System.out.println("Approximate Num. of Entries in Infra Table-{}"+ view.approximateNumEntries());
            while (clusterKeyValueIterator.hasNext()) {
                KeyValue<String, String> next = clusterKeyValueIterator.next();
                if(!next.key.startsWith("\"")) {
                    clusterKeyValueMap.put("\""+next.key+"\"", next.value);
                } else {
                    clusterKeyValueMap.put(next.key, next.value);
                }


            }
        } catch (Exception e) {
            throw new IllegalStateException("Infra Manager State Store not initialized ", e);
        }



        System.out.println(clusterKeyValueMap.get(hostName));

        return clusterKeyValueMap.get(new Gson().toJson(hostName));
    }
}