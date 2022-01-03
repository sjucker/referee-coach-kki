import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ChangePasswordRequestDTO, LoginRequestDTO, LoginResponseDTO} from "../rest";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    private readonly token = 'token';
    private readonly userId = 'user-id';
    private readonly admin = 'admin';

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
        return this.httpClient.post<any>(`${this.baseUrl}/authenticate/change-password`, request);
    }

    setCredentials(dto: LoginResponseDTO): void {
        sessionStorage.setItem(this.token, dto.jwt);
        sessionStorage.setItem(this.userId, String(dto.id));
        if (dto.admin) {
            sessionStorage.setItem(this.admin, "1");
        } else {
            sessionStorage.removeItem(this.admin);
        }
    }

    getAuthorizationToken(): string | null {
        return sessionStorage.getItem(this.token);
    }

    getUserId(): number | null {
        const userId = sessionStorage.getItem(this.userId);
        if (userId) {
            return parseInt(userId);
        }
        return null;
    }

    isLoggedIn(): boolean {
        return sessionStorage.getItem(this.token) !== null;
    }

    isAdmin(): boolean {
        return this.isLoggedIn() && sessionStorage.getItem(this.admin) !== null;
    }

}
