import { Component, OnInit} from '@angular/core';
import '../../public/css/styles.css';
import {Router} from '@angular/router';
import {EventService} from "./services/event.service";
import {OauthService} from "./services/oauth.service";
import {User} from "./models/user";
import {FormGroup, FormBuilder, Validators} from '@angular/forms'
import {forbiddenNameValidator} from "./login/forbiddenEmail/forbidden-email.directive";
import {WebSocketSubscriptionService, Message} from "./services/websocketsubscription.service";

@Component({
    selector: 'my-app',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit{

    loginForm:FormGroup;
    submitted:Boolean;
    languages:string[] = ['en_US'];
    language:string = 'en_US';
    isAuthorised:Boolean = localStorage.getItem("name") !== null;
    user:User = {email: localStorage.getItem("name"), pass: null};

    constructor(private oauthService:OauthService,
                private router:Router,
                private eventService:EventService,
                private fb:FormBuilder,
                private subscriptionService: WebSocketSubscriptionService) {
        subscriptionService.messages.subscribe((msg: Message): void => {
            console.log('chatService.messages.subscribe');
            console.log(msg);
        });
    }

    signOut() {
        this.oauthService.logout();
        this.isAuthorised = false;
        this.user = {email: "", pass: ""};
        this.router.navigate(['/']);
    }

    signIn() {

        this.submitted = true;
        this.user = this.loginForm.value;

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

    ngOnInit():void {
        this.buildForm();
    }

    buildForm():void {

        this.loginForm = this.fb.group({
            'email': [this.user.email, [Validators.required,
                Validators.minLength(4),
                Validators.maxLength(24),
                forbiddenNameValidator(/test/i)]
            ],
            'pass': [this.user.pass, Validators.required]
        });
        this.loginForm.valueChanges
            .subscribe(data => this.onValueChanged(data));
        this.onValueChanged();
    }

    onValueChanged(data?:any) {
        if (!this.loginForm) {
            return;
        }
        const form = this.loginForm;

        for (const field in this.formErrors) {
            // clear previous error message (if any)
            this.formErrors[field] = '';
            const control = form.get(field);

            if (control && control.dirty && !control.valid) {
                const messages = this.validationMessages[field];
                for (const key in control.errors) {
                    this.formErrors[field] += messages[key] + ' ';
                }
            }
        }


    }

    formErrors = {
        'email': '',
        'pass': ''
    };
    validationMessages = {
        'email': {
            'required': 'Email is required.',
            'minlength': 'Email must be at least 4 characters long.',
            'maxlength': 'Email cannot be more than 24 characters long.'
        },
        'pass': {
            'required': 'Password is required.'
        }
    };

    changeLanguage() {
        this.eventService.languageEmitter.emit(this.language);
        localStorage.setItem("language", this.language);
    }
}