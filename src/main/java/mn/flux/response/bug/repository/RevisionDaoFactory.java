package mn.flux.response.bug.repository;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface RevisionDaoFactory {

    @DaoFactory
    RevisionDao createRevisionDao(@DaoKeyspace CqlIdentifier keyspace);
}
