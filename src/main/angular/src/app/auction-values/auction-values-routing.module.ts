import { RouterModule, Routes } from '@angular/router';

import { AuctionValuesComponent } from './auction-values.component';
import { NgModule } from '@angular/core';

const routes: Routes = [
  { path: 'auction-values/:leagueId', component: AuctionValuesComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuctionValuesRoutingModule { }
