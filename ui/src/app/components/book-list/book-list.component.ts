import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Book } from 'src/app/common/book';
import { CartItem } from 'src/app/common/cart-item';
import { BookService } from 'src/app/services/book.service';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {

  books: Book[];
  keyword: string;
  search: boolean;
  bookByAuthor: boolean;
  authorId: string;

  constructor(private bookService: BookService, private route:ActivatedRoute, private cartService: CartService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.getBooks();
    })
   
  }

  getBooks(){
    this.search = this.route.snapshot.paramMap.has('keyword');
    this.bookByAuthor = this.route.snapshot.paramMap.has('authorId');

    if(this.search){
        this.keyword = this.route.snapshot.paramMap.get('keyword');
        this.getSearchBookList();
    }
    else if(this.bookByAuthor){
       this.authorId = this.route.snapshot.paramMap.get('authorId');
       this.getBooksByAuthor();
    }
    else{
      this.getBookList();
    }
  }

  getBookList(){
     this.bookService.getBookList().subscribe(
      result => this.books = result
      );
  }

  getSearchBookList(){
      this.bookService.getSearchBook(this.keyword).subscribe(
        result => 
        this.books = result
      );
  }

  getBooksByAuthor(){
    this.bookService.getBooksByAuthor(this.authorId).subscribe(
      result => this.books = result
    );
  }
  
  updateCart(book: Book){
      let item: CartItem = new CartItem(book);
      this.cartService.addToCard(item);
  }
}
