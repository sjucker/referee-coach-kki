package ch.stefanjucker.videoexpertise.service.expertise;

import ch.stefanjucker.videoexpertise.dto.basketplan.ExpertiseDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpertiseService {

    // TODO move this to database

    private final Map<String, ExpertiseDTO> expertisen = new HashMap<>();

    public ExpertiseDTO save(ExpertiseDTO dto) {
        String uuid = UUID.randomUUID().toString();
        expertisen.put(uuid, dto);

        // TODO map this
        return new ExpertiseDTO(
                uuid,
                dto.imageComment(),
                dto.mechanicsComment(),
                dto.foulsComment(),
                dto.gameManagementComment(),
                dto.pointsToKeepComment(),
                dto.pointsToImproveComment(),
                dto.videoComments()
        );
    }

    public ExpertiseDTO update(String id, ExpertiseDTO dto) {
        return expertisen.put(id, dto);
    }

    public Optional<ExpertiseDTO> find(String id) {
        return Optional.ofNullable(expertisen.get(id));
    }

}
