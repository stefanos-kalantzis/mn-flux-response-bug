package mn.flux.response.bug;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import mn.flux.response.bug.model.ResourceName;
import reactor.core.publisher.Flux;

@Controller(RevisionController.REVISION_BASE_URI)
@RequiredArgsConstructor
public class RevisionController {
    public static final String REVISION_BASE_URI = "/revision";

    private final RevisionService revisionService;

    @Get("/{resourceName}/{resourceId}")
    public Flux<RevisionDto> getAll(@PathVariable ResourceName resourceName, @PathVariable String resourceId) {
        return revisionService.findAllBy(resourceId, resourceName);
    }
}
