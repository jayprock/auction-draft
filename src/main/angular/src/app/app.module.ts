import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AuctionValuesModule } from './auction-values/auction-values.module';
import { BrowserModule } from '@angular/platform-browser';
import { CoreModule } from './core/core.module';
import { HomeModule } from './home/home.module';
import { NgModule } from '@angular/core';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    HomeModule,
    AuctionValuesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
