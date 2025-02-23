package com.alumnione.ecommerce.service;

import com.alumnione.ecommerce.dto.CartDto;
import com.alumnione.ecommerce.entity.Cart;
import com.alumnione.ecommerce.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CrudService<CartDto, Cart> {

    private final CartRepository cartRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<String> create(CartDto cartDto) {
        if (cartDto != null) {
            var newCart = Cart.builder()
            .lastUpdated(cartDto.lastUpdated())
            .build();
            cartRepository.save(newCart);

            return new ResponseEntity<>("Cart Created", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<String> update(Long id, CartDto cartDto) {
        if (id > 0 && cartRepository.existsById(id)) {

            var cartUpdate = Cart.builder()
            .id(id)
            .lastUpdated(cartDto.lastUpdated())
            .build();
            cartRepository.save(cartUpdate);

            return new ResponseEntity<>("Cart Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("cart can't be update", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<String> delete(Long id) {
        if (cartRepository.existsById(id) && id > 0) {
            cartRepository.deleteById(id);
            return new ResponseEntity<>("Cart Deleted", HttpStatus.OK);
        } else return new ResponseEntity<>("Does not exist", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Page<Cart>> getAll(Pageable pageable) {
        if (!cartRepository.findAll().isEmpty()) return new ResponseEntity<>(cartRepository.findAll(pageable), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<CartDto> findById(Long id) {
        if (id != null && id > 0 && cartRepository.existsById(id)) {
            var cartReference = cartRepository.getReferenceById(id);

            var cartDto = CartDto.builder()
            .lastUpdated(cartReference.getLastUpdated())
            .build();

            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}


