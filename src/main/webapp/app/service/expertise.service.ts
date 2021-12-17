import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CreateVideoExpertiseDTO, Federation, Reportee, VideoExpertiseDTO} from "../rest";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ExpertiseService {

    private baseUrl = environment.baseUrl;

    constructor(private readonly httpClient: HttpClient) {
    }

    getExpertise(id: string): Observable<VideoExpertiseDTO> {
        return this.httpClient.get<VideoExpertiseDTO>(`${this.baseUrl}/video-expertise/${id}`);
    }

    getAllExpertise(): Observable<VideoExpertiseDTO[]> {
        return this.httpClient.get<VideoExpertiseDTO[]>(`${this.baseUrl}/video-expertise`);
    }

    createExpertise(gameNumber: string, reportee: Reportee) {
        const request: CreateVideoExpertiseDTO = {
            gameNumber: gameNumber,
            reportee: reportee,
            federation: Federation.SBL
        };
        return this.httpClient.post<VideoExpertiseDTO>(`${this.baseUrl}/video-expertise`, request);
    }

    saveExpertise(dto: VideoExpertiseDTO): Observable<VideoExpertiseDTO> {

        return this.httpClient.put<VideoExpertiseDTO>(`${this.baseUrl}/video-expertise/${dto.id}`, dto);

    }

}
