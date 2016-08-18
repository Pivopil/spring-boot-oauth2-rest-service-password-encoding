import { NgModule } from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { FormsModule }        from '@angular/forms';
import {DIRECTIVES} from "./directives/directives";
import {PROVIDERS} from "./providers";
import {PIPES} from "./pipes/pipes";
import {routing} from "./providers";

@NgModule({
    imports: [
        BrowserModule, FormsModule, routing
    ],
    providers: [...PROVIDERS],
    declarations: [
        AppComponent, ...DIRECTIVES, ...PIPES
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule { }