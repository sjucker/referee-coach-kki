package ch.stefanjucker.refereecoach.mapper;

import ch.stefanjucker.refereecoach.domain.BasketplanGame;
import ch.stefanjucker.refereecoach.domain.Coach;
import ch.stefanjucker.refereecoach.domain.CriteriaEvaluation;
import ch.stefanjucker.refereecoach.domain.Referee;
import ch.stefanjucker.refereecoach.domain.Tags;
import ch.stefanjucker.refereecoach.domain.VideoComment;
import ch.stefanjucker.refereecoach.domain.VideoCommentReply;
import ch.stefanjucker.refereecoach.domain.VideoReport;
import ch.stefanjucker.refereecoach.dto.BasketplanGameDTO;
import ch.stefanjucker.refereecoach.dto.CoachDTO;
import ch.stefanjucker.refereecoach.dto.RefereeDTO;
import ch.stefanjucker.refereecoach.dto.Reportee;
import ch.stefanjucker.refereecoach.dto.TagDTO;
import ch.stefanjucker.refereecoach.dto.VideoCommentDTO;
import ch.stefanjucker.refereecoach.dto.VideoCommentReplyDTO;
import ch.stefanjucker.refereecoach.dto.VideoReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    RefereeDTO toDTO(Referee referee);

    CoachDTO toDTO(Coach coach);

    default VideoReportDTO toDTO(VideoReport videoReport) {
        return toDTO(videoReport, List.of(), List.of());
    }

    @Mapping(target = "videoComments", source = "videoCommentDTOs")
    @Mapping(target = "otherReportees", source = "otherReportees")
    VideoReportDTO toDTO(VideoReport videoReport, List<VideoCommentDTO> videoCommentDTOs, List<Reportee> otherReportees);

    default VideoCommentDTO toDTO(VideoComment videoComment) {
        return toDTO(videoComment, List.of());
    }

    @Mapping(target = "replies", source = "replyDTOs")
    VideoCommentDTO toDTO(VideoComment videoComment, List<VideoCommentReplyDTO> replyDTOs);

    VideoComment fromDTO(VideoCommentDTO dto, String videoReportId);

    VideoCommentReplyDTO toDTO(VideoCommentReply videoCommentReply);

    List<VideoCommentReplyDTO> toDTO(List<VideoCommentReply> videoCommentReply);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coach", ignore = true)
    @Mapping(target = "reportee", ignore = true)
    @Mapping(target = "finished", ignore = true)
    VideoReport copy(VideoReport videoReport);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "videoReportId", ignore = true)
    VideoComment copy(VideoComment videoComment);

    BasketplanGameDTO toDTO(BasketplanGame basketplanGame);

    BasketplanGame fromDTO(BasketplanGameDTO dto);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "level", ignore = true)
    Referee fromDTO(RefereeDTO refereeDTO);

    TagDTO toDTO(Tags tags);

    Set<TagDTO> toDTO(Set<Tags> tags);

    default void update(VideoReportDTO dto, VideoReport videoReport) {
        videoReport.setGeneral(new CriteriaEvaluation(dto.general().comment(), dto.general().score()));

        videoReport.setImage(new CriteriaEvaluation(dto.image().comment(), dto.image().score()));
        videoReport.setFitness(new CriteriaEvaluation(dto.fitness().comment(), dto.fitness().score()));
        videoReport.setMechanics(new CriteriaEvaluation(dto.mechanics().comment(), dto.mechanics().score()));
        videoReport.setFouls(new CriteriaEvaluation(dto.fouls().comment(), dto.fouls().score()));
        videoReport.setViolations(new CriteriaEvaluation(dto.violations().comment(), dto.violations().score()));
        videoReport.setGameManagement(new CriteriaEvaluation(dto.gameManagement().comment(), dto.gameManagement().score()));

        videoReport.setPointsToKeepComment(dto.pointsToKeepComment());
        videoReport.setPointsToImproveComment(dto.pointsToImproveComment());

        videoReport.setFinished(dto.finished());
    }

}
