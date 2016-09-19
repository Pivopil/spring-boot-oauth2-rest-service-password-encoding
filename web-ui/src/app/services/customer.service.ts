import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

export const CUSTOMERS_BASE = "api/users";


@Injectable()
export class CustomerService {


    constructor(private _http:Http) {
    }

    getCustomers():Promise<any> {
        return new Promise((resolve, reject) => {
            this._http.get(CUSTOMERS_BASE, {headers: this.getUpdatedHeaders()}).map(res => res.json()).subscribe(response => {
                resolve(response);
            }, error => {
                localStorage.clear();
                reject(error);
            }, () => {
                console.log('Get users observable completed');
            });
        });
    }

    getUpdatedHeaders():Headers {
        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        headers.append('Authorization', localStorage.getItem('Authorization'));
        return headers;
    }


}