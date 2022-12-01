package mn.flux.response.bug.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Revision {

    @PartitionKey
    private String resourceId;

    @ClusteringColumn(1)
    private ResourceName resourceName;

    @ClusteringColumn(2)
    private UUID id;

    private UUID userId;

    private Instant date;

    private String comment;

    private String revisionData;

    private PublishStatus revisionStatus;

    private Long revisionNumber;
}
