import {Component, OnInit, ViewChild} from '@angular/core';
import {YouTubePlayer} from "@angular/youtube-player";
import {BasketplanService} from "../service/basketplan.service";
import {ExpertiseService} from "../service/expertise.service";
import {ExpertiseDTO, VideoCommentDTO} from "../rest";
import {ActivatedRoute, Router} from "@angular/router";

interface VideoExpertiseModel {
  gameNumber: string;
  youtubeLink: string;
  expertise?: ExpertiseDTO;
}

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  @ViewChild('youtubePlayer') youtube?: YouTubePlayer;
  model: VideoExpertiseModel = {
    gameNumber: '21-05249', // TODO remove
    youtubeLink: '',
    expertise: undefined
  }

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
        this.model.expertise = dto;
      });
    }
  }

  jumpTo(time: number): void {
    this.youtube!.seekTo(time, true)
  }

  searchGame() {
    // TODO error handling
    this.basketplanService.searchGame(this.model.gameNumber).subscribe(result => {
      this.model.expertise = {
        id: undefined,
        basketplanGame: result,
        imageComment: '',
        mechanicsComment: '',
        foulsComment: '',
        gameManagementComment: '',
        pointsToKeepComment: '',
        pointsToImproveComment: '',
        videoComments: [],
      }
    })
  }

  loadVideo() {
    if (this.model.expertise && this.model.youtubeLink) {
      const match = this.model.youtubeLink.match(/v=([^&]+)/);
      if (match) {
        this.model.expertise.basketplanGame.youtubeId = match[1];
      } else {
        alert("Could not parse provided YouTube link...")
      }
    } else {
      // TODO alert
    }

  }

  save() {
    this.expertiseService.saveExpertise(this.model.expertise!).subscribe(dto => {
      this.model.expertise = dto;
      if (this.route.snapshot.url.length == 0) {
        this.router.navigate(['/edit/' + dto.id]);
      }
    })
  }

  addVideoComment(): void {
    this.model.expertise!.videoComments.push({
      comment: '',
      timestamp: Math.round(this.youtube!.getCurrentTime())
    })
  }

  deleteComment(videoComment: VideoCommentDTO) {
    this.model.expertise!.videoComments.splice(this.model.expertise!.videoComments.indexOf(videoComment), 1);
  }

}
