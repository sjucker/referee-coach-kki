import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {AuthenticationService} from "../service/authentication.service";
import {getReferee} from "../service/video-report.service";
import {VideoReportDTO} from "../rest";

@Component({
    selector: 'app-video-report-reply-dialog',
    templateUrl: './video-report-reply-dialog.component.html',
    styleUrls: ['./video-report-reply-dialog.component.css']
})
export class VideoReportReplyDialogComponent implements OnInit {

    reply: String = '';

    constructor(@Inject(MAT_DIALOG_DATA) public data: VideoReportDTO,
                private authenticationService: AuthenticationService) {
    }

    ngOnInit(): void {
    }

    getReferee(): string {
        return getReferee(this.data);
    }

    isLoggedIn(): boolean {
        return this.authenticationService.isLoggedIn();
    }

}
