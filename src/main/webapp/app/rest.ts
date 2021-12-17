/* eslint-disable */

export interface BasketplanGameDTO {
    competition: string;
    date: Date;
    result: string;
    teamA: string;
    teamB: string;
    officiatingMode: OfficiatingMode;
    referee1?: string;
    referee2?: string;
    referee3?: string;
    youtubeId?: string;
}

export interface CreateVideoExpertiseDTO {
    federation?: Federation;
    gameNumber?: string;
    reportee?: Reportee;
}

export interface VideoCommentDTO {
    timestamp: number;
    comment: string;
}

export interface VideoExpertiseDTO {
    id: string;
    basketplanGame: BasketplanGameDTO;
    reportee: Reportee;
    imageComment: string;
    mechanicsComment: string;
    foulsComment: string;
    gameManagementComment: string;
    pointsToKeepComment: string;
    pointsToImproveComment: string;
    videoComments: VideoCommentDTO[];
    finished: boolean;
}

export enum OfficiatingMode {
    OFFICIATING_2PO = "OFFICIATING_2PO",
    OFFICIATING_3PO = "OFFICIATING_3PO",
}

export enum Federation {
    SBL = "SBL",
    PROBASKET = "PROBASKET",
}

export enum Reportee {
    FIRST_REFEREE = "FIRST_REFEREE",
    SECOND_REFEREE = "SECOND_REFEREE",
    THIRD_REFEREE = "THIRD_REFEREE",
}
