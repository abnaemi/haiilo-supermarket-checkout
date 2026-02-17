import { Injectable, signal, computed, inject } from '@angular/core';
import { OfferService, WeeklyOffer } from './offer.service';
import { CartItem } from '../models/cart-item.model';
import { Product } from '../models/Product';
import {Observable} from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class CartService {
  private offerService = inject(OfferService);
  private http = inject(HttpClient);
  private cartItems = signal<CartItem[]>([]);
  private weeklyOffers = signal<WeeklyOffer[]>([]);


  items = this.cartItems.asReadonly();

  count = computed(() => this.cartItems().reduce((s, i) => s + i.quantity, 0));

  constructor() {
    this.loadOffers();
  }

  loadOffers() {
    this.offerService.getOffers().subscribe(offers => this.weeklyOffers.set(offers));
  }

  totalPrice = computed(() => {
    return this.cartItems().reduce((total, item) => {
      const offer = this.weeklyOffers().find(o => String(o.productId) === String(item.id));
      if (offer && item.quantity >= offer.requiredQuantity) {


        const priceForOfferBundle = offer.offerPrice;
        const remainingQuantity = item.quantity - offer.requiredQuantity;
        const priceForRest = remainingQuantity * item.price;

        const subtotal = priceForOfferBundle + priceForRest;

        console.log(`Einmaliges Angebot für ${item.name}: Paketpreis ${priceForOfferBundle}€ + Rest (${remainingQuantity} Stk) ${priceForRest}€ = ${subtotal}€`);

        return total + subtotal;
      }

      return total + (item.quantity * item.price);
    }, 0);
  });

  addToCart(product: Product, quantity: number) {
    this.cartItems.update(items => {
      const index = items.findIndex(i => String(i.id) === String(product.id));

      if (index > -1) {
        const updated = [...items];
        const newQty = updated[index].quantity + quantity;
        updated[index] = { ...updated[index], quantity: newQty > 99 ? 99 : newQty };
        return updated;
      }

      return [...items, { ...product, quantity: quantity }];
    });
  }

  updateQuantity(id: number | string, delta: number) {
    this.cartItems.update(items => items.map(i =>
      i.id === id ? { ...i, quantity: i.quantity + delta } : i
    ).filter(i => i.quantity > 0));
  }

  clearCart() {
    this.cartItems.set([]);
  }


checkout(): Observable<any> {
  const orderData = {
    totalAmount: this.totalPrice(),
    items: this.items().map(item => ({
      productId: item.id,
      productName: item.name,
      quantity: item.quantity,
      priceAtPurchase: item.price
    }))
  };

  return this.http.post('http://localhost:8080/api/v1/orders', orderData);
}
}
