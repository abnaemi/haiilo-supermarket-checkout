import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../enviroments/enviroments';
import { WeeklyOffer } from '../models/weekly-offer.model';
import { CreateOfferDto } from '../models/create-offer.dto';

@Injectable({ providedIn: 'root' })
export class OfferService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/offers`;

  getOffers(): Observable<WeeklyOffer[]> {
    return this.http.get<WeeklyOffer[]>(this.apiUrl);
  }

  createOffer(offer: CreateOfferDto): Observable<WeeklyOffer> {
    return this.http.post<WeeklyOffer>(this.apiUrl, offer);
  }

  deleteOffer(offerId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${offerId}`);
  }
}
