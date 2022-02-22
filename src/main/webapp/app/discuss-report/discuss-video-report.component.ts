import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {VideoReportService} from "../service/video-report.service";
import {AuthenticationService} from "../service/authentication.service";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CommentReplyDTO, OfficiatingMode, VideoCommentDTO, VideoReportDiscussionDTO} from "../rest";
import {YouTubePlayer} from "@angular/youtube-player";
import {VideoReportReplyDialogComponent} from "../video-report-reply-dialog/video-report-reply-dialog.component";
import {Observable, of} from "rxjs";
import {
    DiscussVideoReportUnsavedRepliesDialogComponent
} from "../discuss-video-report-unsaved-replies-dialog/discuss-video-report-unsaved-replies-dialog.component";
import {DiscussVideoReportFinishDialogComponent} from "../discuss-video-report-finish-dialog/discuss-video-report-finish-dialog.component";

@Component({
    selector: 'app-discuss-video-report',
    templateUrl: './discuss-video-report.component.html',
    styleUrls: ['./discuss-video-report.component.css']
})
export class DiscussVideoReportComponent implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild('youtubePlayer') youtube?: YouTubePlayer;
    @ViewChild('widthMeasurement') widthMeasurement?: ElementRef<HTMLDivElement>;

    videoWidth?: number;
    videoHeight?: number;

    dto?: VideoReportDiscussionDTO;
    notFound = false;

    replies: CommentReplyDTO[] = [];

    constructor(private route: ActivatedRoute,
                private videoReportService: VideoReportService,
                private authenticationService: AuthenticationService,
                public dialog: MatDialog,
                public snackBar: MatSnackBar) {
    }

    ngOnInit(): void {
        // This code loads the IFrame Player API code asynchronously, according to the instructions at
        // https://developers.google.com/youtube/iframe_api_reference#Getting_Started
        const tag = document.createElement('script');
        tag.src = 'https://www.youtube.com/iframe_api';
        document.body.appendChild(tag);

        this.videoReportService.getVideoReportDiscussion(this.route.snapshot.paramMap.get('id')!).subscribe(
            result => {
                this.dto = result;
            },
            error => {
                this.notFound = true;
            }
        );
    }

    ngAfterViewInit(): void {
        this.onResize();
        window.addEventListener('resize', this.onResize);
    }

    onResize = (): void => {
        // minus padding (16px each side) and margin (10px each)
        const contentWidth = this.widthMeasurement!.nativeElement.clientWidth - 52;

        this.videoWidth = Math.min(contentWidth, 720);
        this.videoHeight = this.videoWidth * 0.6;
    }

    ngOnDestroy(): void {
        window.removeEventListener('resize', this.onResize);
    }

    isLoggedIn(): boolean {
        return this.authenticationService.isLoggedIn();
    }


    is2PO(): boolean {
        return this.dto?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_2PO;
    }

    is3PO(): boolean {
        return this.dto?.basketplanGame.officiatingMode === OfficiatingMode.OFFICIATING_3PO;
    }

    play(time: number): void {
        this.youtube!.seekTo(time, true);
        this.youtube!.playVideo();
    }

    reply(comment: VideoCommentDTO): void {
        this.dialog.open(VideoReportReplyDialogComponent, {
            data: this.dto,
            disableClose: true,
            hasBackdrop: true,
        }).afterClosed().subscribe(reply => {
            if (reply) {
                this.replies = [...this.replies, {
                    commentId: comment.id!,
                    comment: reply
                }];
                comment.replies.push({
                    id: 0,
                    reply: reply,
                    repliedAt: new Date(),
                    repliedBy: 'New Reply'
                })
            }
        });
    }

    finishReply(): void {
        this.dialog.open(DiscussVideoReportFinishDialogComponent).afterClosed().subscribe(decision => {
            if (decision && this.dto) {
                this.videoReportService.reply(this.dto.videoReportId, this.replies).subscribe(
                    success => {
                        this.showMessage("Replies saved");
                        this.replies = [];
                    },
                    error => {
                        this.showMessage("Replies could not be saved...")
                    }
                );
            }
        })

    }

    private showMessage(message: string) {
        this.snackBar.open(message, undefined, {
            duration: 3000,
            verticalPosition: "top",
            horizontalPosition: "center"
        });
    }

    hasUnsavedReplies(): boolean {
        return this.replies.length > 0;
    }

    canDeactivate(): Observable<boolean> {
        if (this.hasUnsavedReplies()) {
            return this.dialog.open(DiscussVideoReportUnsavedRepliesDialogComponent).afterClosed();
        } else {
            return of(true);
        }
    }

}
