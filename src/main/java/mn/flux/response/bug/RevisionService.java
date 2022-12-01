package mn.flux.response.bug;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import mn.flux.response.bug.model.ResourceName;
import mn.flux.response.bug.repository.RevisionDao;
import reactor.core.publisher.Flux;

@Singleton
@RequiredArgsConstructor
public class RevisionService {

    private final RevisionMapper revisionMapper;
    private final RevisionDao revisionDao;

    public Flux<RevisionDto> findAllBy(String resourceId, ResourceName resourceName) {
        return Flux.from(revisionDao.findAllBy(resourceId, resourceName))
            .transform(revisionMapper::toDto);
    }
}
