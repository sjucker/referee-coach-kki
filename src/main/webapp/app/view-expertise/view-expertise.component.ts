import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ExpertiseService} from "../service/expertise.service";
import {YouTubePlayer} from "@angular/youtube-player";
import {OfficiatingMode, Reportee, VideoExpertiseDTO} from "../rest";

@Component({
    selector: 'app-view-expertise',
    templateUrl: './view-expertise.component.html',
    styleUrls: ['./view-expertise.component.css']
})
export class ViewExpertiseComponent implements OnInit {

    @ViewChild('youtubePlayer') youtube?: YouTubePlayer;

    dto?: VideoExpertiseDTO;

    constructor(private route: ActivatedRoute,
                private readonly expertiseService: ExpertiseService) {
    }

    ngOnInit(): void {
        // This code loads the IFrame Player API code asynchronously, according to the instructions at
        // https://developers.google.com/youtube/iframe_api_reference#Getting_Started
        const tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        document.body.appendChild(tag);

        this.expertiseService.getExpertise(this.route.snapshot.paramMap.get('id')!).subscribe(result => {
            this.dto = result;
        });
    }

    play(time: number): void {
        this.youtube!.seekTo(time, true);
        this.youtube!.playVideo();
    }

    is2PO(): boolean {
        return this.dto?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_2PO;
    }

    is3PO(): boolean {
        return this.dto?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_3PO;
    }

    isFirstUmpire(): boolean {
        return this.dto?.reportee === Reportee.FIRST_REFEREE;
    }

    isSecondUmpire(): boolean {
        return this.dto?.reportee === Reportee.SECOND_REFEREE;
    }

    isThirdUmpire(): boolean {
        return this.dto?.reportee === Reportee.THIRD_REFEREE;
    }

}
