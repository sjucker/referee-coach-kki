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

        // TODO map this
        ExpertiseDTO newDto = new ExpertiseDTO(
                uuid,
                dto.basketplanGame(),
                dto.reportee(),
                dto.imageComment(),
                dto.mechanicsComment(),
                dto.foulsComment(),
                dto.gameManagementComment(),
                dto.pointsToKeepComment(),
                dto.pointsToImproveComment(),
                dto.videoComments()
        );
        expertisen.put(uuid, newDto);

        return newDto;
    }

    public ExpertiseDTO update(String id, ExpertiseDTO dto) {
        expertisen.put(id, dto);
        return dto;
    }

    public Optional<ExpertiseDTO> find(String id) {
        return Optional.ofNullable(expertisen.get(id));
    }

}
