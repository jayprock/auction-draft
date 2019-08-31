import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProjectedAuctionValue } from '../models/projected-auction-value';
import { REST_PATHS } from 'src/app/rest-paths';

@Injectable({
  providedIn: 'root'
})
export class ProjectedAuctionValueService {

  constructor(private http: HttpClient) { }

  findForLeague(leagueId: number): Observable<ProjectedAuctionValue[]> {
    return this.http.get<ProjectedAuctionValue[]>(`${REST_PATHS.projections}/${leagueId}`);
  }

}
