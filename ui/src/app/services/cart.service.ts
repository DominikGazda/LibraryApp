import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { isTemplateExpression } from 'typescript';
import { CartItem } from '../common/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartList: CartItem[] = [];
  totalQuantity: Subject<number> = new Subject<number>();
  currentItem: boolean= false;

  constructor() { }

  addToCard(theCartItem: CartItem){
    let existingCartItem: CartItem = undefined;

    if(this.cartList.length >0){
      existingCartItem = this.cartList.find(item => item.bookid === theCartItem.bookid);
      this.currentItem = (existingCartItem != undefined)
    }
      
    
    if(!this.currentItem)
       this.cartList.push(theCartItem);
    else
       existingCartItem.cartQuantity+=1;      
    
       this.computeQuantity();    
  }

  computeQuantity(){
    let currentQuantity: number = 0;

    for(let temporaryItem of this.cartList){
      console.log(temporaryItem.bookName);
      currentQuantity += temporaryItem.cartQuantity;
    }

    this.totalQuantity.next(currentQuantity); 
  }

  decreaseQuantity(item: CartItem){
    item.cartQuantity --;
    
    if(item.cartQuantity == 0)
      this.remove(item);
    
    this.computeQuantity();
  }

  remove(item: CartItem){
    const index = this.cartList.findIndex(temp => temp.bookid === item.bookid)

    if(index > -1)
      this.cartList.splice(index, 1);
      this.computeQuantity();
  }
}
