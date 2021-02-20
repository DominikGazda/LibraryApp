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

  getAuthors(): Observable<Author[]>{
    return this.httpClient.get<Author[]>(this.baseUrl);
  }
}
