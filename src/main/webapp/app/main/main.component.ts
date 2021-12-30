import {Component, OnInit} from '@angular/core';
import {OfficiatingMode, Reportee, VideoReportDTO} from "../rest";
import {VideoReportService} from "../service/video-report.service";
import {BasketplanService} from "../service/basketplan.service";
import {Router} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";

interface ReporteeSelection {
    reportee: Reportee,
    name: string
}

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
    displayedColumns: string[] = ['date', 'gameNumber', 'competition', 'teams', 'coach', 'reportee', 'edit', 'view'];
    gameNumber: string = '21-03520';

    // whether we are ready to start with video-report
    ready = false;
    problemDescription = '';

    videoReportDtos: MatTableDataSource<VideoReportDTO> = new MatTableDataSource<VideoReportDTO>([]);

    reportee?: Reportee
    reportees: ReporteeSelection[] = []

    constructor(private readonly basketplanService: BasketplanService,
                private readonly videoReportService: VideoReportService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.videoReportService.getAllVideoReports().subscribe(value => {
            this.videoReportDtos = new MatTableDataSource<VideoReportDTO>(value);
            this.videoReportDtos.filterPredicate = (data, filter) => {
                // default filter cannot handle nested objects, so handle each column specifically
                return data.basketplanGame.gameNumber.toLowerCase().indexOf(filter) != -1
                    || data.basketplanGame.competition.toLowerCase().indexOf(filter) != -1
                    || data.basketplanGame.teamA.toLowerCase().indexOf(filter) != -1
                    || data.basketplanGame.teamB.toLowerCase().indexOf(filter) != -1
                    || data.basketplanGame.teamB.toLowerCase().indexOf(filter) != -1
                    || data.reporter.name.toLowerCase().indexOf(filter) != -1
                    || this.getReportee(data).toLowerCase().indexOf(filter) != -1;
            }
        });
    }

    searchGame(): void {
        this.basketplanService.searchGame(this.gameNumber).subscribe(dto => {
            if (dto.referee1 && dto.referee2
                && (dto.officiatingMode === OfficiatingMode.OFFICIATING_2PO || dto.referee3)) {
                if (dto.youtubeId) {
                    this.ready = true;
                    this.problemDescription = '';
                    if (dto.officiatingMode === OfficiatingMode.OFFICIATING_2PO) {
                        this.reportees = [
                            {reportee: Reportee.FIRST_REFEREE, name: dto.referee1.name},
                            {reportee: Reportee.SECOND_REFEREE, name: dto.referee2.name}
                        ];
                    } else {
                        this.reportees = [
                            {reportee: Reportee.FIRST_REFEREE, name: dto.referee1.name},
                            {reportee: Reportee.SECOND_REFEREE, name: dto.referee2.name},
                            {reportee: Reportee.THIRD_REFEREE, name: dto.referee3!.name} // TODO solve this better
                        ];
                    }
                } else {
                    this.ready = false;
                    this.problemDescription = 'No YouTube video available'
                }
            } else {
                this.ready = false;
                this.problemDescription = 'At least one referee not available in database';
            }
        });
    }

    createVideoReport() {
        if (this.reportee) {
            this.videoReportService.createVideoReport(this.gameNumber, this.reportee).subscribe(response => {
                this.router.navigate(['/edit/' + response.id]);
            })
        } else {
            // TODO proper error handling
        }
    }

    getReportee(report: VideoReportDTO): string {
        switch (report.reportee) {
            case Reportee.FIRST_REFEREE:
                return report.basketplanGame.referee1!.name;
            case Reportee.SECOND_REFEREE:
                return report.basketplanGame.referee2!.name;
            case Reportee.THIRD_REFEREE:
                return report.basketplanGame.referee3!.name;
        }
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.videoReportDtos.filter = filterValue.trim().toLowerCase();
    }

}
