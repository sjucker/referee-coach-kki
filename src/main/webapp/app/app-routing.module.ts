import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VideoReportComponent} from "./video-report/video-report.component";
import {ViewVideoReportComponent} from "./view-report/view-video-report.component";
import {MainComponent} from "./main/main.component";
import {LoginComponent} from "./login/login.component";
import {AuthenticationGuard} from "./service/authentication.guard";
import {SettingsComponent} from "./settings/settings.component";
import {UnsavedChangesGuard} from "./service/unsaved-changes.guard";
import {DiscussVideoReportComponent} from "./discuss-report/discuss-video-report.component";
import {UnsavedRepliesGuard} from "./service/unsaved-replies.guard";
import {ReportSearchComponent} from "./report-search/report-search.component";

export const LOGIN_PATH = 'login'
export const EDIT_PATH = 'edit'
export const VIEW_PATH = 'view'
export const DISCUSS_PATH = 'discuss'
export const SETTINGS_PATH = 'settings'
export const SEARCH_PATH = 'search'

const routes: Routes = [
    {
        path: '',
        component: MainComponent,
        canActivate: [AuthenticationGuard]
    },
    {
        path: VIEW_PATH + '/:id',
        component: ViewVideoReportComponent
        // allowed without being logged in, anonymous user only needs to know the report's ID (which should be hard to guess)
    },
    {
        path: DISCUSS_PATH + '/:id',
        component: DiscussVideoReportComponent,
        // allowed without being logged in, anonymous user only needs to know the report's ID (which should be hard to guess)
        canDeactivate: [UnsavedRepliesGuard]
    },
    {
        path: EDIT_PATH + '/:id',
        component: VideoReportComponent,
        canActivate: [AuthenticationGuard],
        canDeactivate: [UnsavedChangesGuard]
    },
    {
        path: LOGIN_PATH,
        component: LoginComponent
    },
    {
        path: SETTINGS_PATH,
        component: SettingsComponent,
        canActivate: [AuthenticationGuard]
    },
    {
        path: SEARCH_PATH,
        component: ReportSearchComponent,
        canActivate: [AuthenticationGuard]
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true})],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
