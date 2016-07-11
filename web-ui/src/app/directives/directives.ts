import {PLATFORM_DIRECTIVES} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';


export const APPLICATION_DIRECTIVES = [
    ...ROUTER_DIRECTIVES,
    ...[]
];

export const DIRECTIVES = [
    {provide: PLATFORM_DIRECTIVES, multi: true, useValue: APPLICATION_DIRECTIVES }
];