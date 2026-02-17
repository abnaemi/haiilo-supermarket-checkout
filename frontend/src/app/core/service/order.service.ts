import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CartItem} from '../models/cart-item.model';
import {Observable} from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/v1/orders';

  placeOrder(total: number, items: CartItem[]): Observable<any> {
    const payload = {
      totalAmount: total,
      items: items.map(item => ({
        productId: item.id,
        productName: item.name,
        quantity: item.quantity,
        priceAtPurchase: item.price
      }))
    };
    return this.http.post(this.API_URL, payload);
  }
}
