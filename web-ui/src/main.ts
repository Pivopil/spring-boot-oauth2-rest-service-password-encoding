import { bootstrap } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';
import { AppComponent } from './app/app.component';
import {DIRECTIVES} from "./app/directives/directives";
import {PROVIDERS} from "./app/providers";
import {PIPES} from "./app/pipes/pipes";
if (process.env.ENV === 'production') {
    enableProdMode();
}


export function main(initialState?: any): Promise<any> {
    return bootstrap(AppComponent, [
        ...DIRECTIVES,
        ...PROVIDERS,
        ...PIPES
    ]).catch(err => console.error(err));
}

document.addEventListener('DOMContentLoaded', () => main());
