import { Book } from "./book";

export class CartItem {

    bookid: string;
    bookName: string;
    availableQuantity: number;
    imageUrl: string;
    cartQuantity: number;

    constructor(book: Book){
        this.bookid = book.bookid;
        this.bookName = book.bookName;
        this.availableQuantity = book.availableQuantity;
        this.imageUrl = book.imageUrl;
        
        this.cartQuantity = 1;
    }
}
