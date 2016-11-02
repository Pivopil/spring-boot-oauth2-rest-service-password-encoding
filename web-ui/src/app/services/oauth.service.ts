import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import {User} from "../models/user";
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class OauthService {

    me:any = {};

    oauthConfig:any = {
        client_id: 'clientapp',
        client_secret: 'admin',
        url: 'oauth/token',
        grant_type: 'password',
        scope: 'read write'
    };

    constructor(private _http:Http) {}

    login(user: User):any {

        return new Promise((resolve, reject) => {

            let resolved: boolean = false;

            let headers = new Headers();

            headers.append('Authorization', 'Basic ' + btoa(this.oauthConfig.client_id + ':' + this.oauthConfig.client_secret));
            headers.append('Accept', 'application/json');

            let body = "password=" + user.pass +
                "&username=" + user.email +
                "&grant_type=" + this.oauthConfig.grant_type +
                "&scope=" + this.oauthConfig.scope +
                "&client_secret=" + this.oauthConfig.client_secret +
                "&client_id=" + this.oauthConfig.client_id;


            this._http.post(this.oauthConfig.url + "?" + body, null, {headers: headers}).map(res => res.json()).subscribe(response => {
                localStorage.setItem('Authorization', "Bearer " + response['access_token']);

                headers.delete('Authorization');
                headers.append('Authorization', localStorage.getItem('Authorization'));

                this._http.get("api/me", {headers: headers}).map(res => res.json()).subscribe((myData) => {

                    let isAdmin = myData['roles'].indexOf('ROLE_ADMIN') > -1;

                    localStorage.setItem("name", myData['name']);
                    localStorage.setItem("isAdmin", "" + isAdmin);

                    this.me = myData;
                    resolve(isAdmin);
                }, reject);
            }, reject);

            resolved = true;

            if (!resolved) {
                reject('not found');
            }
        });
    }

    logout():void {
        this.me = {};
        localStorage.clear();
    }

}