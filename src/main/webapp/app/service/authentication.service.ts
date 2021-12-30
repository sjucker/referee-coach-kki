import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ChangePasswordRequestDTO, LoginRequestDTO, LoginResponseDTO} from "../rest";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    private baseUrl = environment.baseUrl;

    constructor(private readonly httpClient: HttpClient) {
    }

    login(email: string, password: string): Observable<LoginResponseDTO> {
        const request: LoginRequestDTO = {
            email: email,
            password: password
        };

        return this.httpClient.post<LoginResponseDTO>(`${this.baseUrl}/authenticate`, request);
    }

    changePassword(oldPassword: string, newPassword: string): Observable<any> {
        const request: ChangePasswordRequestDTO = {
            oldPassword: oldPassword,
            newPassword: newPassword
        }
        return this.httpClient.post(`${this.baseUrl}/authenticate/change-password`, request);
    }

    setAuthorizationToken(token: string): void {
        sessionStorage.setItem('token', token);
    }

    getAuthorizationToken(): string | null {
        return sessionStorage.getItem('token');
    }

    isLoggedIn(): boolean {
        return sessionStorage.getItem('token') !== null;
    }

}
