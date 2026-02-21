import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../enviroments/enviroments';
import { CartItem } from '../models/cart-item.model';

export interface CheckoutItem {
  productId: string;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/orders`;

  placeOrder(items: CartItem[]): Observable<any> {
    const checkoutItems: CheckoutItem[] = items.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }));
    return this.http.post(`${this.apiUrl}/checkout`, checkoutItems);
  }
}
