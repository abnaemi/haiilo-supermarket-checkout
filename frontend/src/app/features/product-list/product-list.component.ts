import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { PRODUCT_IMAGES } from '../../core/config/product-images';
import { Product } from '../../core/models/product';
import { CartDialog } from '../cart-dialog/cart-dialog';
import { ProductCardComponent } from '../product-card/product-card';
import { OfferInfoPipe } from '../../core/pipes/offer-info.pipe';
import { CartState } from '../../core/state/cart.state';
import { AdminPanelComponent } from '../admin-panel/admin-panel.component';
import { ProductState } from '../../core/state/product.state';
import { OfferState } from '../../core/state/offer.state';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatBadgeModule,
    MatSnackBarModule,
    MatDialogModule,
    MatButtonModule,
    ProductCardComponent,
    OfferInfoPipe,
    AdminPanelComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent {
  protected productState = inject(ProductState);
  protected cartState = inject(CartState);
  protected offerState = inject(OfferState);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  onAddToCart(event: { product: Product, quantity: number }): void {
    this.cartState.addToCart(event.product, event.quantity);

    this.snackBar.open(`${event.quantity}x ${event.product.name} added to cart!`, 'Close', {
      duration: 2000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
  }

  openCart(): void {
    this.dialog.open(CartDialog, {
      width: '500px',
      autoFocus: false
    });
  }

  getProductImage(name: string): string {
    const imageUrl = PRODUCT_IMAGES[name];
    if (!imageUrl) {
      console.warn(`No image found for product: "${name}". Using default image.`);
      return PRODUCT_IMAGES['Default'];
    }
    return imageUrl;
  }
}
