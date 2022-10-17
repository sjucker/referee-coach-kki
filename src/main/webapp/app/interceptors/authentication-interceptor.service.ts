import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {AuthenticationService} from "../service/authentication.service";
import {Router} from "@angular/router";
import {LOGIN_PATH} from "../app-routing.module";

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

    constructor(private authenticationService: AuthenticationService, private router: Router) {
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const token = this.authenticationService.getAuthorizationToken();
        if (token) {
            return next.handle(request.clone({
                setHeaders: {Authorization: 'Bearer ' + token}
            }));
        }

        // return next.handle(request).pipe(catchError(error => this.handleAuthError(error))); TODO
        return next.handle(request);
    }

    private handleAuthError(err: HttpErrorResponse): Observable<any> {
        if (err.status === 401 || err.status === 403) {
            this.router.navigate([LOGIN_PATH]);
            return of(err.message);
        }
        return throwError(() => err);
    }


}
