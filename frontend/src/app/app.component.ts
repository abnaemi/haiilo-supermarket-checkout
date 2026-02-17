import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from './core/service/product.service';
import { OfferService } from './core/service/offer.service';
import {ProductListComponent} from './features/product-list/product-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductListComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private productService = inject(ProductService);
  private offerService = inject(OfferService);

  products: any[] = [];
  offers: any[] = [];

  showProductPopup = false;
  showOfferPopup = false;

  newProduct = { name: '', price: 0 };
  newOffer = { productId: '', requiredQuantity: 1, offerPrice: 0 };

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.productService.getProducts().subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error loading products', err)
    });
    this.offerService.getOffers().subscribe({
      next: (data) => this.offers = data,
      error: (err) => console.error('Error loading offers', err)
    });
  }

  selectProductForOffer(productId: string): void {
    this.newOffer.productId = productId;
    this.showOfferPopup = true;
  }

  saveProduct(): void {
    if (this.newProduct.name && this.newProduct.price > 0) {
      this.productService.createProduct(this.newProduct).subscribe(() => {
        this.showProductPopup = false;
        this.newProduct = { name: '', price: 0 };
        this.refresh();
      });
    }
  }

  deleteProduct(id: string): void {
    if (confirm('Produkt wirklich löschen?')) {
      this.productService.deleteProduct(id).subscribe(() => this.refresh());
    }
  }

  saveOffer(): void {
    if (this.newOffer.productId && this.newOffer.requiredQuantity > 0) {
      this.offerService.createOffer(this.newOffer).subscribe(() => {
        this.showOfferPopup = false;
        this.newOffer = { productId: '', requiredQuantity: 1, offerPrice: 0 };
        this.refresh();
      });
    }
  }

  deleteOffer(id: string): void {
    if (confirm('Angebot wirklich löschen?')) {
      this.offerService.deleteOffer(id).subscribe(() => this.refresh());
    }
  }
}
