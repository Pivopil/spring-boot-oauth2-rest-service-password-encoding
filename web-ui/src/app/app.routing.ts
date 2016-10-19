import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule }   from '@angular/router';
import {loginRoutes} from "./login/login.routing";
import {authProviders} from "./login/login.routing";
import {CanDeactivateGuard} from "./auth/can-deactivate-guard.service";
import {heroesRoutes} from "./heroes/heroes.routing";
import {adminRoutes} from "./admin/admin.routing";


const crisisCenterRoutes:Routes = [
    {
        path: '',
        redirectTo: '/heroes',
        pathMatch: 'full'
    },
    {
        path: 'heroes',
        loadChildren: () =>
            new Promise(resolve =>
                (require as any).ensure([], () =>
                    resolve(require('./heroes/heroes.module').HeroesModule)
                )
            )
    },
    //{
    //    path: 'admin',
    //    loadChildren: () =>
    //        new Promise(resolve =>
    //            (require as any).ensure([], () =>
    //                resolve(require('./admin/admin.module').AdminModule)
    //            )
    //        )
    //},
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

const appRoutes:Routes = [
    ...loginRoutes,
    ...crisisCenterRoutes,
    ...heroesRoutes,
    //...adminRoutes
];

export const appRoutingProviders:any[] = [
    authProviders,
    CanDeactivateGuard
];

export const routing:ModuleWithProviders = RouterModule.forRoot(appRoutes, {useHash: true});
