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
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MatGridListModule} from "@angular/material/grid-list";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatIconModule} from "@angular/material/icon";
import {ExpertiseComponent} from './expertise/expertise.component';
import {ViewExpertiseComponent} from './view-expertise/view-expertise.component';
import {MatRadioModule} from "@angular/material/radio";
import {MainComponent} from "./main/main.component";
import {MatTableModule} from "@angular/material/table";
import {MatSelectModule} from "@angular/material/select";

@NgModule({
    declarations: [
        AppComponent,
        MainComponent,
        ExpertiseComponent,
        ViewExpertiseComponent
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
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
