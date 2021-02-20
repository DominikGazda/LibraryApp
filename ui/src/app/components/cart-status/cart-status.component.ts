import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-cart-status',
  templateUrl: './cart-status.component.html',
  styleUrls: ['./cart-status.component.css']
})
export class CartStatusComponent implements OnInit {

  constructor(private cartService: CartService) { }

  totalQuantity: number = 0;

  ngOnInit(): void {
    this.updateCart();
  }

  updateCart(){ 
    this.cartService.totalQuantity.subscribe(data =>
      this.totalQuantity = data
    );
  }

}
