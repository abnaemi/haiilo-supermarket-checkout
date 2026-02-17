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
import {CartCalculator} from '../../core/utis/CartCalculator';

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
    const confirmRef = this.dialog.open(ConfirmDialog, {
      width: '350px',
      data: { total: this.cartService.totalPrice() }
    });

    confirmRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.performCheckout();
      }
    });
  }

  private performCheckout() {
    this.cartService.checkout().subscribe({
      next: () => {
        this.cartService.clearCart();
        this.dialogRef.close();

        this.snackBar.open('🚀 Order placed successfully!', 'Great', {
          duration: 5000,
          panelClass: ['success-snackbar']
        });
      },
      error: (err) => {
        console.error('Checkout failed', err);
        this.snackBar.open('❌ Order failed. Please try again.', 'Close', {
          duration: 5000
        });
      }
    });
  }

  isOfferActive(item: CartItem): boolean {
    const offers = this.cartService.weeklyOffers();
    const offer = offers.find(o => String(o.productId) === String(item.id));
    return !!offer && item.quantity >= offer.requiredQuantity;
  }

  calculateSubtotal(item: CartItem): number {
    return CartCalculator.calculateItemSubtotal(item, this.cartService.weeklyOffers());
  }
}
