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


    public static Map<String,String> sensuEventMap = new HashMap<>();
    public static Map<String,String> ruleCheckMap = new HashMap<>();
    public static Map<String,String> ansibleInvokeMap = new HashMap<>();
    public static Map<String,String> ansibleStatusMap = new HashMap<>();
    public static Map<String,String> createIncidentMap = new HashMap<>();



    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        GlobalKTable<String, String> sensuEventTable = builder.globalTable(
                "sensu-failure",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("sensuEventMap"));

        GlobalKTable<String, String> ruleCheckMap = builder.globalTable(
                "event-decision",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("ruleCheckMap"));

        GlobalKTable<String, String> ansibleInvokeMap = builder.globalTable(
                "ansiblestat",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("ansibleInvokeMap"));

        GlobalKTable<String, String> ansibleStatusMap = builder.globalTable(
                "statuschck",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("ansibleStatusMap"));

        GlobalKTable<String, String> createIncidentMap = builder.globalTable(
                "crtincident",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("createIncidentMap"));

        GlobalKTable<String, String> failedRuleCheckMap = builder.globalTable(
                "failed-decision",
                Consumed.with(Serdes.String(), Serdes.String()),Materialized.as("failedDecision"));

        return builder.build();
    }






}
