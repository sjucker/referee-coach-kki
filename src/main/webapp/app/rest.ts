/* eslint-disable */

export interface BasketplanGameDTO {
    competition: string;
    date: Date;
    result: string;
    teamA: string;
    teamB: string;
    officiatingMode: OfficiatingMode;
    referee1: string;
    referee2: string;
    referee3?: string;
    youtubeId?: string;
}

export interface ExpertiseDTO {
    id?: string;
    basketplanGame: BasketplanGameDTO;
    reportee: Reportee;
    imageComment: string;
    mechanicsComment: string;
    foulsComment: string;
    gameManagementComment: string;
    pointsToKeepComment: string;
    pointsToImproveComment: string;
    videoComments: VideoCommentDTO[];
}

export interface VideoCommentDTO {
    timestamp: number;
    comment: string;
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
