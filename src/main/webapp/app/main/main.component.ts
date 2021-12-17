import {Component, OnInit} from '@angular/core';
import {OfficiatingMode, Reportee, VideoExpertiseDTO} from "../rest";
import {ExpertiseService} from "../service/expertise.service";
import {BasketplanService} from "../service/basketplan.service";
import {Router} from "@angular/router";

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
    displayedColumns: string[] = ['date', 'link'];
    gameNumber: string = '21-03520';

    // whether we are ready to start with expertise
    ready = false;
    problemDescription = '';

    videoReportDtos: VideoExpertiseDTO[] = [];

    reportee?: Reportee
    reportees: ReporteeSelection[] = []

    constructor(private readonly basketplanService: BasketplanService,
                private readonly expertiseService: ExpertiseService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.expertiseService.getAllExpertise().subscribe(value => {
            this.videoReportDtos = value;
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
                            {reportee: Reportee.FIRST_REFEREE, name: dto.referee1},
                            {reportee: Reportee.SECOND_REFEREE, name: dto.referee2}
                        ];
                    } else {
                        this.reportees = [
                            {reportee: Reportee.FIRST_REFEREE, name: dto.referee1},
                            {reportee: Reportee.SECOND_REFEREE, name: dto.referee2},
                            {reportee: Reportee.THIRD_REFEREE, name: dto.referee3!} // TODO solve this better
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

    createVideoExpertise() {
        if (this.reportee) {
            this.expertiseService.createExpertise(this.gameNumber, this.reportee).subscribe(response => {
                this.router.navigate(['/edit/' + response.id]);
            })
        } else {
            // TODO proper error handling
        }
    }

}
