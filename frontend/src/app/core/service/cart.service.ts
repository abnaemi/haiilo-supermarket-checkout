import { Injectable, signal, computed, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { OfferService, WeeklyOffer } from './offer.service';
import { OrderService } from './order.service';
import { CartItem } from '../models/cart-item.model';
import { Product } from '../models/product';
import {CartCalculator} from '../utils/CartCalculator';

@Injectable({ providedIn: 'root' })
export class CartService {
  private offerService = inject(OfferService);
  private orderService = inject(OrderService);

  private cartItems = signal<CartItem[]>([]);

  weeklyOffers = signal<WeeklyOffer[]>([]);
  items = this.cartItems.asReadonly();

  count = computed(() => this.cartItems().reduce((s, i) => s + i.quantity, 0));

  totalPrice = computed(() => {
    return this.cartItems().reduce((total, item) => {
      return total + CartCalculator.calculateItemSubtotal(item, this.weeklyOffers());
    }, 0);
  });

  constructor() {
    this.loadInitialOffers();
  }

  private loadInitialOffers() {
    this.offerService.getOffers().subscribe(offers => this.weeklyOffers.set(offers));
  }

  addToCart(product: Product, quantity: number) {
    this.cartItems.update(items => {
      const index = items.findIndex(i => String(i.id) === String(product.id));

      if (index > -1) {
        const updated = [...items];
        const newQty = updated[index].quantity + quantity;
        updated[index] = { ...updated[index], quantity: Math.min(newQty, 99) };
        return updated;
      }

      return [...items, { ...product, quantity }];
    });
  }

  updateQuantity(id: number | string, delta: number) {
    this.cartItems.update(items => items
      .map(i => i.id === id ? { ...i, quantity: i.quantity + delta } : i)
      .filter(i => i.quantity > 0 && i.quantity <= 99)
    );
  }

  clearCart() {
    this.cartItems.set([]);
  }

  checkout(): Observable<any> {
    return this.orderService.placeOrder(this.totalPrice(), this.cartItems());
  }
}
