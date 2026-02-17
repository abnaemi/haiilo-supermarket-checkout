import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { PRODUCT_IMAGES } from '../../core/config/product-images';
import {ProductService} from '../../core/service/product.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatBadgeModule,
    MatSnackBarModule
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService);
  private snackBar = inject(MatSnackBar);

  products: any[] = [];
  cartCount: number = 0;

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: (err) => {
        console.error('CORS or Server Error:', err);
        this.snackBar.open('Could not load products. Is the backend running?', 'Close', { duration: 5000 });
      }
    });
  }

  getProductImage(name: string): string {
    return PRODUCT_IMAGES[name] || PRODUCT_IMAGES['Default'];
  }

  addToCart(product: any): void {
    this.cartCount++;
    this.snackBar.open(`${product.name} added to cart!`, 'Close', {
      duration: 2000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
  }
}
