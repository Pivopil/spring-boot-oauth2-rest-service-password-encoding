import { Component } from '@angular/core';
import '../../public/css/styles.css';
import {Router} from '@angular/router';
import {EventService} from "./services/event.service";
import {OauthService} from "./services/oauth.service";
import {User} from "./models/user";

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html'
})
export class AppComponent {
    languages:string[] = ['en_US'];
    language:string = 'en_US';
    isAuthorised:Boolean = localStorage.getItem("name") !== null;
    user:User = {email: localStorage.getItem("name"), pass: null};

    constructor(private oauthService:OauthService,
                private router:Router,
                private eventService: EventService) {
    }

    signOut() {
        this.oauthService.logout();
        this.isAuthorised = false;
        this.user = {email: "", pass: ""};
        this.router.navigate(['/']);
    }

    signIn() {
        this.oauthService.login(this.user)
            .then(() => {
                this.isAuthorised = true;
                this.user = {email: localStorage.getItem("name"), pass: ""};
                this.router.navigate(['/admin-root']);
            }).catch(() => {
                this.isAuthorised = false;
                this.user = {email: "", pass: ""};
                this.router.navigate(['/']);
            }
        );
    }

    changeLanguage() {
        this.eventService.languageEmitter.emit(this.language);
        localStorage.setItem("language", this.language);
    }
}