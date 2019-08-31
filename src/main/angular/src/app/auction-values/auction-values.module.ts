import { AuctionValuesComponent } from './auction-values.component';
import { AuctionValuesRoutingModule } from './auction-values-routing.module';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

@NgModule({
  declarations: [AuctionValuesComponent],
  imports: [
    CommonModule,
    FormsModule,
    AuctionValuesRoutingModule
  ]
})
export class AuctionValuesModule { }
