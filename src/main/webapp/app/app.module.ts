import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {YouTubePlayerModule} from "@angular/youtube-player";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatCardModule} from "@angular/material/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MatGridListModule} from "@angular/material/grid-list";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatIconModule} from "@angular/material/icon";
import {VideoReportComponent} from './video-report/video-report.component';
import {ViewVideoReportComponent} from './view-report/view-video-report.component';
import {MatRadioModule} from "@angular/material/radio";
import {MainComponent} from "./main/main.component";
import {MatTableModule} from "@angular/material/table";
import {MatSelectModule} from "@angular/material/select";
import {LoginComponent} from './login/login.component';
import {AuthenticationInterceptor} from "./interceptors/authentication-interceptor.service";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { ChangePasswordComponent } from './change-password/change-password.component';
import {MatSidenavModule} from "@angular/material/sidenav";

@NgModule({
    declarations: [
        AppComponent,
        MainComponent,
        VideoReportComponent,
        ViewVideoReportComponent,
        LoginComponent,
        ChangePasswordComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        YouTubePlayerModule,
        MatButtonModule,
        MatFormFieldModule,
        MatToolbarModule,
        MatInputModule,
        MatCardModule,
        FormsModule,
        HttpClientModule,
        MatGridListModule,
        FlexLayoutModule,
        MatIconModule,
        MatRadioModule,
        MatTableModule,
        MatSelectModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatSidenavModule,
    ],
    providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthenticationInterceptor, multi: true}],
    bootstrap: [AppComponent]
})
export class AppModule {
}
