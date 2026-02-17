import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ProductService} from '../../core/service/product.service';
import {Product} from '../../core/models/Product';
import {PRODUCT_IMAGES} from '../../core/config/product-images';
import {MatToolbar} from '@angular/material/toolbar';
import {MatIcon} from '@angular/material/icon';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader, MatCardImage,
  MatCardSubtitle,
  MatCardTitle
} from '@angular/material/card';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatBadge} from '@angular/material/badge';
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, MatToolbar, MatIcon, MatCard, MatCardHeader, MatCardTitle, MatCardContent, MatCardSubtitle, MatCardActions, MatCardImage, MatIconButton, MatBadge, MatButton],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService);

  products: Product[] = [];

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Fehler beim Laden der Produkte', err)
    });
  }

  addToCart(product: Product): void {
    console.log('Hinzugefügt zum Warenkorb:', product.name);
  }

  getProductImage(name: string): string {
    return PRODUCT_IMAGES[name] || PRODUCT_IMAGES['Default'];
  }
}
