import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ExpertiseDTO} from "./rest";
import {environment} from "../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ExpertiseService {

    private baseUrl = environment.baseUrl;

    constructor(private readonly httpClient: HttpClient) {
    }

    getExpertise(id: string): Observable<ExpertiseDTO> {
        return this.httpClient.get<ExpertiseDTO>(`${this.baseUrl}/expertise/${id}`);
    }

    saveExpertise(dto: ExpertiseDTO): Observable<ExpertiseDTO> {
        if (dto.id) {
            return this.httpClient.patch<ExpertiseDTO>(`${this.baseUrl}/expertise/${dto.id}`, dto);
        } else {
            return this.httpClient.post<ExpertiseDTO>(`${this.baseUrl}/expertise`, dto);
        }
    }

}
