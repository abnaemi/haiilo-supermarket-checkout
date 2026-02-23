import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../enviroments/enviroments';
import { CartItem } from '../models/cart-item.model';
import { Customer } from '../models/customer.model';

export interface CheckoutItem {
  productId: string;
  quantity: number;
}

export interface OrderRequest {
  customer: Customer;
  items: CheckoutItem[];
}

@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/orders`;

  placeOrder(items: CartItem[], customer: Customer): Observable<any> {
    const checkoutItems: CheckoutItem[] = items.map(item => ({
      productId: item.id,
      quantity: item.quantity
    }));

    const orderRequest: OrderRequest = {
      customer: customer,
      items: checkoutItems
    };

    return this.http.post(`${this.apiUrl}/checkout`, orderRequest);
  }
}
