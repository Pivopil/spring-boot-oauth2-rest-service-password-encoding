import {Injectable, EventEmitter} from '@angular/core';

@Injectable()
export class EventService {
    languageEmitter:EventEmitter<any> = new EventEmitter();
}

