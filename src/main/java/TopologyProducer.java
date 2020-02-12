import com.google.gson.Gson;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/events")
@ApplicationScoped
public class TopologyProducer {

    @Inject
    KafkaStreams streams;

    @GET
    @Path("/txn-event/{custId}")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String getCase(String json,@javax.ws.rs.PathParam("custId") String customerId) {
        Map<String,String> clusterKeyValueMap = new HashMap<>();
        System.out.println("inside getCase");
        ReadOnlyKeyValueStore view = streams.store("CountsWindowStore", QueryableStoreTypes.keyValueStore());
        try (KeyValueIterator<String, String> clusterKeyValueIterator = view.all()) {
            System.out.println("Approximate Num. of Entries in Infra Table-{}"+ view.approximateNumEntries());
            while (clusterKeyValueIterator.hasNext()) {
                KeyValue<String, String> next = clusterKeyValueIterator.next();
                clusterKeyValueMap.put(next.key, next.value);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Infra Manager State Store not initialized ", e);
        }
        System.out.println("customerId"+customerId);

        System.out.println(clusterKeyValueMap.get("\""+customerId+"\""));

        LinkedHashMap<String,String> mapVal = new Gson().fromJson(clusterKeyValueMap.get("\""+customerId+"\""),LinkedHashMap.class);


        return  mapVal.get("activeOffers");
    }
}