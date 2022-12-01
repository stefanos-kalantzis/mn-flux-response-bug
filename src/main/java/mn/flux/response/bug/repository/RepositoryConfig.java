package mn.flux.response.bug.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.codec.ExtraTypeCodecs;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requirements;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import mn.flux.response.bug.model.PublishStatus;
import mn.flux.response.bug.model.ResourceName;

@Factory
@Requirements(@Requires(beans = SchemaBootstrap.class))
public class RepositoryConfig {

    @Singleton
    @SuppressWarnings({"unused"})
    public RevisionDao revisionDao(CqlSession cqlSession) {
        MutableCodecRegistry registry = (MutableCodecRegistry) cqlSession.getContext().getCodecRegistry();
        registry.register(ExtraTypeCodecs.enumNamesOf(ResourceName.class));
        registry.register(ExtraTypeCodecs.enumNamesOf(PublishStatus.class));

        RevisionDaoFactory revisionDaoFactory = new RevisionDaoFactoryBuilder(cqlSession).build();
        return revisionDaoFactory.createRevisionDao(CqlIdentifier.fromCql(SchemaBootstrap.KEYSPACE_NAME));
    }
}
