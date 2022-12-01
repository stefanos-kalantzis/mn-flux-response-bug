package mn.flux.response.bug;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.micronaut.core.annotation.Introspected;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mn.flux.response.bug.model.PublishStatus;
import mn.flux.response.bug.model.ResourceName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class RevisionDto {

    @NotBlank
    private String resourceId;

    @JsonInclude(Include.NON_NULL)
    private UUID id;

    @NotNull
    private ResourceName resourceName;

    private Instant date;

    private String comment;

    private PublishStatus revisionStatus;

    private Long revisionNumber;
}
