import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
@Path("/events")
@ApplicationScoped
public class TopologyProducer {

    @Inject
    KafkaStreams streams;

    @GET
    @Path("/txn-event/{custId}")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String getCase(String json,@javax.ws.rs.PathParam("custId") String customerId) {
        System.out.println("inside getCase");
        ReadOnlyKeyValueStore view = streams.store("CountsWindowStore", QueryableStoreTypes.keyValueStore());
        return view.get(customerId).toString();

    }
}