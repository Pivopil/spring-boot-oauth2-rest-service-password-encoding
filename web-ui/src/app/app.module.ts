import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { ReactiveFormsModule }    from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent }       from './app.component';
import { routing, appRoutingProviders } from './app.routing';

import { HeroesModule } from './heroes/heroes.module';
import {LoginComponent} from "./login/login.component";
import {DialogService} from "./services/dialog.service";
import {EventService} from "./services/event.service";
import {OauthService} from "./services/oauth.service";
import {CustomerService} from "./services/customer.service";
import {ForbiddenValidatorDirective} from "./login/forbiddenEmail/forbidden-email.directive";
// import {AdminModule} from "./admin/admin.module";
import {WebSocketService} from "./services/websocket.service";
import {WebSocketSubscriptionService} from "./services/websocketsubscription.service";



@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpModule,
        routing,
        HeroesModule,
        //AdminModule
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        ForbiddenValidatorDirective
    ],
    providers: [
        appRoutingProviders,
        DialogService,
        EventService,
        OauthService,
        CustomerService,
        WebSocketService,
        WebSocketSubscriptionService
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
}