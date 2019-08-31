import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { ProjectedAuctionValue } from '../core/models/projected-auction-value';
import { ProjectedAuctionValueService } from '../core/services/projected-auction-value.service';

@Component({
  selector: 'auc-auction-values',
  templateUrl: './auction-values.component.html',
  styleUrls: ['./auction-values.component.css']
})
export class AuctionValuesComponent implements OnInit {

  projAuctionValues: ProjectedAuctionValue[];
  

  constructor(
    private route: ActivatedRoute,
    private projAuctionValueService: ProjectedAuctionValueService
  ) { }

  ngOnInit() {
    let leagueId = +this.route.snapshot.paramMap.get('leagueId');
    this.projAuctionValueService.findForLeague(leagueId).subscribe(results => this.projAuctionValues = results);
  }

  getAvailableDollars() {
    let totalDollars = 0;
    if (this.projAuctionValues) {
      this.projAuctionValues.forEach(av => {
        let currentCost = +av.cost;
        totalDollars += currentCost;
      });
    }
    return 2400 - totalDollars;
  }

}
