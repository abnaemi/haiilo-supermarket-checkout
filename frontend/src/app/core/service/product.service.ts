import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/Product';
import { environment } from '../../../enviroments/enviroments';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/products`;

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  // Create: Sendet Name und Preis an @PostMapping
  createProduct(product: { name: string, price: number }): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  // Delete: Nutzt die UUID für @DeleteMapping("/{id}")
  deleteProduct(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
