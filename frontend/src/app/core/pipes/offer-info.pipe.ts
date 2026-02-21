import { Pipe, PipeTransform } from '@angular/core';
import { WeeklyOffer } from '../models/weekly-offer.model';

@Pipe({
  name: 'offerInfo',
  standalone: true
})
export class OfferInfoPipe implements PipeTransform {
  transform(productId: string, offers: WeeklyOffer[]): string | null {
    if (!offers || offers.length === 0) {
      return null;
    }

    const offer = offers.find(o => String(o.productId) === String(productId));
    if (offer) {
      return `Special Offer: ${offer.requiredQuantity} for ${offer.offerPrice.toFixed(2)} €`;
    }

    return null;
  }
}
