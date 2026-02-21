import { Injectable, signal, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { ProductService } from '../service/product.service';

@Injectable({ providedIn: 'root' })
export class ProductState {
  private productService = inject(ProductService);
  private refreshTrigger = signal(0);


  public readonly products = toSignal(
    toObservable(this.refreshTrigger).pipe(
      switchMap(() => this.productService.getProducts())
    ),
    { initialValue: [] }
  );


  public refresh(): void {
    this.refreshTrigger.update(v => v + 1);
  }
}
