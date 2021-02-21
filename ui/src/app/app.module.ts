import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BookListComponent } from './components/book-list/book-list.component';
import { HttpClientModule } from '@angular/common/http';
import { BookDetailsComponent } from './components/book-details/book-details.component';
import { SearchComponent } from './components/search/search.component';
import { AuthorListComponent } from './components/author-list/author-list.component';
import { CartStatusComponent } from './components/cart-status/cart-status.component';
import { CartListComponent } from './components/cart-list/cart-list.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    BookListComponent,
    BookDetailsComponent,
    SearchComponent,
    AuthorListComponent,
    CartStatusComponent,
    CartListComponent,
    CheckoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
