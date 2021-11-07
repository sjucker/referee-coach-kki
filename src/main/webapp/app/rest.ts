/* eslint-disable */

export interface BasketplanGameDTO {
    competition: string;
    date: Date;
    result: string;
    teamA: string;
    teamB: string;
    referee1: string;
    referee2: string;
    referee3?: string;
    youtubeId?: string;
}

export interface ExpertiseDTO {
    id?: string;
    basketplanGame: BasketplanGameDTO;
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
