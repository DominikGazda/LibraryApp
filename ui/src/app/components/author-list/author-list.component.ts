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

  thePageNumber: number = 1;
  thePageSize: number = 4;
  theTotalElements: number = 0;

  constructor(private authorService: AuthorService, private route: Router) { }

  ngOnInit(): void {
      this.getPaginatedAuthors();
  }

  getPaginatedAuthors(){
    this.authorService.getPaginatedAuthorList(this.thePageNumber-1, this.thePageSize).subscribe(
      result =>{
        this.authors = result.content;
        this.thePageNumber = result.pageable.pageNumber+1;
        this.thePageSize = result.pageable.pageSize;
        this.theTotalElements = result.totalElements;
      }
      );
  }

  getAuthors(){
    this.authorService.getAuthors().subscribe(
      result => 
      this.authors = result);
  }

  findBooksByAuthor(author: Author){
    this.route.navigateByUrl(`/author/${author.authorId}/books`);
  }
}
