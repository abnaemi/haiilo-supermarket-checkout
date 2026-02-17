package com.haiilo.interview.haiilosupermarketcheckout.domain.service;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.CartItemRequestDTO;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Cart;
import com.haiilo.interview.haiilosupermarketcheckout.domain.model.Product;
import com.haiilo.interview.haiilosupermarketcheckout.infrastructure.persistence.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void createCart_success() {
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart result = cartService.createCart();

        assertThat(result).isNotNull();
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addOrUpdateProduct_ShouldInvokeCartLogic() {
        UUID cartId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        Product product = new Product("Apple", BigDecimal.ONE);
        product.setId(productId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productService.getProductEntityById(productId)).thenReturn(product);

        cartService.addOrUpdateProduct(cartId, new CartItemRequestDTO(productId, 5));

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(5);
        verify(cartRepository).save(cart);
    }

    @Test
    void deleteCart_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        when(cartRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> cartService.deleteCart(id))
                .isInstanceOf(EntityNotFoundException.class);
    }
}