import {Component, OnInit, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';

import {EventService} from "../../services/event.service";
import {CustomerService} from "../../services/customer.service";


let templateOfAdminComponent = require("./admin.html");

@Component({
    template: templateOfAdminComponent
})
export class AdminComponent implements OnInit, OnDestroy {


    selectedCustomer:any;
    language:string = 'en_US';
    customers:any;
    languageSubscription:any;

    constructor(private _router:Router,
                private _customerService:CustomerService,
                private _eventService:EventService) {

        this.languageSubscription = _eventService.languageEmitter.subscribe(data => {
            this.language = data;
        });

    }

    ngOnInit() {
        return Promise
            .all([this._customerService.getCustomers()])
            .then(res => {
                this.customers = res[0];

                if (this.customers.length > 0) {
                    this.selectedCustomer = this.customers[0].id;
                }
            })
            .catch(err => console.log(err));
    }

    ngOnDestroy():any {
        this.languageSubscription.unsubscribe();
    }

    customerDialog(isNew:boolean):void {
        console.log("Customer Dialog Functionality");
    }

}
