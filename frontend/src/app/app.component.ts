import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './features/product-list/product-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ProductListComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
}
