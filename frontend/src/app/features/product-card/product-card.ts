import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import {Product} from '../../core/models/product';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatFormFieldModule
  ],
  templateUrl: './product-card.html',
  styleUrl: './product-card.scss',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductCardComponent {
  @Input({ required: true }) product!: Product;
  @Input() image: string = '';
  @Input() offerInfo: string | null = null;

  @Output() addToCart = new EventEmitter<{ product: Product, quantity: number }>();

  quantity: number = 1;

  onAdd() {
    if (this.quantity > 0 && this.quantity <= 99) {
      this.addToCart.emit({ product: this.product, quantity: this.quantity });
      this.quantity = 1;
    }
  }
}
