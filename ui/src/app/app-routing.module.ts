import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthorListComponent } from './components/author-list/author-list.component';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { BookListComponent } from './components/book-list/book-list.component';
import { CartListComponent } from './components/cart-list/cart-list.component';

const routes: Routes = [
  {path:'author/:authorId/books', component:BookListComponent},
  {path:'cart-list', component:CartListComponent},
  {path:'search/:keyword', component:BookListComponent},
  {path:'books/:id', component:BookDetailsComponent},
  {path:'books', component:BookListComponent},
  {path:'authors', component:AuthorListComponent},
  {path:'', redirectTo: '/books', pathMatch:'full'},
  {path:'**', redirectTo: '/books', pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
