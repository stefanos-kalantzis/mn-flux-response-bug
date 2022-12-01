package mn.flux.response.bug.repository;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static java.time.Duration.ofSeconds;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Context
public class SchemaBootstrap {

    public static final String KEYSPACE_NAME = "revisions";
    public static final String TABLE_REVISION_NAME = "revision";

    /**
     * Constructor initialises cassandra keyspace and table.
     */
    @Inject
    public SchemaBootstrap(CqlSession cqlSession) {
        cqlSession.execute(
            createKeyspace(KEYSPACE_NAME)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
                .setTimeout(ofSeconds(10)));

        cqlSession.execute(
            createTable(KEYSPACE_NAME, TABLE_REVISION_NAME)
                .ifNotExists()
                .withPartitionKey("resource_id", DataTypes.TEXT)
                .withClusteringColumn("resource_name", DataTypes.TEXT)
                .withClusteringColumn("id", DataTypes.TIMEUUID)
                .withColumn("date", DataTypes.TIMESTAMP)
                .withColumn("revision_data", DataTypes.TEXT)
                .withColumn("comment", DataTypes.TEXT)
                .withColumn("user_id", DataTypes.UUID)
                .withColumn("revision_status", DataTypes.TEXT)
                .withColumn("revision_number", DataTypes.BIGINT)
                .withClusteringOrder("resource_name", ClusteringOrder.ASC)
                .withClusteringOrder("id", ClusteringOrder.DESC)
                .build()
                .setTimeout(ofSeconds(10)));
    }
}
