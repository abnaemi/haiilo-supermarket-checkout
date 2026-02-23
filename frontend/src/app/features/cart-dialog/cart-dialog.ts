import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CartState } from '../../core/state/cart.state';
import { ConfirmDialog } from '../confirm-dialog/confirm-dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartCalculator } from '../../core/utils/CartCalculator';
import { OfferState } from '../../core/state/offer.state';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Customer } from '../../core/models/customer.model';

@Component({
  selector: 'app-cart-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './cart-dialog.html',
  styleUrl: './cart-dialog.scss'
})
export class CartDialog {
  protected cartState = inject(CartState);
  protected offerState = inject(OfferState);
  private dialogRef = inject(MatDialogRef<CartDialog>);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private fb = inject(FormBuilder);

  protected readonly CartCalculator = CartCalculator;

  customerForm: FormGroup = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    street: ['', Validators.required],
    city: ['', Validators.required],
    country: ['', Validators.required],
    phoneNumber: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  });

  checkout() {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    const confirmRef = this.dialog.open(ConfirmDialog, {
      width: '350px',
      data: { total: this.cartState.totalPrice() }
    });

    confirmRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.performCheckout();
      }
    });
  }

  private performCheckout() {
    const customer: Customer = this.customerForm.value;
    this.cartState.checkout(customer).subscribe({
      next: () => {
        this.cartState.clearCart();
        this.dialogRef.close();

        this.snackBar.open('🚀 Order placed successfully!', 'Great', {
          duration: 5000,
          panelClass: ['success-snackbar']
        });
      },
      // Generic errors are now handled by the HttpErrorInterceptor
    });
  }
}
