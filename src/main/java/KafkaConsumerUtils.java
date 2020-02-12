import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class KafkaConsumerUtils {


    private static final String EVENT_INP_STREAM = "event-input-stream";
    public static Map<String,String> custMap = new HashMap<>();

    @GET
    @Path("/txn-event/{custId}")
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String getCase(String json,@javax.ws.rs.PathParam("custId") String customerId) {
        System.out.println("inside getCase");

        return custMap.get(customerId);
    }

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        GlobalKTable<String, String> stations = builder.globalTable(
                EVENT_INP_STREAM,
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("CountsWindowStore"));

        return builder.build();
    }






}
