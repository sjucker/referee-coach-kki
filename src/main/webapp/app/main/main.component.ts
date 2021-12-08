import {Component, OnInit} from '@angular/core';
import {ExpertiseDTO, OfficiatingMode} from "../rest";
import {ExpertiseService} from "../service/expertise.service";
import {BasketplanService} from "../service/basketplan.service";

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

    videoReportDtos: ExpertiseDTO[] = [];

    constructor(private readonly basketplanService: BasketplanService,
                private readonly expertiseService: ExpertiseService) {
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


}
