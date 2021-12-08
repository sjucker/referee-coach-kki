import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExpertiseComponent} from "./expertise/expertise.component";
import {ViewExpertiseComponent} from "./view-expertise/view-expertise.component";
import {MainComponent} from "./main/main.component";

const routes: Routes = [
    {
        path: '',
        component: MainComponent
    },
    {
        path: 'view/:id',
        component: ViewExpertiseComponent
    },
    {
        path: 'edit',
        component: ExpertiseComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true})],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
