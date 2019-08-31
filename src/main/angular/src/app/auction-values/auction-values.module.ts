import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuctionValuesRoutingModule } from './auction-values-routing.module';
import { AuctionValuesComponent } from './auction-values.component';


@NgModule({
  declarations: [AuctionValuesComponent],
  imports: [
    CommonModule,
    AuctionValuesRoutingModule
  ]
})
export class AuctionValuesModule { }
