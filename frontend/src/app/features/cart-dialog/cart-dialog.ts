import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {CartService} from '../../core/service/cart.service';
import {CartItem} from '../../core/models/cart-item.model';

@Component({
  selector: 'app-cart-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatListModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './cart-dialog.html',
  styleUrl: './cart-dialog.scss'
})
export class CartDialog {

  public cartService = inject(CartService);
  private dialogRef = inject(MatDialogRef<CartDialog>);

  checkout() {
    console.log('Ordering items:', this.cartService.items());
    alert('Thank you for your order!');
    this.cartService.clearCart();
    this.dialogRef.close();
  }

  isOfferActive(item: CartItem): boolean {
    const offers = this.cartService['weeklyOffers']();
    if (!offers) return false;

    const offer = offers.find((o: any) => String(o.productId) === String(item.id));

    return !!offer && item.quantity >= offer.requiredQuantity;
  }

  calculateSubtotal(item: CartItem): number {
    const offers = this.cartService['weeklyOffers']();

    const offer = offers?.find((o: any) => String(o.productId) === String(item.id));

    if (offer && item.quantity >= offer.requiredQuantity) {
      const bundlePrice = offer.offerPrice;
      const extraItems = item.quantity - offer.requiredQuantity;
      return bundlePrice + (extraItems * item.price);
    }

    return item.quantity * item.price;
  }

}
