import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CreateVideoReportDTO, Federation, Reportee, VideoReportDTO} from "../rest";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ExpertiseService {

    private baseUrl = environment.baseUrl;

    constructor(private readonly httpClient: HttpClient) {
    }

    getExpertise(id: string): Observable<VideoReportDTO> {
        return this.httpClient.get<VideoReportDTO>(`${this.baseUrl}/video-expertise/${id}`);
    }

    getAllExpertise(): Observable<VideoReportDTO[]> {
        return this.httpClient.get<VideoReportDTO[]>(`${this.baseUrl}/video-expertise`);
    }

    createExpertise(gameNumber: string, reportee: Reportee) {
        const request: CreateVideoReportDTO = {
            gameNumber: gameNumber,
            reportee: reportee,
            federation: Federation.SBL
        };
        return this.httpClient.post<VideoReportDTO>(`${this.baseUrl}/video-expertise`, request);
    }

    saveExpertise(dto: VideoReportDTO): Observable<VideoReportDTO> {

        return this.httpClient.put<VideoReportDTO>(`${this.baseUrl}/video-expertise/${dto.id}`, dto);

    }

}
