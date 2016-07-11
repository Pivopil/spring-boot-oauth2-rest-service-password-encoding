import { Component } from '@angular/core';
import '../../public/css/app.css';

import {Router} from '@angular/router';
import {EventService} from "./services/event.service";
import {OauthService} from "./services/oauth.service";
import {User} from "./models/user";



@Component({
    selector: 'my-app',
    template: require('./app.component.html')
})
export class AppComponent {
    languages:string[] = ['en_US'];
    language:string = 'en_US';
    isAuthorised:Boolean = localStorage.getItem("name") !== null;
    user:User = {email: localStorage.getItem("name"), pass: null};

    constructor(private _oauthService:OauthService,
                private _router:Router,
                private _eventService: EventService) {
    }

    signOut() {
        this._oauthService.logout();
        this.isAuthorised = false;
        this.user = {email: "", pass: ""};
        this._router.navigate(['/']);
    }

    signIn() {
        this._oauthService.login(this.user)
            .then(isAdmin => {
                this.isAuthorised = true;
                this.user = {email: localStorage.getItem("name"), pass: ""};
                this._router.navigate(['/admin-root']);
            }).catch(error => {
                this.isAuthorised = false;
                this.user = {email: "", pass: ""};
                this._router.navigate(['/']);
            }
        );
    }

    changeLanguage() {
        this._eventService.languageEmitter.emit(this.language);
        localStorage.setItem("language", this.language);
    }
}