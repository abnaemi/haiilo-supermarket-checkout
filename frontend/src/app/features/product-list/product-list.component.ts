import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { PRODUCT_IMAGES } from '../../core/config/product-images';
import { Product } from '../../core/models/Product';
import { CartService } from '../../core/service/cart.service';
import { ProductService } from '../../core/service/product.service';
import { CartDialog } from '../cart-dialog/cart-dialog';
import {ProductCardComponent} from '../product-card/product-card';


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
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService);
  public cartService = inject(CartService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);

  products: Product[] = [];

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Connection to Backend failed:', err);
        this.snackBar.open('Backend not reachable!', 'Close', { duration: 5000 });
      }
    });
  }

  onAddToCart(event: { product: Product, quantity: number }): void {
    this.cartService.addToCart(event.product, event.quantity);

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
    return PRODUCT_IMAGES[name] || PRODUCT_IMAGES['Default'];
  }

  getOfferInfo(productId: string): string | null {
    const offer = this.cartService.weeklyOffers().find(o => String(o.productId) === String(productId));
    if (offer) {
      return `Special Offer: ${offer.requiredQuantity} for ${offer.offerPrice.toFixed(2)} €`;
    }
    return null;
  }
}
