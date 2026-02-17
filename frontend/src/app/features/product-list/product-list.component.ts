import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { PRODUCT_IMAGES } from '../../core/config/product-images';
import { Product } from '../../core/models/Product';
import { CartService } from '../../core/service/cart.service';
import { ProductService } from '../../core/service/product.service';
import { CartDialog } from '../cart-dialog/cart-dialog';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatBadgeModule,
    MatSnackBarModule,
    MatDialogModule,
    MatInputModule,
    MatFormFieldModule
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
  quantities: { [productId: string]: number } = {};

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
        data.forEach(p => this.quantities[p.id] = 1);
      },
      error: (err) => {
        console.error('Connection to Backend failed:', err);
        this.snackBar.open('Backend not reachable!', 'Close', { duration: 5000 });
      }
    });
  }

  getProductImage(name: string): string {
    return PRODUCT_IMAGES[name] || PRODUCT_IMAGES['Default'];
  }

  addToCart(product: Product): void {
    let qty = this.quantities[product.id] || 1;

    if (qty > 99) qty = 99;
    if (qty < 1) qty = 1;

    this.cartService.addToCart(product, qty);

    this.snackBar.open(`${qty}x ${product.name} added to cart!`, 'Close', {
      duration: 2000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });

    this.quantities[product.id] = 1;
  }

  openCart(): void {
    this.dialog.open(CartDialog, {
      width: '500px',
      autoFocus: false
    });
  }
}
