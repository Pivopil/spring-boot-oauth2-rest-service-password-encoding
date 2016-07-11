import {Component, OnDestroy} from '@angular/core';
import {EventService} from "../../services/event.service";

let templateInfoComponent = require("./info.html");

@Component({template: templateInfoComponent})
export class InfoComponent implements OnDestroy{

    languageSubscription:any;
    language: string;

    constructor(private _eventService:EventService){
        this.languageSubscription = _eventService.languageEmitter.subscribe(data => {
            this.language = data;
        });
    }

    ngOnDestroy():any {
        this.languageSubscription.unsubscribe();
    }

}