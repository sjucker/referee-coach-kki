import {Component, OnInit, ViewChild} from '@angular/core';
import {YouTubePlayer} from "@angular/youtube-player";
import {BasketplanService} from "../service/basketplan.service";
import {ExpertiseService} from "../service/expertise.service";
import {BasketplanGameDTO, OfficiatingMode, Reportee, VideoCommentDTO, VideoExpertiseDTO} from "../rest";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-expertise',
    templateUrl: './expertise.component.html',
    styleUrls: ['./expertise.component.css']
})
export class ExpertiseComponent implements OnInit {

    @ViewChild('youtubePlayer') youtube?: YouTubePlayer;

    report?: VideoExpertiseDTO;

    constructor(private readonly basketplanService: BasketplanService,
                private readonly expertiseService: ExpertiseService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit(): void {
        // This code loads the IFrame Player API code asynchronously, according to the instructions at
        // https://developers.google.com/youtube/iframe_api_reference#Getting_Started
        const tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        document.body.appendChild(tag);

        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.expertiseService.getExpertise(id).subscribe(dto => {
                // TODO error handling
                this.report = dto;
            });
        }
    }

    jumpTo(time: number): void {
        this.youtube!.seekTo(time, true)
    }

    save() {
        this.expertiseService.saveExpertise(this.report!).subscribe(dto => {
            this.report = dto;
        })
    }

    addVideoComment(): void {
        this.report!.videoComments.push({
            comment: '',
            timestamp: Math.round(this.youtube!.getCurrentTime())
        })
    }

    deleteComment(videoComment: VideoCommentDTO) {
        this.report!.videoComments.splice(this.report!.videoComments.indexOf(videoComment), 1);
    }

    is2PO(): boolean {
        return this.report?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_2PO;
    }

    is3PO(): boolean {
        return this.report?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_3PO;
    }

    isFirstUmpire(): boolean {
        return this.report?.reportee === Reportee.FIRST_REFEREE;
    }

    isSecondUmpire(): boolean {
        return this.report?.reportee === Reportee.SECOND_REFEREE;
    }

    isThirdUmpire(): boolean {
        return this.report?.reportee === Reportee.THIRD_REFEREE;
    }

}
