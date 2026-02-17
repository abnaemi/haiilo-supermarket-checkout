import {CartItem} from '../models/cart-item.model';
import {WeeklyOffer} from '../service/offer.service';

export class CartCalculator {
  static calculateItemSubtotal(item: CartItem, offers: WeeklyOffer[]): number {
    const offer = offers.find(o => String(o.productId) === String(item.id));

    if (offer && item.quantity >= offer.requiredQuantity) {
      const priceForOfferBundle = offer.offerPrice;
      const remainingQuantity = item.quantity - offer.requiredQuantity;
      return priceForOfferBundle + (remainingQuantity * item.price);
    }

    return item.quantity * item.price;
  }
}
