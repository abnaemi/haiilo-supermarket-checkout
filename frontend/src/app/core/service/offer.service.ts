import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WeeklyOffer {
  id: string;
  productId: string;
  productName: string;
  requiredQuantity: number;
  offerPrice: number;
}

@Injectable({ providedIn: 'root' })
export class OfferService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v1/offers';

  getOffers(): Observable<WeeklyOffer[]> {
    return this.http.get<WeeklyOffer[]>(this.apiUrl);
  }
}
