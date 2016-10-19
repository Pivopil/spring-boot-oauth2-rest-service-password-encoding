import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';
import { FormsModule }    from '@angular/forms';
import {adminRouting} from "./admin.routing";
import {AdminComponent} from "./admin.component";
import {CustomerService} from "../services/customer.service";
import {EventService} from "../services/event.service";



@NgModule({
    imports: [
        CommonModule,
        adminRouting
    ],
    declarations: [
        AdminComponent
    ],
    providers: [
    ]
})
export class AdminModule {}