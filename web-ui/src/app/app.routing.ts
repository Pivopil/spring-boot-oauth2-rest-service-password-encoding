import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule }   from '@angular/router';
import {loginRoutes} from "./login/login.routing";
import {authProviders} from "./login/login.routing";
import {CanDeactivateGuard} from "./auth/can-deactivate-guard.service";


const crisisCenterRoutes: Routes = [
    {
        path: '',
        redirectTo: '/heroes',
        pathMatch: 'full'
    },
    {
        path: 'crisis-center',
        loadChildren: () =>
            new Promise(resolve =>
                (require as any).ensure([], () =>
                    resolve(require('./crisis-center/crisis-center.module').CrisisCenterModule)
                )
            )
    }
];

const appRoutes: Routes = [
    ...loginRoutes,
    ...crisisCenterRoutes
];

export const appRoutingProviders: any[] = [
    authProviders,
    CanDeactivateGuard
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes,  { useHash: true });
