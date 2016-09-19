import { Routes }         from '@angular/router';
import {LoginComponent} from "./login.component";
import {AuthGuard} from "../auth/auth-guard.service";
import {AuthService} from "../auth/auth.service";

export const loginRoutes: Routes = [
    { path: 'login', component: LoginComponent }
];

export const authProviders = [
    AuthGuard,
    AuthService
];