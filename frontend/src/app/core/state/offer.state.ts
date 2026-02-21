import { Injectable, signal, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { OfferService } from '../service/offer.service';

@Injectable({ providedIn: 'root' })
export class OfferState {
  private offerService = inject(OfferService);
  private refreshTrigger = signal(0);


  public readonly offers = toSignal(
    toObservable(this.refreshTrigger).pipe(
      switchMap(() => this.offerService.getOffers())
    ),
    { initialValue: [] }
  );


  public refresh(): void {
    this.refreshTrigger.update(v => v + 1);
  }
}
