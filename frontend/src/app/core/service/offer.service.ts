import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from '../../../enviroments/enviroments';

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
  private readonly apiUrl = `${environment.apiUrl}/offers`;

  getOffers(): Observable<WeeklyOffer[]> {
    return this.http.get<WeeklyOffer[]>(this.apiUrl);
  }
}
