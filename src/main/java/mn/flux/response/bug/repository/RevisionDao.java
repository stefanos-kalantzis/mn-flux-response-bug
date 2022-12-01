package mn.flux.response.bug.repository;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import mn.flux.response.bug.model.ResourceName;
import mn.flux.response.bug.model.Revision;

@Dao
public interface RevisionDao {

    @Select(orderBy = {"resource_name ASC", "id DESC"}, limit = ":maxResults")
    MappedReactiveResultSet<Revision> findAllBy(String resourceId, ResourceName resourceName);

    @Insert
    ReactiveResultSet save(Revision revision);
}
