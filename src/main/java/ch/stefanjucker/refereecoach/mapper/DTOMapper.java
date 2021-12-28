package ch.stefanjucker.refereecoach.mapper;

import ch.stefanjucker.refereecoach.domain.BasketplanGame;
import ch.stefanjucker.refereecoach.domain.Referee;
import ch.stefanjucker.refereecoach.domain.User;
import ch.stefanjucker.refereecoach.domain.VideoComment;
import ch.stefanjucker.refereecoach.domain.VideoReport;
import ch.stefanjucker.refereecoach.dto.BasketplanGameDTO;
import ch.stefanjucker.refereecoach.dto.RefereeDTO;
import ch.stefanjucker.refereecoach.dto.ReporterDTO;
import ch.stefanjucker.refereecoach.dto.VideoCommentDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

import static java.util.stream.Collectors.toCollection;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    RefereeDTO toDTO(Referee referee);

    ReporterDTO toDTO(User user);

    VideoReportDTO toDTO(VideoReport videoReport);

    BasketplanGameDTO toDTO(BasketplanGame basketplanGame);

    BasketplanGame fromDTO(BasketplanGameDTO dto);

    VideoComment fromDTO(VideoCommentDTO dto);

    default void update(VideoReportDTO dto, VideoReport videoReport) {
        videoReport.setImageComment(dto.imageComment());
        videoReport.setMechanicsComment(dto.mechanicsComment());
        videoReport.setFoulsComment(dto.foulsComment());
        videoReport.setGameManagementComment(dto.gameManagementComment());
        videoReport.setPointsToKeepComment(dto.pointsToKeepComment());
        videoReport.setPointsToImproveComment(dto.pointsToImproveComment());

        videoReport.setVideoComments(dto.videoComments()
                                        .stream()
                                        .map(this::fromDTO)
                                        .collect(toCollection(ArrayList::new)));

        videoReport.setFinished(dto.finished());
    }

}
