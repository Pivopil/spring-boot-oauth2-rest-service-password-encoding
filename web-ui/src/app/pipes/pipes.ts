import {PLATFORM_PIPES} from '@angular/core';
import {TranslatePipe} from "./translate.pipe";

export const APPLICATION_PIPES = [
    ...[TranslatePipe]
];

export const PIPES = [
    {provide: PLATFORM_PIPES, multi: true, useValue: APPLICATION_PIPES }
];