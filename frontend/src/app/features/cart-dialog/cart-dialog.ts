import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatDialog, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {CartService} from '../../core/service/cart.service';
import {CartItem} from '../../core/models/cart-item.model';
import {ConfirmDialog} from '../confirm-dialog/confirm-dialog';
import {MatSnackBar} from '@angular/material/snack-bar';

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
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  checkout() {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '350px',
      data: { total: this.cartService.totalPrice() }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.performCheckout();
      }
    });
  }

  private performCheckout() {
    this.cartService.clearCart();
    this.dialog.closeAll();

    this.snackBar.open('🚀 Order placed successfully! Thank you.', 'Great!', {
      duration: 5000,
      panelClass: ['success-snackbar'],
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
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
