import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'auc-auction-values',
  templateUrl: './auction-values.component.html',
  styleUrls: ['./auction-values.component.css']
})
export class AuctionValuesComponent implements OnInit {

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    let leagueId = this.route.snapshot.paramMap.get('leagueId');
    console.log(leagueId);
  }

}
