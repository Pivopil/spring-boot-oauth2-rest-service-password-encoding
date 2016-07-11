import {provideRouter, RouterConfig} from '@angular/router';
import {FORM_PROVIDERS, LocationStrategy, HashLocationStrategy} from '@angular/common';
import {Http, HTTP_PROVIDERS, RequestOptions, BaseRequestOptions, Headers} from '@angular/http';
import {OauthService} from "./services/oauth.service";
import {EventService} from "./services/event.service";
import {InfoComponent} from "./components/info/info.component";
import {CustomerService} from "./services/customer.service";
import {AdminComponent} from "./components/admin/admin.component";


class CustomRequestOptions extends BaseRequestOptions {
    headers: Headers = new Headers();
    constructor(){
        super();
        this.headers.append('Authorization', localStorage.getItem('Authorization'));
    }
}

export const routes: RouterConfig = [
    {path: 'admin-root', component: AdminComponent},
    {path: '', component: InfoComponent}
];

export const APPLICATION_PROVIDERS = [
    provideRouter(routes), {provide: LocationStrategy, useClass: HashLocationStrategy},
    ...FORM_PROVIDERS,
    ...HTTP_PROVIDERS, {provide:RequestOptions, useClass:CustomRequestOptions},
    ...[
        OauthService,
        EventService,
        CustomerService
    ]
];

export const PROVIDERS = [
    ...APPLICATION_PROVIDERS
];