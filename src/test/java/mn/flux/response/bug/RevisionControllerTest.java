package mn.flux.response.bug;

import static io.restassured.RestAssured.given;
import static java.util.Objects.isNull;
import static mn.flux.response.bug.RevisionController.REVISION_BASE_URI;
import static org.junit.jupiter.api.Assertions.fail;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Ulimit;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import mn.flux.response.bug.model.PublishStatus;
import mn.flux.response.bug.model.ResourceName;
import mn.flux.response.bug.model.Revision;
import mn.flux.response.bug.repository.RevisionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RevisionControllerTest implements TestPropertyProvider {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    RevisionDao revisionDao;

    @Inject
    RevisionMapper mapper;

    @Inject
    ObjectMapper objectMapper;

    @Test
    void getRevisions_bug_JsonEOFException() {
        // given
        Revision revision = Revision.builder()
            .revisionNumber(ThreadLocalRandom.current().nextLong())
            .id(UUIDs.timeBased())
            .resourceName(ResourceName.ARTICLE)
            .resourceId(UUID.randomUUID().toString())
            .revisionStatus(PublishStatus.DRAFT)
            .revisionNumber(1L)
            .comment("no comment")
            .build();
        Mono.from(revisionDao.save(revision)).block();

        String uri = String.format("/%s/%s", revision.getResourceName().getJsonValue(), revision.getResourceId());

        // usually fails somewhere around 1500-3000 loops.
        IntStream.range(0, 30000)
            .forEach(i -> {
                try {
                    System.out.println(i);

                    // when
                    Response response = given()
                        .when()
                        .get(uri)
                        .then()
                        .statusCode(HttpStatus.OK.getCode())
                        .and()
                        .extract().response();

                    // then
                    objectMapper.readValue(response.getBody().asString(), new TypeReference<List<RevisionDto>>() {
                    });
                } catch (JsonProcessingException e) {
                    fail(e.getMessage(), e);
                }
            });
    }

    @BeforeEach
    void setup() {
        RestAssured.config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.socket.timeout", 30000)
                .setParam("http.connection.timeout", 30000))
            .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        RestAssured.requestSpecification = new RequestSpecBuilder()
            .setBasePath(REVISION_BASE_URI)
            .setPort(embeddedServer.getPort())
            .setContentType(ContentType.JSON)
            .build();
    }

    private static final String CASSANDRA_DOCKER_IMAGE = "cassandra:3.11.2";
    private static final String JVM_EXTRA_OPTS = "-Xms512M -Xmx512M -Xmn128M -Dcassandra.skip_wait_for_gossip_to_settle=0 -Dcassandra.load_ring_state=false";

    protected static CassandraContainer<?> CASSANDRA_CONTAINER;

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        if (isNull(CASSANDRA_CONTAINER)) {
            CASSANDRA_CONTAINER = new CassandraContainer<>(DockerImageName.parse(CASSANDRA_DOCKER_IMAGE));

            CASSANDRA_CONTAINER.withCreateContainerCmdModifier(cmd ->
                Objects.requireNonNull(cmd.getHostConfig())
                    .withUlimits(List.of(new Ulimit("nofile", 65535L, 65535L))));

            CASSANDRA_CONTAINER.withEnv("JVM_EXTRA_OPTS", JVM_EXTRA_OPTS);
            CASSANDRA_CONTAINER.withReuse(true);
            CASSANDRA_CONTAINER.start();
        }

        return Map.of(
            "cdb.host", CASSANDRA_CONTAINER.getContainerIpAddress(),
            "cdb.port", CASSANDRA_CONTAINER.getMappedPort(CassandraContainer.CQL_PORT).toString(),
            "cdb.datacenter", "datacenter1"
        );
    }
}
