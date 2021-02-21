import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Book } from '../common/book';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private baseUrl = "http://localhost:8080/api/books"


  constructor(private httpClient: HttpClient) { }

  getPaginatedBookList(pageNumber: number, pageSize: number):Observable<GetResponseBook>{
      const bookUrl = `${this.baseUrl}/?pageNumber=${pageNumber}&pageSize=${pageSize}`;
      return this.httpClient.get<GetResponseBook>(bookUrl);
  }

  getBookList(): Observable<Book[]>{
    return this.httpClient.get<Book[]>(this.baseUrl);
  }

  getBook(bookId: number): Observable<Book> {
    const bookUrl = `${this.baseUrl}/${bookId}`;
    return this.httpClient.get<Book>(bookUrl);
  }

  getSearchBook(keyword: string): Observable<Book[]>{
    const bookUrl = `${this.baseUrl}/search/findBooksByName?name=${keyword}`;
    return this.httpClient.get<Book[]>(bookUrl);
  }

  getBooksByAuthor(autorId: string): Observable<Book[]>{
    const bookUrl = `http://localhost:8080/api/authors/${autorId}/books`;
    return this.httpClient.get<Book[]>(bookUrl);
  }

  getPaginatedBooksByAuthor(pageNumber: number, pageSize: number, authorId: string): Observable<GetResponseBook>{
    const bookUrl = `http://localhost:8080/api/authors/${authorId}/books?pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.httpClient.get<GetResponseBook>(bookUrl);
  }

}

interface GetResponseBook{
   content: Book[];
    pageable:{
     pageSize: number;
     pageNumber: number;
   }
   totalElements: number;
   }
