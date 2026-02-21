import { CartItem } from '../models/cart-item.model';
import { WeeklyOffer } from '../models/weekly-offer.model';

export class CartCalculator {

  static isOfferActive(item: CartItem, offers: WeeklyOffer[]): boolean {
    const offer = offers.find(o => String(o.productId) === String(item.id));
    return !!offer && item.quantity >= offer.requiredQuantity;
  }

  static calculateItemSubtotal(item: CartItem, offers: WeeklyOffer[]): number {
    const offer = offers.find(o => String(o.productId) === String(item.id));

    if (this.isOfferActive(item, offers) && offer) {
      const numberOfOfferBundles = Math.floor(item.quantity / offer.requiredQuantity);
      const remainingQuantity = item.quantity % offer.requiredQuantity;
      const priceForOfferBundles = numberOfOfferBundles * offer.offerPrice;
      return priceForOfferBundles + (remainingQuantity * item.price);
    }

    return item.quantity * item.price;
  }
}
