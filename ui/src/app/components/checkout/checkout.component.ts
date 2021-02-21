import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  checkoutFormGroup: FormGroup;
  totalQuantity: number = 0;

  constructor(private formBuilder: FormBuilder, ) { }


  ngOnInit(): void {

    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: [''],
        lastName: [''],
        email: ['']
      }),
      address: this.formBuilder.group({
        street: [''],
        city: [''],
        state: [''],
        country:[''],
        zipCode:['']
      })
    });
  }

  onSubmit(){
    const customer = this.checkoutFormGroup.get('customer');
    const address = this.checkoutFormGroup.get('address');
    console.log(`firstName: ${customer.get('firstName').value}`);
    console.log(`lastName: ${customer.get('lastName').value}`);
    console.log(`e-mail: ${customer.get('email').value}`);
    console.log(`street: ${address.get('street').value}`);
    console.log(`city: ${address.get('city').value}`);
    console.log(`state: ${address.get('state').value}`);
    console.log(`country: ${address.get('country').value}`);
    console.log(`zipCode: ${address.get('zipCode').value}`);
  }

}
