import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/common/cart-item';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-cart-list',
  templateUrl: './cart-list.component.html',
  styleUrls: ['./cart-list.component.css']
})
export class CartListComponent implements OnInit {

  cartList: CartItem[];
  totalQuantity: number;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.updateCartList();
  }

  updateCartList(){
    this.cartList = this.cartService.cartList;
    this.cartService.totalQuantity.subscribe(
      data => this.totalQuantity = data
    );
  }

  increaseQuantity(item: CartItem){
    this.cartService.addToCard(item);
  }

  decreaseQuantity(item: CartItem){
    this.cartService.decreaseQuantity(item);
  }
}
