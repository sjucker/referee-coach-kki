import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CopyVideoReportDTO, CreateVideoReportDTO, Federation, Reportee, VideoReportDTO} from "../rest";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class VideoReportService {

    private baseUrl = environment.baseUrl;

    constructor(private readonly httpClient: HttpClient) {
    }

    getVideoReport(id: string): Observable<VideoReportDTO> {
        return this.httpClient.get<VideoReportDTO>(`${this.baseUrl}/video-report/${id}`);
    }

    getAllVideoReports(): Observable<VideoReportDTO[]> {
        return this.httpClient.get<VideoReportDTO[]>(`${this.baseUrl}/video-report`);
    }

    createVideoReport(gameNumber: string, reportee: Reportee): Observable<VideoReportDTO> {
        const request: CreateVideoReportDTO = {
            gameNumber: gameNumber,
            reportee: reportee,
            federation: Federation.SBL
        };
        return this.httpClient.post<VideoReportDTO>(`${this.baseUrl}/video-report`, request);
    }

    copyVideoReport(sourceId: string, reportee: Reportee) {
        const request: CopyVideoReportDTO = {
            sourceId: sourceId,
            reportee: reportee,
        };
        return this.httpClient.post<VideoReportDTO>(`${this.baseUrl}/video-report/copy`, request)
    }

    saveVideoReport(dto: VideoReportDTO): Observable<VideoReportDTO> {
        return this.httpClient.put<VideoReportDTO>(`${this.baseUrl}/video-report/${dto.id}`, dto);
    }

}
