import {Component, Inject, OnInit} from '@angular/core';
import {Reportee, VideoReportDTO} from "../rest";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

interface ReporteeSelection {
    reportee: Reportee,
    name: string
}

@Component({
    selector: 'app-video-report-copy-dialog',
    templateUrl: './video-report-copy-dialog.component.html',
    styleUrls: ['./video-report-copy-dialog.component.css']
})
export class VideoReportCopyDialogComponent implements OnInit {

    reportee?: Reportee
    reportees: ReporteeSelection[] = []

    constructor(@Inject(MAT_DIALOG_DATA) public dto: VideoReportDTO) {
    }

    ngOnInit(): void {
        if (this.dto.reportee !== Reportee.FIRST_REFEREE && this.dto.basketplanGame.referee1) {
            this.reportees = [...this.reportees, {
                reportee: Reportee.FIRST_REFEREE,
                name: this.dto.basketplanGame.referee1.name
            }];
            this.reportee = Reportee.FIRST_REFEREE;
        }

        if (this.dto.reportee !== Reportee.SECOND_REFEREE && this.dto.basketplanGame.referee2) {
            this.reportees = [...this.reportees, {
                reportee: Reportee.SECOND_REFEREE,
                name: this.dto.basketplanGame.referee2.name
            }];
            if (!this.reportee) {
                this.reportee = Reportee.SECOND_REFEREE;
            }
        }

        if (this.dto.reportee !== Reportee.THIRD_REFEREE && this.dto.basketplanGame.referee3) {
            this.reportees = [...this.reportees, {
                reportee: Reportee.THIRD_REFEREE,
                name: this.dto.basketplanGame.referee3.name
            }];
            if (!this.reportee) {
                this.reportee = Reportee.THIRD_REFEREE;
            }
        }
    }

}
