package mn.flux.response.bug;

import mn.flux.response.bug.model.Revision;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "jsr330")
public abstract class RevisionMapper {
    protected abstract RevisionDto toDto(Revision revision);

    public Flux<RevisionDto> toDto(Flux<Revision> revisions) {
        return revisions.flatMap(revision -> Mono.just(toDto(revision)));
    }
}
