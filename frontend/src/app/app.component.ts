import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from './core/service/product.service';
import { OfferService } from './core/service/offer.service';
import { ProductListComponent } from './features/product-list/product-list.component';

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

  errorMessage: string | null = null;

  newProduct = { name: '', price: 0 };
  newOffer = { productId: '', requiredQuantity: 1, offerPrice: 0 };

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.errorMessage = null;
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
      this.productService.createProduct(this.newProduct).subscribe({
        next: () => {
          this.showProductPopup = false;
          this.newProduct = { name: '', price: 0 };
          this.refresh();
        },
        error: (err) => console.error('Error saving product', err)
      });
    }
  }

  deleteProduct(id: string): void {
    if (confirm('Delete Product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => this.refresh(),
        error: (err) => {
          this.errorMessage = "Can't delete because it's used in weekly offer";
          console.error('Delete failed', err);
          setTimeout(() => this.errorMessage = null, 5000);
        }
      });
    }
  }

  saveOffer(): void {
    if (this.newOffer.productId && this.newOffer.requiredQuantity > 0) {
      this.offerService.createOffer(this.newOffer).subscribe({
        next: () => {
          this.showOfferPopup = false;
          this.newOffer = { productId: '', requiredQuantity: 1, offerPrice: 0 };
          this.refresh();
        },
        error: (err) => console.error('Error saving offer', err)
      });
    }
  }

  deleteOffer(id: string): void {
    if (confirm('Delete Offer?')) {
      this.offerService.deleteOffer(id).subscribe({
        next: () => this.refresh(),
        error: (err) => console.error('Error deleting offer', err)
      });
    }
  }
}
