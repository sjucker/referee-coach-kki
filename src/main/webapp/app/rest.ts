/* eslint-disable */

export interface BasketplanGameDTO {
    gameNumber: string;
    competition: string;
    date: Date;
    result: string;
    teamA: string;
    teamB: string;
    officiatingMode: OfficiatingMode;
    referee1?: RefereeDTO;
    referee2?: RefereeDTO;
    referee3?: RefereeDTO;
    youtubeId?: string;
}

export interface ChangePasswordRequestDTO {
    oldPassword: string;
    newPassword: string;
}

export interface CommentReplyDTO {
    commentId: number;
    comment: string;
}

export interface CopyVideoReportDTO {
    sourceId: string;
    reportee: Reportee;
}

export interface CreateRepliesDTO {
    replies: CommentReplyDTO[];
}

export interface CreateVideoReportDTO {
    federation: Federation;
    gameNumber: string;
    youtubeId: string;
    reportee: Reportee;
}

export interface CriteriaEvaluationDTO {
    comment?: string;
    score?: number;
    rating?: string;
}

export interface LoginRequestDTO {
    email: string;
    password: string;
}

export interface LoginResponseDTO {
    id: number;
    name: string;
    admin: boolean;
    jwt: string;
}

export interface RefereeDTO {
    id: number;
    name: string;
}

export interface ReporterDTO {
    id: number;
    name: string;
}

export interface UserDTO {
    email?: string;
    admin: boolean;
}

export interface VideoCommentDTO {
    id?: number;
    timestamp: number;
    comment: string;
    replies: VideoCommentReplyDTO[];
}

export interface VideoCommentReplyDTO {
    id: number;
    repliedBy: string;
    repliedAt: Date;
    reply: string;
}

export interface VideoReportDTO {
    id: string;
    basketplanGame: BasketplanGameDTO;
    reporter: ReporterDTO;
    reportee: Reportee;
    overallScore: number;
    overallRating: string;
    generalComment?: string;
    image: CriteriaEvaluationDTO;
    fitness: CriteriaEvaluationDTO;
    mechanics: CriteriaEvaluationDTO;
    fouls: CriteriaEvaluationDTO;
    violations: CriteriaEvaluationDTO;
    gameManagement: CriteriaEvaluationDTO;
    pointsToKeepComment?: string;
    pointsToImproveComment?: string;
    videoComments: VideoCommentDTO[];
    finished: boolean;
    version: number;
}

export interface VideoReportDiscussionDTO {
    videoReportId: string;
    basketplanGame: BasketplanGameDTO;
    reporter: ReporterDTO;
    referee: string;
    videoComments: VideoCommentDTO[];
}

export enum OfficiatingMode {
    OFFICIATING_2PO = "OFFICIATING_2PO",
    OFFICIATING_3PO = "OFFICIATING_3PO",
}

export enum Reportee {
    FIRST_REFEREE = "FIRST_REFEREE",
    SECOND_REFEREE = "SECOND_REFEREE",
    THIRD_REFEREE = "THIRD_REFEREE",
}

export enum Federation {
    SBL = "SBL",
    PROBASKET = "PROBASKET",
}
