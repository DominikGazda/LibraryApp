import { Route } from '@angular/compiler/src/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Author } from 'src/app/common/author';
import { AuthorService } from 'src/app/services/author.service';

@Component({
  selector: 'app-author-list',
  templateUrl: './author-list.component.html',
  styleUrls: ['./author-list.component.css']
})
export class AuthorListComponent implements OnInit {

  authors: Author[];

  constructor(private authorService: AuthorService, private route: Router) { }

  ngOnInit(): void {
      this.getAuthors();
  }

  getAuthors(){
    this.authorService.getAuthors().subscribe(
      result => 
      this.authors = result);
  }

  findBooksByAuthor(author: Author){
    console.log(author.authorId);
    this.route.navigateByUrl(`/author/${author.authorId}/books`);
  }
}
