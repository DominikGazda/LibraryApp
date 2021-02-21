import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Author } from '../common/author';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  private baseUrl = "http://localhost:8080/api/authors";

  constructor(private httpClient: HttpClient) { }

  getPaginatedAuthorList(pageNumber: number, pageSize: number):Observable<GetResponseAuthor>{
    const bookUrl = `${this.baseUrl}/?pageNumber=${pageNumber}&pageSize=${pageSize}`;
    return this.httpClient.get<GetResponseAuthor>(bookUrl);
}

  getAuthors(): Observable<Author[]>{
    return this.httpClient.get<Author[]>(this.baseUrl);
  }
}

interface GetResponseAuthor{
  content: Author[];
   pageable:{
    pageSize: number;
    pageNumber: number;
  }
  totalElements: number;
  }
