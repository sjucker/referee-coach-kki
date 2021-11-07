import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./main/main.component";
import {ViewExpertiseComponent} from "./view-expertise/view-expertise.component";

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
    path: 'edit/:id',
    component: MainComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
